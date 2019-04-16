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

import com.alibaba.fastjson.JSONObject;

import javax.persistence.*;

@Entity
@SuppressWarnings("unused")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;

    @Column(nullable = false, length = 32, unique = true)
    public String email;

    @Column(nullable = false, length = 32)
    public String password;

    @Column(nullable = false, length = 18)
    public String name;

    public String avatar = "https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png";

    public String site = "http://47.94.134.55:3000/";

    public String sign = "发现更大的世界";

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        return id == ((User) o).id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
