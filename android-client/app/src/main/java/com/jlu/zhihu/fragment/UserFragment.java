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

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jlu.zhihu.R;

import java.util.Objects;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserFragment extends Fragment implements ScrollToHeadListener {

    private AlertDialog loginDialog;

    private CircularProgressButton loginButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        ButterKnife.bind(this, view);
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        builder.setTitle(R.string.login);
        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_login, (ViewGroup) getView(), false);
        builder.setView(contentView);
        loginDialog = builder.create();
        initLoginDialog(contentView);
        return view;
    }

    private void initLoginDialog(View contentView) {
        loginButton = contentView.findViewById(R.id.ok);
        loginButton.setOnClickListener(v -> loginButton.startAnimation());
    }

    @Override
    public void onScrollToHead() {

    }

    @OnClick(R.id.user_panel)
    public void login() {
        loginDialog.show();
    }
}