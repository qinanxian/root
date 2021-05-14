/*
 * Copyright 2019-2029 FISOK(www.fisok.cn).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.fisok.web.interceptor;

import cn.fisok.web.holder.WebHolder;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.web.WebConsts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author : 杨松<yangsong158@qq.com>
 * @date : 2020/1/10
 * @desc : Web资源属性注入拦截器，把Request，Response，Session绑定到线程上
 */
public class WebHolderInterceptor implements HandlerInterceptor {
    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestId = StringKit.uuid();
        HttpSession session = request.getSession(false);

        WebHolder.setRequest(request);
        WebHolder.setResponse(response);
        WebHolder.setSession(session);

        //设置请求一级的全局属性
        request.setAttribute(WebConsts.KEY_REQUEST, requestId);
        MDC.put(WebConsts.KEY_REQUEST,requestId);
        if(session != null){
            request.setAttribute(WebConsts.KEY_SESSION, session.getId());
            MDC.put(WebConsts.KEY_SESSION,session.getId());
        }

        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        WebHolder.clear();
        MDC.clear();
    }
}
