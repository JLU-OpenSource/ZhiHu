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

package com.jlu.zhihu.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jlu.zhihu.R;
import com.jlu.zhihu.ZApplication;
import com.jlu.zhihu.api.UserApi;
import com.jlu.zhihu.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;

public class UserFragment extends Fragment implements ScrollToHeadListener {

    @BindView(R.id.avatar)
    ImageView imageViewAvatar;

    @BindView(R.id.name)
    TextView textViewName;

    @BindView(R.id.name_txt)
    TextView textViewNameTxt;

    @BindView(R.id.email_txt)
    TextView textViewEmailTxt;

    private final User user = UserApi.getService().getLoginUser();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        Glide.with(this)
                .load(user.avatar)
                .placeholder(R.drawable.avatar)
                .error(R.drawable.avatar)
                .into(imageViewAvatar);

        textViewName.setText(user.name);
        textViewNameTxt.setText(user.name);
        SharedPreferences preferences = getActivity().getSharedPreferences("zhihu.xml", MODE_PRIVATE);
        String email = preferences.getString("email", "");
        textViewEmailTxt.setText(email);
    }

    @Override
    public void onScrollToHead() {

    }
}