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

import com.jlu.zhihu.model.Answer;
import com.jlu.zhihu.model.User;
import com.jlu.zhihu.net.Request;
import com.jlu.zhihu.net.Response;
import com.jlu.zhihu.service.AnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/answer")
public class AnswerController {

    private final AnswerService answerService;

    @Autowired
    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @PostMapping("/create")
    public Response<Answer> createAnswer(@RequestBody Request<Answer> request) {
        /* The person who making this request must be the author of the answer. */
        Response<Answer> response = new Response<>();
        if (request.user.equals(request.body.author)) {
            response.body = answerService.createAnswer(request.body,
                    request.args.get("html"), request.args.get("raw"));
        } else {
            response.status = HttpStatus.FORBIDDEN;
        }
        return response;
    }

    @PostMapping("/all")
    public Response<List<Answer>> all(@RequestBody Request<Void> request) {
        Response<List<Answer>> response = new Response<>();
        Sort sort = new Sort(Sort.Direction.DESC, "st");
        Pageable pageable = PageRequest.of(Integer.valueOf(request.args.get("page"))
                , Integer.valueOf(request.args.get("size")), sort);
        response.body = answerService.all(pageable);
        return response;
    }

    @PostMapping("/metadata")
    public Response<Answer> metaData(@RequestBody Request<Answer> request) {
        Response<Answer> response = new Response<>();
        response.body = answerService.metaData(request.body);
        return response;
    }

    @GetMapping("/{aid}")
    public Response<Answer> getAnswer(@PathVariable int aid) {
        Response<Answer> response = new Response<>();
        response.body = answerService.findById(aid);
        return response;
    }

    @PostMapping("/author")
    public Response<Answer> getAnswerByAuthor(@RequestBody Request<Integer> request) {
        Response<Answer> response = new Response<>();
        response.body = answerService.findByAuthor(request.body, request.user);
        return response;
    }

    @PostMapping("/question")
    public Response<List<Answer>> getAnswersUnderQuestion(@RequestBody Request<Integer> request) {
        Response<List<Answer>> response = new Response<>();
        Sort sort = new Sort(Sort.Direction.DESC, "st");
        Pageable pageable = PageRequest.of(Integer.valueOf(request.args.get("page"))
                , Integer.valueOf(request.args.get("size")), sort);
        response.body = answerService.allUnderQid(request.body, pageable);
        return response;
    }

    @PostMapping("/collect")
    public Response<List<Answer>> getCollect(@RequestBody Request<User> request) {
        Response<List<Answer>> response = new Response<>();
        if (request.user.equals(request.body)) {
            response.body = answerService.findCollect(request.body);
        } else {
            response.status = HttpStatus.FORBIDDEN;
        }
        return response;
    }

    @PostMapping("/removeCollect")
    public Response<Void> removeCollect(@RequestBody Request<Answer> request) {
        Response<Void> response = new Response<>();
        answerService.removeCollect(request.body, request.user);
        return response;
    }

    @GetMapping("/count")
    public long countAll() {
        return answerService.countAll();
    }
}
