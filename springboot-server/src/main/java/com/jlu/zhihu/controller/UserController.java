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
import com.jlu.zhihu.model.User;
import com.jlu.zhihu.net.Request;
import com.jlu.zhihu.net.Response;
import com.jlu.zhihu.security.TokenManager;
import com.jlu.zhihu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final TokenManager tokenManager;

    @Autowired
    public UserController(UserService userService, TokenManager tokenManager) {
        this.userService = userService;
        this.tokenManager = tokenManager;
    }

    @PostMapping("/register")
    public Response<String> register(@RequestBody Request<User> request) {
        Response<String> response = new Response<>();
        if (userService.findUserByEmail(request.body.email) != null) {
            response.status = HttpStatus.FORBIDDEN;
            response.body = "user already exist.";
        } else {
            response.body = tokenManager.generateToken(userService.register(request.body));
        }
        return response;
    }

    @PostMapping("/login")
    public Response<String> login(@RequestBody Request<User> request) {
        Response<String> response = new Response<>();
        User user = userService.login(request.body);
        if (user != null) {
            response.body = tokenManager.generateToken(user);
        } else {
            response.status = HttpStatus.NOT_FOUND;
        }
        return response;
    }

    @PostMapping("/api/email")
    public Response<User> getUser(@RequestBody Request<String> request) {
        Response<User> response = new Response<>();
        response.body = userService.findUserByEmail(request.body);
        if (response.body == null)
            response.status = HttpStatus.NOT_FOUND;
        return response;
    }

    @GetMapping("/question/{id}")
    public Response<List<Question>> getQuestion(@PathVariable int id) {
        Response<List<Question>> response = new Response<>();
        response.body = userService.getQuestion(id);
        return response;
    }

}
