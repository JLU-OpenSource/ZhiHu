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

package com.jlu.zhihu.event;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.UiThread;

import java.util.HashSet;
import java.util.Set;

public class EventBus {

    private static EventBus instance;

    private Handler handler;

    private Set<EventHandler> eventHandlers;

    private EventBus() {
        handler = new Handler(Looper.getMainLooper());
        eventHandlers = new HashSet<>();
    }

    public static EventBus getInstance() {
        if (instance == null) {
            synchronized (EventBus.class) {
                if (instance == null)
                    instance = new EventBus();
            }
        }
        return instance;
    }

    public void register(EventHandler eventHandler) {
        eventHandlers.add(eventHandler);
    }

    @UiThread
    public void sendMessage(int what, Object o, String msg) {
        for (EventHandler handler : eventHandlers) {
            if (handler.handleMsg(what, msg, o))
                return;
        }
        throw new IllegalArgumentException("no handler found for msg: " + msg);
    }

    public void onMainThread(Runnable runnable) {
        handler.post(runnable);
    }
}
