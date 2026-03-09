package com.springfield.gymrat.controller;

import com.springfield.gymrat.common.Result;
import com.springfield.gymrat.dto.LoginDTO;
import com.springfield.gymrat.dto.LoginResultDTO;
import com.springfield.gymrat.service.UserService;
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
@RequiredArgsConstructor
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
    public Result<?> logout() {
        // JWT 是无状态的，前端清除 token 即可
        return Result.success("登出成功", null);
    }
}
