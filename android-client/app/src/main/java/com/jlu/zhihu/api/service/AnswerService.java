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

import com.jlu.zhihu.model.Answer;

import java.util.List;

public interface AnswerService extends ListService {

    String PATH_ANSWER = HOST + "/api/answer";
    String PATH_ALL_ANSWER = HOST + "/api/answer/all";
    String PATH_ANSWER_METADATA = HOST + "/api/answer/metadata";
    String PATH_ANSWER_QUESTION = HOST + "/api/answer/question";

    void getAnswer(int id);

    interface AnswerCallback {

        void onGetAnswer(Answer answer);
    }

    void setAnswerCallback(AnswerCallback answerCallback);

    void metaData(Answer answer);
}
