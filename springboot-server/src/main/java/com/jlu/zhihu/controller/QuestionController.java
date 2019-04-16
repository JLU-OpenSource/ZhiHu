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

package com.jlu.zhihu.controller;

import com.jlu.zhihu.model.Question;
import com.jlu.zhihu.net.Request;
import com.jlu.zhihu.net.Response;
import com.jlu.zhihu.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/question")
public class QuestionController {

    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping("/create")
    public Response<Question> createQuestion(@RequestBody Request<Question> request) {
        /* The person who making this request must be the author of the question. */
        Response<Question> response = new Response<>();
        if (request.user.equals(request.body.author)) {
            response.body = questionService.createQuestion(request.body,
                    request.args.get("html"), request.args.get("raw"));
        } else {
            response.status = HttpStatus.FORBIDDEN;
        }
        return response;
    }

    @PostMapping("/all")
    public Response<List<Question>> all(@RequestBody Request<Integer> request) {
        Response<List<Question>> response = new Response<>();
        Sort sort = new Sort(Sort.Direction.DESC, "st");
        Pageable pageable = PageRequest.of(request.body, questionService.PAGE_SIZE, sort);
        response.body = questionService.findAll(pageable);
        return response;
    }

    @GetMapping("/{id}")
    public Response<Question> findById(@PathVariable int id) {
        Response<Question> response = new Response<>();
        response.body = questionService.findById(id);
        return response;
    }
}
