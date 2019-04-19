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

package com.jlu.zhihu.api.service;

import com.google.gson.reflect.TypeToken;
import com.jlu.zhihu.model.Question;
import com.jlu.zhihu.model.User;
import com.jlu.zhihu.net.Response;

import java.lang.reflect.Type;
import java.util.List;

public interface BaseService {

    int OK = 200;

    String HOST = "http://192.168.137.159:8080";

    Type TYPE_RESPONSE_STRING = new TypeToken<Response<String>>() {}.getType();

    Type TYPE_RESPONSE_USER = new TypeToken<Response<User>>() {}.getType();

    Type TYPE_RESPONSE_LIST_QUESTION = new TypeToken<Response<List<Question>>>() {}.getType();
}
