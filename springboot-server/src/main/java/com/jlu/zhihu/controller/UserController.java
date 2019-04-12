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

import com.jlu.zhihu.model.User;
import com.jlu.zhihu.net.Request;
import com.jlu.zhihu.net.Response;
import com.jlu.zhihu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Response<String> register(@RequestBody Request<User> request) {
        Response<String> response = new Response<>();
        if (userService.findUserByEmail(request.body.email) != null) {
            response.status = HttpStatus.FORBIDDEN;
            response.body = "user already exist.";
        } else {
            userService.register(request.body);
            response.body = "success";
        }
        return response;
    }

    @PostMapping("/login")
    public Response<String> login(@RequestBody Request<User> request) {
        Response<String> response = new Response<>();
        if (userService.login(request.body)) {
            response.body = "login success";
        } else {
            response.status = HttpStatus.NOT_FOUND;
        }
        return response;
    }
}
