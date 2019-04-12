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

import com.alibaba.fastjson.JSONObject;
import com.jlu.zhihu.net.Response;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class TokenInterceptor extends HandlerInterceptorAdapter {

    private final TokenManager tokenManager;

    public TokenInterceptor(TokenManager tokenManager) {
        this.tokenManager = tokenManager;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        boolean result = tokenManager.isTokenActive(request.getHeader("token"));
        if (!result) {
            PrintWriter writer = response.getWriter();
            response.setContentType("application/json; charset=utf-8");
            Response<String> r = new Response<>();
            r.status = HttpStatus.UNAUTHORIZED;
            r.body = "login your account to get a token.";
            writer.print(JSONObject.toJSON(r));
            writer.flush();
            writer.close();
        }
        return result;
    }
}
