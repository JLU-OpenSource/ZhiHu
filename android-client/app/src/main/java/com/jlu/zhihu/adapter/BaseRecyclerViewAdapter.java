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
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jlu.zhihu.view.ListItemModel;
import com.jlu.zhihu.view.ListItemView;

import java.util.List;


public class BaseRecyclerViewAdapter extends RecyclerView.Adapter<BaseRecyclerViewAdapter.ViewHolder> {

    private List<ListItemModel> items;

    private LayoutInflater inflater;

    public BaseRecyclerViewAdapter(Context context, List<ListItemModel> items) {
        this.items = items;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int type) {
        View view = inflater.inflate(type, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.contentView.onBind(items.get(i));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ListItemView contentView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            contentView = (ListItemView) itemView;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getLayoutId();
    }
}
