/*
 *    Copyright [2019] [chengjie.jlu@qq.com]
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.jlu.zhihu.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jlu.zhihu.R;
import com.jlu.zhihu.activity.AnswerActivity;
import com.jlu.zhihu.activity.ArticleActivity;
import com.jlu.zhihu.adapter.AnswerAdapter;
import com.jlu.zhihu.adapter.RecyclerViewAdapter;
import com.jlu.zhihu.api.service.ListService;
import com.jlu.zhihu.event.Event;
import com.jlu.zhihu.event.EventBus;
import com.jlu.zhihu.event.EventHandler;
import com.jlu.zhihu.model.Answer;
import com.jlu.zhihu.net.OkHttpHelper;
import com.jlu.zhihu.net.Request;
import com.jlu.zhihu.net.Response;
import com.jlu.zhihu.util.LogUtil;
import com.jlu.zhihu.model.ListItemModel;
import com.jlu.zhihu.util.TaskRunner;
import com.jlu.zhihu.util.ToastUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.jlu.zhihu.api.service.AnswerService.PATH_ANSWER_QUESTION;
import static com.jlu.zhihu.api.service.BaseService.OK;
import static com.jlu.zhihu.api.service.BaseService.TYPE_RESPONSE_LIST_ANSWER;

public class ListFragment extends Fragment implements
        ScrollToHeadListener, EventHandler, ListService.ListCallback {

    private static final String TAG = "ListFragment";

    @BindView(R.id.list)
    RecyclerView recyclerView;

    @BindView(R.id.refresh)
    RefreshLayout refreshLayout;

    private static final int REFRESH_STOP = 0;
    private static final int SCROLL_VERTICALLY_UP = -1;

    private View rootView;

    private final List<ListItemModel> list = new ArrayList<>();

    private ListService listService;

    private RecyclerViewAdapter adapter;

    private final EventBus eventBus = EventBus.getInstance();

    public void setListService(ListService listService) {
        this.listService = listService;
    }

    public static ListFragment newInstance(ListService listService) {
        ListFragment fragment = new ListFragment();
        fragment.setListService(listService);
        return fragment;
    }

    @Override
    @Nullable
    @SuppressLint("InflateParams")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView != null) {
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if (parent != null) {
                parent.removeView(rootView);
            }
        } else {
            rootView = inflater.inflate(R.layout.fragment_list, null);
            ButterKnife.bind(this, rootView);
            adapter = new RecyclerViewAdapter(getContext(), list);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);

            refreshLayout.setOnRefreshListener(refreshLayout -> listService.refresh(refreshLayout));
            refreshLayout.setOnLoadMoreListener(refreshLayout -> listService.loadMore(refreshLayout));

            listService.setListCallback(this);
            eventBus.register(this);
            listService.init();
        }
        return rootView;
    }

    @Override
    public void onInit(List<ListItemModel> list) {
        this.list.clear();
        this.list.addAll(list);
        eventBus.onMainThread(() -> adapter.notifyDataSetChanged());
    }

    @Override
    public void onRefresh(List<ListItemModel> list, RefreshLayout refreshLayout) {
        eventBus.onMainThread(refreshLayout::finishRefresh);
        if (list != null)
            LogUtil.d(TAG, "on list refresh, data size %d, service: %s",
                    list.size(), listService.getClass().getSimpleName());
        else {
            ToastUtil.msg("刷新失败，请重试");
            LogUtil.w(TAG, "refresh list failed.");
            return;
        }
        if (list.size() > 0) {
            this.list.clear();
            this.list.addAll(list);
            eventBus.onMainThread(() -> adapter.notifyDataSetChanged());
        }
    }

    @Override
    public void onLoadMore(List<ListItemModel> list, RefreshLayout refreshLayout) {
        eventBus.onMainThread(refreshLayout::finishLoadMore);
        if (list != null)
            LogUtil.d(TAG, "on list load more, data size %d, service: %s",
                    list.size(), listService.getClass().getSimpleName());
        else {
            ToastUtil.msg("加载失败，请重试");
            LogUtil.w(TAG, "load more failed.");
            return;
        }
        if (list.size() > 0) {
            this.list.addAll(list);
            eventBus.onMainThread(() -> adapter.notifyItemInserted(this.list.size()));
        } else {
            ToastUtil.msg("没有更多数据了");
        }
    }

    @Override
    public boolean handleMsg(int what, String msg, Object o) {
        switch (what) {
            case Event.Click.ON_ARTICLE_CLICK: {
                Intent intent = new Intent(getActivity(), ArticleActivity.class);
                intent.putExtra("id", (int) o);
                startActivity(intent);
                return true;
            }
            case Event.Click.ON_ANSWER_CLICK: {
                Intent intent = new Intent(getActivity(), AnswerActivity.class);
                intent.putExtra("id", (int) o);
                startActivity(intent);
                return true;
            }
            case Event.Click.ON_QUESTION_CLICK:
                initAnswers((int) o);
                return true;
        }
        return false;
    }

    @Override
    public void onScrollToHead() {
        if (refreshLayout.getState() == RefreshState.Loading ||
                recyclerView.getScrollState() != REFRESH_STOP) return;
        if (recyclerView.canScrollVertically(SCROLL_VERTICALLY_UP))
            recyclerView.scrollToPosition(0);
        else refreshLayout.autoRefresh();
    }

    @SuppressLint("SetTextI18n")
    private void initAnswers(int id) {
        TaskRunner.execute(() -> {
            Request<Integer> request = new Request<>();
            request.body = id;
            request.args = new HashMap<>();
            request.args.put("page", "0");
            request.args.put("size", "10000");
            Response<List<Answer>> response = OkHttpHelper.post(
                    PATH_ANSWER_QUESTION, request, TYPE_RESPONSE_LIST_ANSWER);
            if (response != null && response.status == OK) {
                List<Answer> answers = response.body;
                eventBus.onMainThread(() -> {
                    BottomSheetDialog dialog = new BottomSheetDialog(Objects.requireNonNull(getActivity()));
                    @SuppressLint("InflateParams")
                    View contentView = getLayoutInflater().inflate(R.layout.dialog_commets, null);
                    dialog.setContentView(contentView);
                    RecyclerView recyclerView = contentView.findViewById(R.id.list);
                    TextView textView = contentView.findViewById(R.id.title);
                    textView.setText(answers.size() + "个回答");
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(new AnswerAdapter(getActivity(), answers));
                    dialog.show();
                });
            } else {
                ToastUtil.msg("加载失败，请稍后重试。");
            }
        });
    }
}
