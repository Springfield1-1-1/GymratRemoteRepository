package com.springfield.gymrat.controller;

import com.springfield.gymrat.common.Result;
import com.springfield.gymrat.dto.LoginDTO;
import com.springfield.gymrat.dto.LoginResultDTO;
import com.springfield.gymrat.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor    //使用构造器注入比@Autowired好，不容易出现依赖循环&Spring自动进行依赖注入
@Validated
public class AuthController {

    private final UserService userService;

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<LoginResultDTO> login(@Valid @RequestBody LoginDTO dto) {
        LoginResultDTO result = userService.login(dto);
        return Result.success("登录成功", result);
    }

    /**
     * 用户登出（前端清除 token 即可）
     */
    @PostMapping("/logout")
    public Result<?> logout(HttpServletRequest request) {
        // 从请求属性中获取用户ID（拦截器已设置）
        Long userId = (Long) request.getAttribute("userId");
        String username = (String) request.getAttribute("username");

        if (userId != null) {
            log.info("用户登出 - 用户ID: {}, 用户名: {}", userId, username);
        } else {
            log.warn("用户登出 - 未获取到用户信息，可能未登录或token无效");
        }
        return Result.success("已登出", null);
    }
}
