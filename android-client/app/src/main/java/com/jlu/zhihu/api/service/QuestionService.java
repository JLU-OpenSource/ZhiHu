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

package com.jlu.zhihu.api.service;

import com.jlu.zhihu.model.Question;


public interface QuestionService extends ListService {

    String PATH_CREATE_QUESTION = HOST + "/api/question/create";
    String PATH_ALL_QUESTION = HOST + "/api/question/all";
    String PATH_QUESTION = HOST + "/api/question/";

    interface QuestionCallback {

        void onCreateQuestion(Question question);

        void onLoadQuestionDetail(String html);
    }

    void createQuestion(Question question);

    void loadQuestionDetail();

    void setQuestionCallback(QuestionCallback callback);
}
