package com.springfield.gymrat.common.context;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class UserContext {

    /**
     * 获取当前用户ID
     */
    public static Long getCurrentUserId() {
        ServletRequestAttributes attributes = (ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            Object userId = attributes.getRequest().getAttribute("userId");
            if (userId != null) {
                return (Long) userId;
            }
        }
        return null;
    }

    /**
     * 获取当前用户名
     */
    public static String getCurrentUsername() {
        ServletRequestAttributes attributes = (ServletRequestAttributes)
                RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            Object username = attributes.getRequest().getAttribute("username");
            if (username != null) {
                return (String) username;
            }
        }
        return null;
    }

    /**
     * 检查用户是否已登录
     */
    public static boolean isLoggedIn() {
        return getCurrentUserId() != null;
    }
}
