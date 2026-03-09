package com.springfield.gymrat.controller;

import com.springfield.gymrat.common.Result;
import com.springfield.gymrat.dto.RegisterDTO;
import com.springfield.gymrat.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import com.springfield.gymrat.entity.User;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<Map<String, Object>> register(@Valid @RequestBody RegisterDTO dto) {
        log.info("用户注册请求: username={}, phone={}", dto.getUsername(), dto.getPhone());

        try {
            Long userId = userService.register(dto);

            Map<String, Object> data = new HashMap<>();
            data.put("userId", userId);
            data.put("username", dto.getUsername());
            data.put("phone", dto.getPhone());

            log.info("用户注册成功: userId={}, username={}", userId, dto.getUsername());
            return Result.success("注册成功", data);

        } catch (Exception e) {
            log.error("用户注册失败: username={}, error={}", dto.getUsername(), e.getMessage());
            throw e;
        }
    }

}
