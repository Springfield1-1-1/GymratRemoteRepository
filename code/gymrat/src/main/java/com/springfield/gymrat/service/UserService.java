package com.springfield.gymrat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.springfield.gymrat.dto.LoginResultDTO;
import com.springfield.gymrat.dto.ProfileUpdateDTO;
import com.springfield.gymrat.dto.RegisterDTO;
import com.springfield.gymrat.dto.LoginDTO;
import com.springfield.gymrat.entity.User;
import com.springfield.gymrat.vo.UserProfileVO;

public interface UserService extends IService<User>{
    /**
     * 用户注册
     */
    Long register(RegisterDTO dto);
    /**
     * 用户登录
     */
    LoginResultDTO login(LoginDTO dto);
    /**
     * 获取用户个人信息
     */
    UserProfileVO getProfile(Long userId);

    /**
     * 更新用户个人信息
     */
    void updateProfile(Long userId, ProfileUpdateDTO dto);

    /**
     * 更新用户名
     */
    void updateUsername(Long userId, String username);

    /**
     * 更新用户头像
     */
    void updateAvatar(Long userId, String avatarUrl);
}
