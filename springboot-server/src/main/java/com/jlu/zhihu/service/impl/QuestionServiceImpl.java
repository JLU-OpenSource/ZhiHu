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

package com.jlu.zhihu.service.impl;

import com.jlu.zhihu.model.Question;
import com.jlu.zhihu.repository.QuestionRepository;
import com.jlu.zhihu.service.QuestionService;
import com.jlu.zhihu.util.FileUtil;
import com.jlu.zhihu.util.RawUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionServiceImpl(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    @Override
    public Question createQuestion(Question question, String html, String raw) {
        question.summary = RawUtil.getSummaryByRaw(raw, SUMMARY_LENGTH);
        question = questionRepository.save(question);
        FileUtil.write2File(html, QUESTION_PATH + question.id + ".html");
        return questionRepository.save(question);
    }

    @Override
    public List<Question> findAll(Pageable pageable) {
        return questionRepository.findAll(pageable).getContent();
    }

    @Override
    public Question findById(int id) {
        return questionRepository.findById(id);
    }
}
