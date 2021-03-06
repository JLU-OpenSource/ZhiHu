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

import com.alibaba.fastjson.JSONObject;
import com.jlu.zhihu.util.TextUtil;
import org.springframework.http.HttpStatus;

import java.text.DateFormat;
import java.util.Date;

@SuppressWarnings("unused")
public class Response<Body> {

    public HttpStatus status = HttpStatus.OK;

    public Body body;

    public String msg;

    public String time = DateFormat.getDateTimeInstance().format(new Date());

    public String getMsg() {
        return TextUtil.isEmpty(msg) ? status.getReasonPhrase() : msg;
    }

    public int getStatus() {
        return status.value();
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
