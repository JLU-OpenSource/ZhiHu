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

package com.jlu.zhihu.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class RawUtil {

    private static final String KEY_BLOCK = "blocks";

    private static final String KEY_TEXT = "text";

    public static String getSummaryByRaw(String raw, int length) {
        if (length <= 0) throw new IllegalArgumentException("length can not less than 0!");
        JSONObject rawJson = JSONObject.parseObject(raw);
        JSONArray blocks = rawJson.getJSONArray(KEY_BLOCK);
        StringBuilder builder = new StringBuilder();
        for (Object block : blocks) {
            JSONObject object = (JSONObject) block;
            builder.append(object.getString(KEY_TEXT)).append(" ");
            if (builder.length() > length) break;
        }
        String result = builder.toString();
        return builder.length() < length ? result : result.substring(0, length);
    }
}
