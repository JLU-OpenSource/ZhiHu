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

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.jlu.zhihu.R;
import com.jlu.zhihu.api.UserApi;
import com.jlu.zhihu.api.service.UserService;
import com.jlu.zhihu.model.User;
import com.jlu.zhihu.util.LogUtil;
import com.jlu.zhihu.util.ToastUtil;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements UserService.LoginCallback {

    private static final String TAG = "LoginActivity";

    private final UserService userService = UserApi.getService();

    @BindView(R.id.name)
    EditText editTextName;

    @BindView(R.id.email)
    EditText editTextEmail;

    @BindView(R.id.password)
    EditText editTextPassword;

    @BindView(R.id.register)
    TextView textViewRegister;

    @BindView(R.id.ok)
    CircularProgressButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        userService.setLoginCallback(this);
        SharedPreferences preferences = getSharedPreferences("zhihu.xml", MODE_PRIVATE);
        String email = preferences.getString("email", "");
        editTextEmail.setText(email);
    }

    @OnClick(R.id.ok)
    public void login() {
        User user = new User();
        user.email = editTextEmail.getText().toString();
        user.password = editTextPassword.getText().toString();
        boolean login = editTextName.getVisibility() == View.GONE;
        if (!login) user.name = editTextName.getText().toString();
        if (TextUtils.isEmpty(user.email) || TextUtils.isEmpty(user.password) || (!login && TextUtils.isEmpty(user.name))) {
            ToastUtil.msg("输入不能位空");
            return;
        } else {
            SharedPreferences preferences = getSharedPreferences("zhihu.xml", MODE_PRIVATE);
            preferences.edit().putString("email", user.email).apply();
        }
        LogUtil.d(TAG, "start login user: " + user.toString());
        userService.go(user, login);
        loginButton.startAnimation();
        textViewRegister.setEnabled(false);
    }

    @OnClick(R.id.register)
    public void switchLogin() {
        boolean login = editTextName.getVisibility() == View.GONE;

        Animation animation = login ? AnimationUtils.makeInAnimation(this, true)
                : AnimationUtils.makeOutAnimation(this, true);
        editTextName.startAnimation(animation);
        editTextName.setVisibility(login ? View.VISIBLE : View.GONE);
        textViewRegister.setText(login ? "返回登陆" : "注册知乎账号");
        loginButton.setText(login ? "注册" : "登陆");
    }

    @Override
    public void loginFinish(boolean success) {
        runOnUiThread(() -> {
            loginButton.revertAnimation();
            textViewRegister.setEnabled(true);
            if (!success)
                ToastUtil.msg("登陆失败，用户名或密码错误");
            else
                startActivity(new Intent(this, MainActivity.class));
        });
    }
}
