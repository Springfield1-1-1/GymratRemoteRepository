package com.springfield.gymrat.config;

import com.springfield.gymrat.common.interceptor.AdminAuthInterceptor;
import com.springfield.gymrat.common.interceptor.JwtInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final JwtInterceptor jwtInterceptor;

    private final AdminAuthInterceptor adminAuthInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/api/**")  // 保护所有 /api 开头的接口
                .excludePathPatterns(
                        "/api/user/login",
                        "/api/user/register",
                        "/api/public/**",
                        "/error"
                );
        registry.addInterceptor(adminAuthInterceptor)
                .addPathPatterns(
                        "/api/admin/**",
                        "/api/user/admin/**"
                );
    }
}
