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

import com.jlu.zhihu.util.LogUtil;
import com.jlu.zhihu.util.TaskRunner;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.OkHttpClient;

public class HtmlRender {

    private static final String TAG = "HtmlRender";

    private static final String HTML_HEADER = "<head>" +
            "<link rel='stylesheet' href='file:///android_asset/css/bootstrap.min.css'>\n" +
            "<meta name='viewport' content='width=device-width, initial-scale=1.0, user-scalable=no'>\n" +
            "<link rel='stylesheet' href='file:///android_asset/css/output.css'>\n" +
            "<style>img{max-width:100% !important; width:auto; height:auto;}</style>\n" +
            "</head>\n" +
            "<div class='container'>";

    public interface RenderCallback {

        void renderFinish(String html);
    }

    public static void render(String url, RenderCallback callback) {
        OkHttpClient client = new OkHttpClient.Builder().build();
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .get()
                .build();
        Call call = client.newCall(request);

        TaskRunner.execute(() -> {
            try {
                String html = Objects.requireNonNull(call.execute().body()).string();
                callback.renderFinish(HTML_HEADER + html + "</div>");
            } catch (IOException e) {
                LogUtil.e(TAG, "render html exception.", e);
            }
        });
    }
}
