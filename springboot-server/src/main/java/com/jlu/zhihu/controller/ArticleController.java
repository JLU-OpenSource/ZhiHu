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

import com.jlu.zhihu.model.Article;
import com.jlu.zhihu.model.User;
import com.jlu.zhihu.net.Request;
import com.jlu.zhihu.net.Response;
import com.jlu.zhihu.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/article")
public class ArticleController {

    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping("/create")
    public Response<Article> create(@RequestBody Request<Article> request) {
        /* The person who making this request must be the author of the article. */
        Response<Article> response = new Response<>();
        if (request.user.equals(request.body.author)) {
            response.body = articleService.create(request.body,
                    request.args.get("raw"), request.args.get("html"));
        } else {
            response.status = HttpStatus.FORBIDDEN;
        }
        return response;
    }

    @PostMapping("/all")
    public Response<List<Article>> all(@RequestBody Request<Void> request) {
        Response<List<Article>> response = new Response<>();
        Sort sort = new Sort(Sort.Direction.DESC, "st");
        Pageable pageable = PageRequest.of(Integer.valueOf(request.args.get("page"))
                , Integer.valueOf(request.args.get("size")), sort);
        response.body = articleService.all(pageable);
        return response;
    }

    @PostMapping("/metadata")
    public Response<Article> metaData(@RequestBody Request<Article> request) {
        Response<Article> response = new Response<>();
        response.body = articleService.metaData(request.body);
        return response;
    }

    @GetMapping("/{aid}")
    public Response<Article> getAnswer(@PathVariable int aid) {
        Response<Article> response = new Response<>();
        response.body = articleService.findById(aid);
        return response;
    }

    @PostMapping("/collect")
    public Response<List<Article>> getCollect(@RequestBody Request<User> request) {
        Response<List<Article>> response = new Response<>();
        if (request.user.equals(request.body)) {
            response.body = articleService.findCollect(request.body);
        } else {
            response.status = HttpStatus.FORBIDDEN;
        }
        return response;
    }

    @PostMapping("/removeCollect")
    public Response<Void> removeCollect(@RequestBody Request<Article> request) {
        Response<Void> response = new Response<>();
        articleService.removeCollect(request.body, request.user);
        return response;
    }

    @GetMapping("/count")
    public long countAll() {
        return articleService.countAll();
    }
}
