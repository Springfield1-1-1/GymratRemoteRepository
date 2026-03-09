package com.springfield.gymrat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springfield.gymrat.common.exception.BusinessException;
import com.springfield.gymrat.dto.LoginDTO;
import com.springfield.gymrat.dto.LoginResultDTO;
import com.springfield.gymrat.dto.RegisterDTO;
import com.springfield.gymrat.entity.User;
import com.springfield.gymrat.mapper.UserMapper;
import com.springfield.gymrat.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.springfield.gymrat.common.jwt.JwtUtil;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long register(RegisterDTO dto) {
        // 1. 验证用户名和手机号是否已存在
        if (userMapper.countByUsername(dto.getUsername()) > 0) {
            throw new BusinessException(2001, "用户名已存在");
        }
        if (userMapper.countByPhone(dto.getPhone()) > 0) {
            throw new BusinessException(2001, "手机号已注册");
        }

        // 2. 密码加密
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        // 3. 创建用户
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPhone(dto.getPhone());
        user.setPasswordHash(encodedPassword);
        user.setStatus(1); // 启用状态
        user.setIsVerified(false);

        // 4. 保存用户
        userMapper.insert(user);
        log.info("用户注册成功: {}, ID: {}", dto.getUsername(), user.getId());

        return user.getId();
    }

    @Override
    public LoginResultDTO login(LoginDTO dto) {
        log.info("用户登录请求: username={}", dto.getUsername());

        // 1. 根据用户名查询用户
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>()
                        .eq(User::getUsername, dto.getUsername())
        );

        if (user == null) {
            log.warn("登录失败: 用户不存在 - {}", dto.getUsername());
            throw new BusinessException(3001, "用户名或密码错误");
        }

        // 2. 检查用户状态
        if (user.getStatus() != null && user.getStatus() == 0) {
            log.warn("登录失败: 用户已被禁用 - {}", dto.getUsername());
            throw new BusinessException(3002, "账号已被禁用");
        }

        // 3. 验证密码
        if (!passwordEncoder.matches(dto.getPassword(), user.getPasswordHash())) {
            log.warn("登录失败: 密码错误 - {}", dto.getUsername());
            throw new BusinessException(3001, "用户名或密码错误");
        }

        // 4. 生成 JWT 令牌
        String token = jwtUtil.generateToken(
                user.getId(),
                user.getUsername(),
                Boolean.TRUE.equals(dto.getRememberMe())
        );

        // 5. 更新最后登录时间（可选）
        user.setUpdatedAt(LocalDateTime.now());
        userMapper.updateById(user);

        // 6. 构建返回结果
        LoginResultDTO result = new LoginResultDTO();
        result.setUserId(user.getId());
        result.setUsername(user.getUsername());
        result.setToken(token);
        result.setAvatarUrl(user.getAvatarUrl());

        // 计算过期时间（秒）
        if (Boolean.TRUE.equals(dto.getRememberMe())) {
            result.setExpiresIn(30L * 24 * 60 * 60);  // 30天
        } else {
            result.setExpiresIn(24L * 60 * 60);  // 1天
        }

        log.info("用户登录成功: username={}, userId={}", user.getUsername(), user.getId());
        return result;
    }
}
