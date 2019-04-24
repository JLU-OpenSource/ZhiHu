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

package com.jlu.zhihu.api;

import com.jlu.zhihu.api.service.AnswerService;
import com.jlu.zhihu.model.Answer;
import com.jlu.zhihu.net.OkHttpHelper;
import com.jlu.zhihu.net.Request;
import com.jlu.zhihu.net.Response;
import com.jlu.zhihu.util.TaskRunner;
import com.jlu.zhihu.util.ToastUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class AnswerApi implements AnswerService {

    private static AnswerService instance;

    private int currentPage = 0;

    private ListCallback listCallback;
    private AnswerCallback answerCallback;

    public static AnswerService getInstance() {
        if (instance == null) {
            synchronized (AnswerApi.class) {
                if (instance == null)
                    instance = new AnswerApi();
            }
        }
        return instance;
    }

    @Override
    public void init() {
        TaskRunner.execute(() -> {
            currentPage = 0;
            Response<List<Answer>> response = OkHttpHelper.post(
                    PATH_ALL_ANSWER, listRequest(currentPage), TYPE_RESPONSE_LIST_ANSWER);
            if (response != null && response.status == OK)
                listCallback.onInit(new ArrayList<>(response.body));
            else listCallback.onInit(new ArrayList<>(0));
        });
    }

    @Override
    public void refresh(RefreshLayout refreshLayout) {
        TaskRunner.execute(() -> {
            currentPage = 0;
            Response<List<Answer>> response = OkHttpHelper.post(
                    PATH_ALL_ANSWER, listRequest(currentPage), TYPE_RESPONSE_LIST_ANSWER);
            if (response != null && response.status == OK)
                listCallback.onRefresh(new ArrayList<>(response.body), refreshLayout);
            else listCallback.onRefresh(null, refreshLayout);
        });
    }

    @Override
    public void loadMore(RefreshLayout refreshLayout) {
        TaskRunner.execute(() -> {
            currentPage++;
            Response<List<Answer>> response = OkHttpHelper.post(
                    PATH_ALL_ANSWER, listRequest(currentPage), TYPE_RESPONSE_LIST_ANSWER);
            if (response != null && response.status == OK)
                listCallback.onLoadMore(new ArrayList<>(response.body), refreshLayout);
            else listCallback.onLoadMore(null, refreshLayout);
        });
    }

    @Override
    public void setListCallback(ListCallback callback) {
        this.listCallback = callback;
    }

    @Override
    public void getAnswer(int id) {
        TaskRunner.execute(() -> {
            Response<Answer> response = OkHttpHelper.get(
                    PATH_ANSWER + "/" + id, TYPE_RESPONSE_ANSWER);
            if (response != null && response.status == OK)
                this.answerCallback.onGetAnswer(response.body);
            else this.answerCallback.onGetAnswer(null);
        });
    }

    @Override
    public void setAnswerCallback(AnswerCallback answerCallback) {
        this.answerCallback = answerCallback;
    }

    @Override
    public void metaData(Answer answer) {
        TaskRunner.execute(() -> {
            Request<Answer> request = new Request<>();
            request.body = answer;
            Response<Answer> response = OkHttpHelper.
                    post(PATH_ANSWER_METADATA, request, TYPE_RESPONSE_ANSWER);
            if (response.status != 200) {
                ToastUtil.msg("操作失败，请稍后重试！");
            }
        });
    }
}
