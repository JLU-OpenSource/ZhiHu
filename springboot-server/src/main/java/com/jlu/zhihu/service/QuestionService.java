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

package com.jlu.zhihu.service;

import com.jlu.zhihu.model.Question;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuestionService {

    String QUESTION_PATH = "/Users/chengjie/github/ZhiHu/question/";

    int SUMMARY_LENGTH = 250;

    int PAGE_SIZE = 3;

    Question createQuestion(Question question, String html, String raw);

    List<Question> findAll(Pageable pageable);

    Question findById(int id);
}
