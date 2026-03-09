package com.springfield.gymrat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springfield.gymrat.common.exception.BusinessException;
import com.springfield.gymrat.dto.RegisterDTO;
import com.springfield.gymrat.entity.User;
import com.springfield.gymrat.mapper.UserMapper;
import com.springfield.gymrat.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;
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
}
