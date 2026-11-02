package com.example.inspection.config;

import com.example.inspection.common.Result;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 登录拦截器
 * 1. 未登录访问 /api/** 返回 401 JSON
 * 2. 未登录访问页面重定向到登录页
 */
@Component
public class LoginInterceptor implements HandlerInterceptor {

    /** session 中保存登录用户的键 */
    public static final String SESSION_USER = "loginUser";

    private final ObjectMapper objectMapper;

    public LoginInterceptor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute(SESSION_USER) != null) {
            return true;
        }

        // AJAX / 接口请求：返回 401
        if (request.getRequestURI().startsWith(request.getContextPath() + "/api/")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            Result<Void> body = Result.error(401, "未登录或登录已过期");
            response.getWriter().write(objectMapper.writeValueAsString(body));
            return false;
        }

        // 页面请求：重定向到登录页
        response.sendRedirect(request.getContextPath() + "/login");
        return false;
    }
}
