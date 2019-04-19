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
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jlu.zhihu.R;
import com.jlu.zhihu.model.ListItemModel;
import com.jlu.zhihu.model.Question;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListItemQuestionView extends LinearLayout implements ListItemView {

    @BindView(R.id.title)
    TextView textViewTitle;

    @BindView(R.id.summary)
    TextView textViewSummary;

    public ListItemQuestionView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onBind(ListItemModel item) {
        ButterKnife.bind(this);
        Question question = (Question) item;
        textViewTitle.setText(question.title);
        textViewSummary.setText(question.summary);
    }

    @Override
    public void onClick(View v) {

    }
}
