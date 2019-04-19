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

package com.jlu.zhihu.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jlu.zhihu.R;
import com.jlu.zhihu.event.Event;
import com.jlu.zhihu.event.EventBus;
import com.jlu.zhihu.model.ListItemModel;
import com.jlu.zhihu.model.Question;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListItemQuestionView extends LinearLayout implements ListItemView {

    @BindView(R.id.title)
    TextView textViewTitle;

    @BindView(R.id.summary)
    TextView textViewSummary;

    @BindView(R.id.author_name)
    TextView textViewAuthorName;

    @BindView(R.id.author_sign)
    TextView textViewAuthorSign;

    @BindView(R.id.info)
    TextView textViewInfo;

    @BindView(R.id.avatar)
    ImageView imageViewAvatar;

    private static final String INFO_FORMATTER = "%d 赞同 · %d 评论 · %d 收藏";

    private Question question;

    public ListItemQuestionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onBind(ListItemModel item) {
        ButterKnife.bind(this);
        question = (Question) item;
        textViewTitle.setText(question.title);
        textViewSummary.setText(question.summary);
        textViewAuthorName.setText(question.author.name);
        textViewAuthorSign.setText(question.author.sign);
        String info = String.format(Locale.CHINA, INFO_FORMATTER, question.agree.size(),
                question.comment.size(), question.collect.size());
        textViewInfo.setText(info);

        Glide.with(getContext())
                .load(question.author.avatar)
                .placeholder(R.drawable.avatar)
                .error(R.drawable.avatar)
                .into(imageViewAvatar);
    }

    @Override
    @OnClick
    public void onItemClick() {
        EventBus.getInstance().sendMessage(Event.Click.ON_QUESTION_CLICK,
                question, "on question click");
    }
}
