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
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jlu.zhihu.R;
import com.jlu.zhihu.adapter.BaseRecyclerViewAdapter;
import com.jlu.zhihu.event.EventBus;
import com.jlu.zhihu.event.EventHandler;
import com.jlu.zhihu.util.TaskRunner;
import com.jlu.zhihu.view.ListItemModel;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseListFragment extends Fragment implements ScrollToHeadListener, EventHandler {

    @BindView(R.id.list)
    RecyclerView recyclerView;

    @BindView(R.id.refresh)
    RefreshLayout refreshLayout;

    private static final int REFRESH_STOP = 0;
    private static final int SCROLL_VERTICALLY_UP = -1;

    private View rootView;

    private List<ListItemModel> list = new ArrayList<>();

    private BaseRecyclerViewAdapter adapter;

    private EventBus eventBus = EventBus.getInstance();

    private OnRefreshListener refreshListener = refreshLayout ->
            TaskRunner.execute(() -> {
                list.clear();
                onRefresh(list);
                eventBus.onMainThread(() -> adapter.notifyDataSetChanged());
                refreshLayout.finishRefresh();
            });

    private OnLoadMoreListener loadMoreListener = refreshLayout ->
            TaskRunner.execute(() -> {
                onLoadMore(list);
                eventBus.onMainThread(() -> adapter.notifyItemInserted(list.size()));
                refreshLayout.finishLoadMore();
            });

    public BaseListFragment() {
        eventBus.registered(this);
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
            adapter = new BaseRecyclerViewAdapter(getContext(), list);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adapter);
            refreshLayout.setOnRefreshListener(refreshListener);
            refreshLayout.setOnLoadMoreListener(loadMoreListener);
            TaskRunner.execute(() -> {
                onInit(list);
                eventBus.onMainThread(() -> adapter.notifyDataSetChanged());
            });
        }
        return rootView;
    }

    abstract void onInit(List<ListItemModel> data);

    abstract void onLoadMore(List<ListItemModel> data);

    abstract void onRefresh(List<ListItemModel> data);

    @Override
    public void onScrollToHead() {
        if (refreshLayout.getState() == RefreshState.Loading ||
                recyclerView.getScrollState() != REFRESH_STOP) return;
        if (recyclerView.canScrollVertically(SCROLL_VERTICALLY_UP))
            recyclerView.scrollToPosition(0);
        else refreshLayout.autoRefresh();
    }

    @Override
    public boolean handleMsg(int what, String msg, Object o) {
        return false;
    }
}
