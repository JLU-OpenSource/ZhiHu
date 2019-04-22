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


public class Event {

    public static class Click {
        /* Answer Fragment */
        public static final int ON_ANSWER_CLICK = 0x11000001;

        /* Question Fragment */
        public static final int ON_QUESTION_CLICK = 0x21000001;

        /* Article Fragment */
        public static final int ON_ARTICLE_CLICK = 0x31000001;
    }
}
