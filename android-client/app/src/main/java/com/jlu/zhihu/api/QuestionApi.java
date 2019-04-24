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

import com.jlu.zhihu.api.service.QuestionService;
import com.jlu.zhihu.model.Question;
import com.jlu.zhihu.net.OkHttpHelper;
import com.jlu.zhihu.net.Response;
import com.jlu.zhihu.util.TaskRunner;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class QuestionApi implements QuestionService {

    private static QuestionService instance;

    private int currentPage = 0;

    private ListCallback listCallback;

    private QuestionCallback questionCallback;

    public static QuestionService getInstance() {
        if (instance == null) {
            synchronized (QuestionApi.class) {
                if (instance == null)
                    instance = new QuestionApi();
            }
        }
        return instance;
    }

    private QuestionApi() {
    }


    @Override
    public void init() {
        TaskRunner.execute(() -> {
            currentPage = 0;
            Response<List<Question>> response = OkHttpHelper.post(
                    PATH_ALL_QUESTION, listRequest(currentPage), TYPE_RESPONSE_LIST_QUESTION);
            if (response != null && response.status == OK)
                listCallback.onInit(new ArrayList<>(response.body));
            else listCallback.onInit(new ArrayList<>(0));
        });
    }

    @Override
    public void refresh(RefreshLayout refreshLayout) {
        TaskRunner.execute(() -> {
            currentPage = 0;
            Response<List<Question>> response = OkHttpHelper.post(
                    PATH_ALL_QUESTION, listRequest(currentPage), TYPE_RESPONSE_LIST_QUESTION);
            if (response != null && response.status == OK)
                listCallback.onRefresh(new ArrayList<>(response.body), refreshLayout);
            else listCallback.onRefresh(null, refreshLayout);
        });
    }

    @Override
    public void loadMore(RefreshLayout refreshLayout) {
        TaskRunner.execute(() -> {
            currentPage++;
            Response<List<Question>> response = OkHttpHelper.post(
                    PATH_ALL_QUESTION, listRequest(currentPage), TYPE_RESPONSE_LIST_QUESTION);
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
    public void createQuestion(Question question) {
    }

    @Override
    public void loadQuestionDetail() {
    }

    @Override
    public void setQuestionCallback(QuestionCallback callback) {
        this.questionCallback = callback;
    }
}
