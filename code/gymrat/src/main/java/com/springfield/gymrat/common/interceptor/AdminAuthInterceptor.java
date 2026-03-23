package com.springfield.gymrat.common.interceptor;

import com.springfield.gymrat.common.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminAuthInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("管理员接口访问失败：缺少 Authorization 头");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        String token = authHeader.substring(7);

        // 验证 Token 有效性
        if (!jwtUtil.validateToken(token)) {
            log.warn("管理员接口访问失败：Token 无效或已过期");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        // 检查是否为管理员
        String role = jwtUtil.getRoleFromToken(token);
        if (!"admin".equals(role)) {
            log.warn("管理员接口访问失败：用户无权限 - userId={}, role={}",
                    jwtUtil.getUserIdFromToken(token), role);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":403,\"message\":\"无权访问管理员功能\"}");
            return false;
        }

        log.info("管理员接口访问成功：userId={}, role={}",
                jwtUtil.getUserIdFromToken(token), role);
        return true;
    }
}
