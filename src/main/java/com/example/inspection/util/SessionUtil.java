package com.example.inspection.util;

import com.example.inspection.config.LoginInterceptor;
import com.example.inspection.entity.User;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录会话工具类
 */
public final class SessionUtil {

    private SessionUtil() {}

    public static User getLoginUser() {
        HttpServletRequest req = request();
        if (req == null) return null;
        Object u = req.getSession(false).getAttribute(LoginInterceptor.SESSION_USER);
        return u == null ? null : (User) u;
    }

    public static Long getLoginUserId() {
        User u = getLoginUser();
        return u == null ? null : u.getId();
    }

    public static HttpServletRequest request() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attr == null ? null : attr.getRequest();
    }
}
