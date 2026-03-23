package com.springfield.gymrat.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.springfield.gymrat.dto.*;
import com.springfield.gymrat.entity.User;
import com.springfield.gymrat.vo.DataOverviewVO;
import com.springfield.gymrat.vo.PageResult;
import com.springfield.gymrat.vo.UserProfileVO;
import com.springfield.gymrat.vo.UserVO;

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

    /**
     * 获取数据概览
     */
    DataOverviewVO getDataOverview();

    /**
     * 分页查询用户列表
     */
    PageResult<UserVO> getUserList(Page<UserVO> page, UserQueryDTO queryDTO);

    /**
     * 更新用户状态（启用/禁用）
     */
    boolean updateUserStatus(Long userId, Integer status);
}
