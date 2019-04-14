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

package com.jlu.zhihu.service.impl;

import com.jlu.zhihu.model.Draft;
import com.jlu.zhihu.model.User;
import com.jlu.zhihu.repository.DraftRepository;
import com.jlu.zhihu.service.DraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DraftServiceImpl implements DraftService {

    private final DraftRepository draftRepository;

    @Autowired
    public DraftServiceImpl(DraftRepository draftRepository) {
        this.draftRepository = draftRepository;
    }

    @Override
    public List<Draft> findAllByAuthor(User author) {
        return draftRepository.findByAuthor(author);
    }

    @Override
    public Draft saveDraft(Draft draft, String html,String raw) {
        System.out.println(html);
        System.out.println(raw);
        return draftRepository.save(draft);
    }

    @Override
    public Draft removeDraft(Draft draft) {
        draftRepository.delete(draft);
        return draft;
    }
}
