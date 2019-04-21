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

package com.jlu.zhihu.security;

import com.jlu.zhihu.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class TokenManager {

    private final Logger logger = LoggerFactory.getLogger(TokenManager.class);

    private static final long INTERVAL = 15 * 60 * 60 * 1000;

    private final Map<String, Long> tokenPool = new HashMap<>();

    public String generateToken(User user) {
        if (user == null) return null;
        logger.debug("generate a token for user: " + user.name);
        cleanToken();
        String res = user.email + '-' + UUID.randomUUID().toString().replaceAll("-", "")
                + '-' + System.currentTimeMillis();
        String token = Base64.getEncoder().encodeToString(res.getBytes());
        tokenPool.put(token, System.currentTimeMillis());
        return token;
    }

    boolean isTokenActive(String token) {
        cleanToken();
        if (tokenPool.containsKey(token)) {
            tokenPool.put(token, System.currentTimeMillis());
            return true;
        }
        return false;
    }

    private void cleanToken() {
        List<String> expiredTokens = new ArrayList<>();
        for (String token : tokenPool.keySet()) {
            if (System.currentTimeMillis() - tokenPool.get(token) > INTERVAL)
                expiredTokens.add(token);
        }
        for (String token : expiredTokens) {
            tokenPool.remove(token);
        }
    }
}
