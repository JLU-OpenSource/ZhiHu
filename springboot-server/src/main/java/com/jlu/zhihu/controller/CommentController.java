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
import com.jlu.zhihu.model.Article;
import com.jlu.zhihu.model.Comment;
import com.jlu.zhihu.net.Request;
import com.jlu.zhihu.net.Response;
import com.jlu.zhihu.repository.AnswerRepository;
import com.jlu.zhihu.repository.ArticleRepository;
import com.jlu.zhihu.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    private final CommentRepository commentRepository;
    private final AnswerRepository answerRepository;
    private final ArticleRepository articleRepository;

    @Autowired
    public CommentController(CommentRepository commentRepository,
                             AnswerRepository answerRepository,
                             ArticleRepository articleRepository) {
        this.commentRepository = commentRepository;
        this.answerRepository = answerRepository;
        this.articleRepository = articleRepository;
    }

    @PostMapping("/create")
    public Response<Comment> createComment(@RequestBody Request<Comment> request) {
        /* The person who making this request must be the author of the comment. */
        Response<Comment> response = new Response<>();

        if (request.user.equals(request.body.author)) {
            int id = Integer.parseInt(request.args.get("id"));
            Comment comment = commentRepository.save(request.body);
            if ("answer".equals(request.args.get("type"))) {
                Answer answer = answerRepository.findAnswerByAid(id);
                answer.comment.add(comment);
                answerRepository.save(answer);
                response.body = comment;
            } else {
                Article article = articleRepository.findArticleById(id);
                article.comment.add(comment);
                articleRepository.save(article);
                response.body = comment;
            }
        } else {
            response.status = HttpStatus.FORBIDDEN;
        }
        return response;
    }

    @GetMapping("/answer/{id}")
    public Response<List<Comment>> getAnswerComment(@PathVariable int id) {
        Response<List<Comment>> response = new Response<>();
        response.body = answerRepository.findAnswerByAid(id).comment;
        return response;
    }

    @GetMapping("/article/{id}")
    public Response<List<Comment>> getArticleComment(@PathVariable int id) {
        Response<List<Comment>> response = new Response<>();
        response.body = articleRepository.findArticleById(id).comment;
        return response;
    }
}
