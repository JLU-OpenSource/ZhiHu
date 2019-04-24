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

package com.jlu.zhihu.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jlu.zhihu.R;
import com.jlu.zhihu.activity.AnswerActivity;
import com.jlu.zhihu.model.Answer;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.ViewHolder> {

    private final List<Answer> answers;
    private final Context context;

    public AnswerAdapter(Context context, List<Answer> answers) {
        this.answers = answers;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_comment, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Answer answer = answers.get(i);
        Glide.with(context)
                .load(answer.author.avatar)
                .placeholder(R.drawable.avatar)
                .error(R.drawable.avatar)
                .into(viewHolder.avatar);
        viewHolder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AnswerActivity.class);
            intent.putExtra("id", answer.aid);
            context.startActivity(intent);
        });
        viewHolder.content.setText(answer.summary);
        viewHolder.name.setText(answer.author.name);
        viewHolder.time.setText(DateFormat.getDateTimeInstance().format(new Date(answer.st)));
    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.avatar)
        ImageView avatar;

        @BindView(R.id.name)
        TextView name;

        @BindView(R.id.time)
        TextView time;

        @BindView(R.id.content)
        TextView content;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
