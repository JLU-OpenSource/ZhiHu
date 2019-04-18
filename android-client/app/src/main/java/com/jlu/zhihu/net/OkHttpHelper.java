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

package com.jlu.zhihu.net;

import com.google.gson.Gson;
import com.jlu.zhihu.api.UserApi;
import com.jlu.zhihu.api.service.UserService;
import com.jlu.zhihu.util.LogUtil;

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

@SuppressWarnings("all")
public class OkHttpHelper {

    private static final String TAG = "OkHttpHelper";

    private static final int TIMEOUT = 5000;

    public static final MediaType MEDIATYPE_JSON
            = MediaType.parse("application/json; charset=utf-8");

    private static final UserService USER_SERVICE = UserApi.getService();

    public static <Body> Response<Body> get(String url, Type type) {
        OkHttpClient okHttpClient = client();
        final okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .get()
                .build();

        return go(okHttpClient, request, type);
    }

    public static <ResponseBody, ReqeuesBody> Response<ResponseBody> post(String url, Request<ReqeuesBody> data, Type type) {
        data.user = USER_SERVICE.getLoginUser();
        String json = new Gson().toJson(data);
        RequestBody body = RequestBody.create(MEDIATYPE_JSON, json);
        OkHttpClient okHttpClient = client();
        final okhttp3.Request request = new okhttp3.Request.Builder()
                .addHeader("token", USER_SERVICE.getToken())
                .url(url)
                .post(body)
                .build();
        return go(okHttpClient, request, type);
    }

    private static <Body> Response<Body> go(OkHttpClient okHttpClient, okhttp3.Request request, Type type) {
        try {
            Call call = okHttpClient.newCall(request);
            String result = call.execute().body().string();
            LogUtil.d(TAG, "url: " + request.url() + " response: " + result);
            return new Gson().fromJson(result, type);
        } catch (Exception e) {
            LogUtil.e(TAG, "OkHttp exception, method: %s, url: %s", e, request.method(), request.url());
        }
        return null;
    }

    private static OkHttpClient client() {
        return new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                .build();
    }
}
