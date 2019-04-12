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

package com.jlu.zhihu.config;

import com.jlu.zhihu.security.TokenInterceptor;
import com.jlu.zhihu.security.TokenManager;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class TokenWebMvcConfig implements WebMvcConfigurer {

    private final TokenManager tokenManager;

    public TokenWebMvcConfig(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    private final List<String> PATH_PATTERNS = new ArrayList<String>() {{
        add("/api/*");
    }};

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenInterceptor(tokenManager))
                .addPathPatterns(PATH_PATTERNS);
    }

}
