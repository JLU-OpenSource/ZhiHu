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

package com.jlu.zhihu.service;

import com.jlu.zhihu.model.Draft;
import com.jlu.zhihu.model.User;

import java.util.List;

public interface DraftService {

    String DRAFT_PATH = "/Users/chengjie/github/ZhiHu/draft/";

    int SUMMARY_LENGTH = 32;

    List<Draft> findAllByAuthor(User author);

    Draft saveDraft(Draft draft, String html, String raw);

    Draft removeDraft(Draft draft);
}
