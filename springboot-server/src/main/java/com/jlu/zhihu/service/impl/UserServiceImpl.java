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
import com.jlu.zhihu.util.Encoder;
import com.jlu.zhihu.model.User;
import com.jlu.zhihu.repository.UserRepository;
import com.jlu.zhihu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           QuestionRepository questionRepository) {
        this.userRepository = userRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(Encoder.md5(email));
    }

    @Override
    public User register(User user) {
        user.email = Encoder.md5(user.email);
        user.password = Encoder.md5(user.password);
        return userRepository.save(user);
    }

    @Override
    public User login(User user) {
        user.email = Encoder.md5(user.email);
        user.password = Encoder.md5(user.password);
        return userRepository.findByEmailAndPassword(user.email, user.password);
    }

    @Override
    public List<Question> getQuestion(int id) {
        User user = userRepository.findUserById(id);
        return questionRepository.findByAuthor(user);
    }
}
