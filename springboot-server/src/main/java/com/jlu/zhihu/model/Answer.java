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

package com.jlu.zhihu.model;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int aid;

    /* question id */
    public int qid;

    @OneToOne
    public User author;

    public String title;

    public String summary;

    @ManyToMany(fetch = FetchType.EAGER)
    public Set<User> agree;

    @ManyToMany(fetch = FetchType.EAGER)
    public Set<User> collect;

    @OneToMany(fetch = FetchType.EAGER)
    public List<Comment> comment;

    public long st = System.currentTimeMillis();
}
