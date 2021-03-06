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

package com.jlu.zhihu.activity;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jlu.zhihu.R;
import com.jlu.zhihu.adapter.CommentAdapter;
import com.jlu.zhihu.api.ArticleApi;
import com.jlu.zhihu.api.UserApi;
import com.jlu.zhihu.api.service.ArticleService;
import com.jlu.zhihu.model.Article;
import com.jlu.zhihu.model.User;
import com.jlu.zhihu.net.HtmlRender;
import com.jlu.zhihu.util.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ArticleActivity extends AppCompatActivity implements
        HtmlRender.RenderCallback, ArticleService.ArticleCallback {

    @BindView(R.id.web_view)
    WebView webView;

    @BindView(R.id.avatar)
    ImageView imageViewAvatar;

    @BindView(R.id.author_name)
    TextView textViewName;

    @BindView(R.id.author_sign)
    TextView textViewAuthorSign;

    @BindView(R.id.like)
    ImageView imageViewLike;

    @BindView(R.id.star)
    ImageView imageViewStar;

    private ArticleService articleService = ArticleApi.getInstance();

    private Handler handler = new Handler(Looper.getMainLooper());

    private boolean agree = false, collect = false;

    private Article article = null;

    private final User user = UserApi.getService().getLoginUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        ButterKnife.bind(this);
        int id = getIntent().getIntExtra("id", 1);
        HtmlRender.render("http://47.94.134.55:8080/article/" + id + ".html", this);
        articleService.getArticle(id);
        articleService.setArticleCallback(this);
    }


    @Override
    public void renderFinish(String html) {
        handler.post(() -> webView.loadDataWithBaseURL("file:///android_asset/", html, "text/html", "UTF-8", null));
    }

    @Override
    public void onGetArticle(Article article) {
        this.article = article;
        handler.post(() -> {
            Glide.with(this)
                    .load(article.author.avatar)
                    .placeholder(R.drawable.avatar)
                    .error(R.drawable.avatar)
                    .into(imageViewAvatar);

            textViewName.setText(article.author.name);
            textViewAuthorSign.setText(article.author.sign);
            agree = article.agree.contains(user);
            collect = article.collect.contains(user);
            initMetaData();
        });
    }

    private void initMetaData() {
        Drawable like = getDrawable(agree ? R.drawable.like_active : R.drawable.like);
        Drawable star = getDrawable(collect ? R.drawable.star_active : R.drawable.star);
        imageViewLike.setImageDrawable(like);
        imageViewStar.setImageDrawable(star);
    }

    @OnClick(R.id.like)
    void agree() {
        if (article == null) return;
        agree = !agree;
        initMetaData();
        ToastUtil.msg(agree ? "已赞同" : "已取消赞同");
        if (agree) article.agree.add(user);
        else article.agree.remove(user);
        articleService.metadata(article);
    }

    @OnClick(R.id.star)
    void star() {
        if (article == null) return;
        collect = !collect;
        initMetaData();
        ToastUtil.msg(collect ? "已收藏" : "已取消收藏");
        if (collect) article.collect.add(user);
        else article.collect.remove(user);
        articleService.metadata(article);
    }

    @SuppressLint("SetTextI18n")
    @OnClick(R.id.comment)
    void comment() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View contentView = getLayoutInflater().inflate(R.layout.dialog_commets, null);
        dialog.setContentView(contentView);
        RecyclerView recyclerView = contentView.findViewById(R.id.list);
        TextView textView = contentView.findViewById(R.id.title);
        textView.setText(article.comment.size() + "条评论");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new CommentAdapter(this, article.comment));
        dialog.show();
    }
}
