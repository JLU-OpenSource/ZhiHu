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

package com.jlu.zhihu.model;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.jlu.zhihu.R;


import java.util.List;
import java.util.Set;

public class Question implements ListItemModel {

    public int id;

    public String title;

    public User author;

    public String summary;

    public Set<User> agree;

    public Set<User> collect;

    public List<Comment> comment;

    public long st = System.currentTimeMillis();

    @Override
    public int getLayoutId() {
        return R.layout.list_item_question;
    }

    @NonNull
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
