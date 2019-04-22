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

import com.jlu.zhihu.model.Answer;
import com.jlu.zhihu.model.User;
import com.jlu.zhihu.repository.AnswerRepository;
import com.jlu.zhihu.service.AnswerService;
import com.jlu.zhihu.util.FileUtil;
import com.jlu.zhihu.util.RawUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;
    private final Environment environment;

    @Autowired
    public AnswerServiceImpl(AnswerRepository answerRepository,
                             Environment environment) {
        this.answerRepository = answerRepository;
        this.environment = environment;
    }

    @Override
    public Answer createAnswer(Answer answer, String html, String raw) {
        answer.summary = RawUtil.getSummaryByRaw(raw, SUMMARY_LENGTH);
        answer = answerRepository.save(answer);
        FileUtil.write2File(html, getAnswerPath() + answer.aid + ".html");
        return answer;
    }

    @Override
    public Answer metaData(Answer answer) {
        Answer database = answerRepository.findAnswerByAid(answer.aid);
        if (database != null) database = answerRepository.save(answer);
        else database = answer;
        return database;
    }

    @Override
    public Answer findById(int aid) {
        return answerRepository.findAnswerByAid(aid);
    }

    @Override
    public Answer findByAuthor(int qid, User author) {
        return answerRepository.findByAuthorAndQid(author, qid);
    }

    @Override
    public List<Answer> all(Pageable pageable) {
        return answerRepository.findAll(pageable).getContent();
    }

    @Override
    public List<Answer> allUnderQid(int qid, Pageable pageable) {
        return answerRepository.findAllByQid(qid, pageable);
    }

    @Override
    public List<Answer> findCollect(User user) {
        List<Answer> all = answerRepository.findAll();
        List<Answer> collect = new ArrayList<>();
        for (Answer a : all) {
            if (a.collect.contains(user))
                collect.add(a);
        }
        return collect;
    }

    @Override
    public void removeCollect(Answer answer, User user) {
        answer.collect.remove(user);
        answerRepository.save(answer);
    }

    @Override
    public long countAll() {
        return answerRepository.count();
    }

    @Override
    public String getAnswerPath() {
        return environment.getProperty("static-resources-path") + "answer/";
    }
}
