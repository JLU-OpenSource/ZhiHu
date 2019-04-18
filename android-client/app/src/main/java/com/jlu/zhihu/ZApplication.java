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

package com.jlu.zhihu;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.jlu.zhihu.util.LogUtil;

import java.lang.ref.SoftReference;

@SuppressWarnings("unused")
public class ZApplication extends Application {

    private static SoftReference<Context> contextHolder;

    private static SoftReference<Activity> currentActivity;

    private static final String TAG = "ZApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        contextHolder = new SoftReference<>(getApplicationContext());
        registerActivityLifecycleCallbacks(callbacks);
    }

    private ActivityLifecycleCallbacks callbacks = new ActivityLifecycleCallbacks() {

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            LogUtil.d(TAG, "activity onCreate: " + activity.getClass().getSimpleName());
        }

        @Override
        public void onActivityStarted(Activity activity) {
            LogUtil.d(TAG, "activity onStart: " + activity.getClass().getSimpleName());
        }

        @Override
        public void onActivityResumed(Activity activity) {
            LogUtil.d(TAG, "activity onResume: " + activity.getClass().getSimpleName());
        }

        @Override
        public void onActivityPaused(Activity activity) {
            LogUtil.d(TAG, "activity onPause: " + activity.getClass().getSimpleName());
        }

        @Override
        public void onActivityStopped(Activity activity) {
            LogUtil.d(TAG, "activity onStop: " + activity.getClass().getSimpleName());
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            LogUtil.d(TAG, "activity onSaveInstanceState: " + activity.getClass().getSimpleName());
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            LogUtil.d(TAG, "activity onDestroyed: " + activity.getClass().getSimpleName());
        }
    };

    public static Activity getCurrentActivity() {
        return currentActivity.get();
    }

    public static Context getContextHolder() {
        return contextHolder.get();
    }

    @Override
    protected void finalize() throws Throwable {
        Log.d(TAG, "Application exit");
        super.finalize();
    }

}
