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

import com.jlu.zhihu.model.Answer;
import com.jlu.zhihu.model.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AnswerService {

    int SUMMARY_LENGTH = 200;

    Answer createAnswer(Answer answer, String html, String raw);

    Answer metaData(Answer answer);

    Answer findById(int id);

    Answer findByAuthor(int qid, User author);

    List<Answer> all(Pageable pageable);

    List<Answer> allUnderQid(int qid, Pageable pageable);

    List<Answer> findCollect(User user);

    void removeCollect(Answer answer, User user);

    long countAll();

    String getAnswerPath();
}
