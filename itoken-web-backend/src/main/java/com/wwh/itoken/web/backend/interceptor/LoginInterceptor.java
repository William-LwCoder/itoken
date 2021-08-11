package com.wwh.itoken.web.backend.interceptor;

import com.wwh.itoken.common.utils.CookieUtils;
import com.wwh.itoken.common.web.constants.WebConstants;
import com.wwh.itoken.common.web.interceptor.BaseInterceptorMethods;
import com.wwh.itoken.web.backend.service.RedisService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return BaseInterceptorMethods.preHandleForLogin(request, response, handler, "http://localhost:8603/" + request.getServletPath());
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        String token = CookieUtils.getCookieValue(request, WebConstants.SESSION_TOKEN);
        if (StringUtils.isNotBlank(token)) {
            String loginCode = redisService.get(token);
            if (StringUtils.isNotBlank(loginCode)) {
                BaseInterceptorMethods.postHandleForLogin(request, response, handler, modelAndView, redisService.get(loginCode), "http://localhost:8603/" + request.getServletPath());
            }
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }
}
