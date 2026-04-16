package com.springfield.gymrat.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springfield.gymrat.common.Result;
import com.springfield.gymrat.common.context.UserContext;
import com.springfield.gymrat.config.OssConfig;
import com.springfield.gymrat.dto.RegisterDTO;
import com.springfield.gymrat.dto.ProfileUpdateDTO;
import com.springfield.gymrat.dto.UserQueryDTO;
import com.springfield.gymrat.dto.UsernameUpdateDTO;
import com.springfield.gymrat.service.UserService;
import com.springfield.gymrat.vo.PageResult;
import com.springfield.gymrat.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import com.springfield.gymrat.entity.User;
import com.springfield.gymrat.vo.UserProfileVO;


@Slf4j
@Validated
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final OssConfig ossConfig;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<Map<String, Object>> register(@Valid @RequestBody RegisterDTO dto) {
        log.info("用户注册请求：username={}, phone={}", dto.getUsername(), dto.getPhone());

        try {
            Long userId = userService.register(dto);

            Map<String, Object> data = new HashMap<>();
            data.put("userId", userId);
            data.put("username", dto.getUsername());
            data.put("phone", dto.getPhone());

            log.info("用户注册成功：userId={}, username={}", userId, dto.getUsername());
            return Result.success("注册成功", data);

        } catch (Exception e) {
            log.error("用户注册失败：username={}, error={}", dto.getUsername(), e.getMessage());
            throw e;
        }
    }

    /**
     * 获取当前用户个人信息
     */
    @GetMapping("/profile")
    public Result<UserProfileVO> getProfile() {
        Long userId = UserContext.getCurrentUserId();
        if (userId == null) {
            return Result.error("未登录");
        }

        UserProfileVO profile = userService.getProfile(userId);
        return Result.success(profile);
    }

    /**
     * 更新用户个人信息
     */
    @PutMapping("/profile")
    public Result<Void> updateProfile(@RequestBody @Valid ProfileUpdateDTO dto) {
        Long userId = UserContext.getCurrentUserId();
        if (userId == null) {
            return Result.error("未登录");
        }

        userService.updateProfile(userId, dto);
        return Result.success();
    }

    /**
     * 更新用户名
     */
    @PutMapping("/username")
    public Result<Void> updateUsername(@RequestBody @Validated UsernameUpdateDTO dto) {
        Long userId = UserContext.getCurrentUserId();
        if (userId == null) {
            return Result.error("未登录");
        }

        userService.updateUsername(userId, dto.getUsername());
        return Result.success();
    }

    /**
     * 上传头像
     */
    //TODO: 抽取服务层业务解耦
    @PostMapping("/avatar")
    public Result<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        Long userId = UserContext.getCurrentUserId();
        if (userId == null) {
            return Result.error("未登录");
        }

        if (file.isEmpty()) {
            return Result.error("请选择要上传的文件");
        }

        try {
            String originalFilename = file.getOriginalFilename();
            String fileExtension = originalFilename != null && originalFilename.contains(".")
                    ? originalFilename.substring(originalFilename.lastIndexOf("."))
                    : ".jpg";

            String fileName = System.currentTimeMillis() + "_" + userId + fileExtension;
            String filePath = "用户头像/" + fileName;

            com.aliyun.oss.ClientBuilderConfiguration conf = new com.aliyun.oss.ClientBuilderConfiguration();
            conf.setMaxConnections(100);

            com.aliyun.oss.OSS ossClient = new com.aliyun.oss.OSSClientBuilder().build(
                    ossConfig.getEndpoint(),
                    ossConfig.getAccessKeyId(),
                    ossConfig.getAccessKeySecret(),
                    conf);

            try {
                ossClient.putObject(ossConfig.getBucketName(), filePath, file.getInputStream());

                String url = "https://" + ossConfig.getBucketName() + "." + ossConfig.getEndpoint() + "/" + filePath;

                log.info("上传成功，URL: {}", url);

                userService.updateAvatar(userId, url);

                return Result.success(url);
            } finally {
                ossClient.shutdown();
            }
        } catch (Exception e) {
            log.error("上传头像失败", e);
            return Result.error("上传失败：" + e.getMessage());
        }
    }
    /**
     * 管理员：分页查询用户列表
     */
    @GetMapping("/admin/list")
    public Result<PageResult<UserVO>> getUserList(
            Page<UserVO> page,
            UserQueryDTO queryDTO) {
        PageResult<UserVO> result = userService.getUserList(page, queryDTO);
        return Result.success(result);
    }

    /**
     * 管理员：更新用户状态（启用/禁用）
     */
    @PutMapping("/admin/status/{userId}")
    public Result<Boolean> updateUserStatus(
            @PathVariable Long userId,
            @RequestParam Integer status) {
        boolean success = userService.updateUserStatus(userId, status);
        if (success) {
            return Result.success();
        } else {
            return Result.error("用户状态更新失败");
        }
    }
}
