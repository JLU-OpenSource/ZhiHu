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

import android.util.Log;

public class LogUtil {

    private static final String PREFIX = "ZhiHu-";

    private static boolean LOG_ENABLE = true;

    private static String addPreFix(String tag) {
        return PREFIX + tag;
    }

    public static void d(String tag, String msg) {
        if (LOG_ENABLE)
            Log.d(addPreFix(tag), msg);
    }

    public static void d(String tag, String msg, Object... args) {
        if (LOG_ENABLE)
            Log.d(addPreFix(tag), String.format(msg, args));
    }

    public static void e(String tag, String msg, Throwable e) {
        if (LOG_ENABLE)
            Log.e(addPreFix(tag), msg, e);
    }

    public static void e(String tag, String msg, Throwable e, Object... args) {
        if (LOG_ENABLE)
            Log.e(addPreFix(tag), String.format(msg, args), e);
    }

    public static void setLogEnable(boolean logEnable) {
        LOG_ENABLE = logEnable;
    }
}
