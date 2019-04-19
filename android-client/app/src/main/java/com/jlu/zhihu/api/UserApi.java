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

package com.jlu.zhihu.api;

import com.jlu.zhihu.api.service.UserService;
import com.jlu.zhihu.model.User;
import com.jlu.zhihu.net.OkHttpHelper;
import com.jlu.zhihu.net.Request;
import com.jlu.zhihu.net.Response;
import com.jlu.zhihu.util.TaskRunner;

public class UserApi implements UserService {

    private LoginCallback loginCallback;

    private static UserService instance;

    private String token = "";

    private User userHolder;

    private UserApi() {
    }

    public static UserService getService() {
        if (instance == null) {
            synchronized (UserApi.class) {
                if (instance == null)
                    instance = new UserApi();
            }
        }
        return instance;
    }

    @Override
    public void go(User user, boolean login) {
        TaskRunner.execute(() -> {
            if (generateToken(user, login)) {
                Request<String> request = new Request<>();
                request.body = user.email;
                Response<User> userResponse = OkHttpHelper.post(HOST + PATH_FIND_EMAIL, request, TYPE_RESPONSE_USER);
                if (userResponse.status == 200) {
                    userHolder = userResponse.body;
                    loginCallback.loginFinish(true);
                } else {
                    loginCallback.loginFinish(false);
                }
            } else loginCallback.loginFinish(false);

        });
    }

    private boolean generateToken(User user, boolean login) {
        Request<User> request = new Request<>();
        request.body = user;
        Response<String> response = OkHttpHelper.post(
                login ? PATH_LOGIN : PATH_REGISTER, request, TYPE_RESPONSE_STRING);
        if (response != null && response.status == OK) {
            this.token = response.body;
        }
        return response != null && response.status == OK;
    }

    @Override
    public String getToken() {
        return this.token;
    }

    @Override
    public User getLoginUser() {
        return this.userHolder;
    }

    @Override
    public void setLoginCallback(LoginCallback loginCallback) {
        this.loginCallback = loginCallback;
    }
}
