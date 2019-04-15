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

import com.jlu.zhihu.model.Draft;
import com.jlu.zhihu.model.User;
import com.jlu.zhihu.net.Request;
import com.jlu.zhihu.net.Response;
import com.jlu.zhihu.service.DraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/draft")
public class DraftController {

    /* The person who making the request must be the owner of the draft. */

    private final DraftService draftService;

    @Autowired
    public DraftController(DraftService draftService) {
        this.draftService = draftService;
    }

    @PostMapping("/save")
    public Response<Draft> saveDraft(@RequestBody Request<Draft> request) {
        Response<Draft> response = new Response<>();
        if (request.user.equals(request.body.author)) {
            response.body = draftService.saveDraft(request.body,
                    request.args.get("html"), request.args.get("raw"));
        } else {
            response.status = HttpStatus.FORBIDDEN;
        }
        return response;
    }

    @PostMapping("/all")
    public Response<List<Draft>> getDrafts(@RequestBody Request<User> request) {
        Response<List<Draft>> response = new Response<>();
        if (request.body.equals(request.user)) {
            response.body = draftService.findAllByAuthor(request.body);
        } else {
            response.status = HttpStatus.FORBIDDEN;
            response.body = new ArrayList<>(0);
        }
        return response;
    }

    @PostMapping("/remove")
    public Response<Draft> removeDraft(@RequestBody Request<Draft> request) {
        Response<Draft> response = new Response<>();
        if (request.user.equals(request.body.author)) {
            response.body = draftService.removeDraft(request.body);
        } else {
            response.status = HttpStatus.FORBIDDEN;
        }
        return response;
    }
}
