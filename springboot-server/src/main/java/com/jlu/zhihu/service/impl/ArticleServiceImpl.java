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

import com.jlu.zhihu.model.Article;
import com.jlu.zhihu.model.User;
import com.jlu.zhihu.repository.ArticleRepository;
import com.jlu.zhihu.service.ArticleService;
import com.jlu.zhihu.util.FileUtil;
import com.jlu.zhihu.util.RawUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final Environment environment;

    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository,
                              Environment environment) {
        this.articleRepository = articleRepository;
        this.environment = environment;
    }

    @Override
    public Article findById(int id) {
        return articleRepository.findArticleById(id);
    }

    @Override
    public Article create(Article article, String raw, String html) {
        article.summary = RawUtil.getSummaryByRaw(raw, SUMMARY_LENGTH);
        article = articleRepository.save(article);
        FileUtil.write2File(html, getAnswerPath() + article.id + ".html");
        article.collect = new HashSet<>();
        article.agree = new HashSet<>();
        article.comment = new ArrayList<>();
        return article;
    }

    @Override
    public Article metaData(Article article) {
        Article database = articleRepository.findArticleById(article.id);
        if (database != null) database = articleRepository.save(article);
        else database = article;
        return database;
    }

    @Override
    public List<Article> all(Pageable pageable) {
        return articleRepository.findAll(pageable).getContent();
    }

    @Override
    public List<Article> findCollect(User user) {
        List<Article> all = articleRepository.findAll();
        List<Article> collect = new ArrayList<>();
        for (Article a : all) {
            if (a.collect.contains(user))
                collect.add(a);
        }
        return collect;
    }

    @Override
    public void removeCollect(Article article, User user) {
        article.collect.remove(user);
        articleRepository.save(article);
    }

    @Override
    public long countAll() {
        return articleRepository.count();
    }

    @Override
    public String getAnswerPath() {
        return environment.getProperty("static-resources-path") + "article/";
    }
}
