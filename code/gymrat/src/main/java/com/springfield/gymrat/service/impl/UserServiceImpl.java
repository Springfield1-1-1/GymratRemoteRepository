package com.springfield.gymrat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springfield.gymrat.common.exception.BusinessException;
import com.springfield.gymrat.dto.*;
import com.springfield.gymrat.entity.User;
import com.springfield.gymrat.entity.UserProfile;
import com.springfield.gymrat.mapper.UserMapper;
import com.springfield.gymrat.service.UserService;
import com.springfield.gymrat.mapper.UserProfileMapper;
import com.springfield.gymrat.vo.DataOverviewVO;
import com.springfield.gymrat.vo.PageResult;
import com.springfield.gymrat.vo.UserProfileVO;
import com.springfield.gymrat.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.springfield.gymrat.common.jwt.JwtUtil;
import com.springfield.gymrat.common.exception.ErrorCode;
import com.springfield.gymrat.mapper.EquipmentMapper;
import com.springfield.gymrat.mapper.GymStoreMapper;
import com.springfield.gymrat.mapper.CoachMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final UserProfileMapper userProfileMapper;
    private final EquipmentMapper equipmentMapper;
    private final CoachMapper coachMapper;
    private final GymStoreMapper gymStoreMapper;

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

        // 5. 更新最后登录时间
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

        // 7. 设置用户角色
        String role = "admin".equals(user.getUsername()) ? "admin" : "user";
        result.setRole(role);

        log.info("用户登录成功: username={}, userId={}, role={}", user.getUsername(), user.getId(), role);
        return result;
    }

    public UserProfileVO getProfile(Long userId) {
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_EXIST.getMessage());
        }

        UserProfile profile = userProfileMapper.selectById(userId);

        UserProfileVO vo = new UserProfileVO();
        vo.setUserId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setAvatarUrl(user.getAvatarUrl());

        if (profile != null) {
            vo.setGender(profile.getGender());
            vo.setBirthday(profile.getBirthday());
            vo.setHeight(profile.getHeight());
            vo.setWeight(profile.getWeight());
            vo.setFitnessGoal(profile.getFitnessGoal());
            vo.setBio(profile.getBio());
        }

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProfile(Long userId, ProfileUpdateDTO dto) {
        UserProfile profile = userProfileMapper.selectById(userId);

        if (profile == null) {
            profile = new UserProfile();
            profile.setUserId(userId);
            if (dto.getGender() != null) {
                profile.setGender(dto.getGender());
            }
            if (dto.getBirthday() != null) {
                profile.setBirthday(dto.getBirthday());
            }
            if (dto.getHeight() != null) {
                profile.setHeight(dto.getHeight());
            }
            if (dto.getWeight() != null) {
                profile.setWeight(dto.getWeight());
            }
            if (dto.getFitnessGoal() != null) {
                profile.setFitnessGoal(dto.getFitnessGoal());
            }
            if (dto.getBio() != null) {
                profile.setBio(dto.getBio());
            }
            userProfileMapper.insert(profile);
        } else {
            if (dto.getGender() != null) {
                profile.setGender(dto.getGender());
            }
            if (dto.getBirthday() != null) {
                profile.setBirthday(dto.getBirthday());
            }
            if (dto.getHeight() != null) {
                profile.setHeight(dto.getHeight());
            }
            if (dto.getWeight() != null) {
                profile.setWeight(dto.getWeight());
            }
            if (dto.getFitnessGoal() != null) {
                profile.setFitnessGoal(dto.getFitnessGoal());
            }
            if (dto.getBio() != null) {
                profile.setBio(dto.getBio());
            }
            userProfileMapper.updateById(profile);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUsername(Long userId, String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        wrapper.ne(User::getId, userId);

        Long count = baseMapper.selectCount(wrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.USER_EXIST.getMessage());
        }

        User user = getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_EXIST.getMessage());
        }

        user.setUsername(username);
        baseMapper.updateById(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAvatar(Long userId, String avatarUrl) {
        User user = getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.USER_NOT_EXIST.getMessage());
        }

        user.setAvatarUrl(avatarUrl);
        baseMapper.updateById(user);
    }

    @Override
    public DataOverviewVO getDataOverview() {
        // 统计总用户数
        Long totalUsers = baseMapper.selectCount(null);

        // 统计器械数
        Long totalEquipment = equipmentMapper.selectCount(null);

        // 统计门店数
        Long totalStores = gymStoreMapper.selectCount(null);

        // 统计教练数
        Long totalCoaches = coachMapper.selectCount(null);

        DataOverviewVO vo = new DataOverviewVO();
        vo.setTotalUsers(totalUsers);
        vo.setTotalEquipment(totalEquipment);
        vo.setTotalStores(totalStores);
        vo.setTotalCoaches(totalCoaches);

        return vo;
    }

    @Override
    public PageResult<UserVO> getUserList(Page<UserVO> page, UserQueryDTO queryDTO) {
        // 构建查询条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        // 模糊搜索：通过id/username/phone查询
        if (queryDTO != null) {
            if (queryDTO.getKeyword() != null && !queryDTO.getKeyword().trim().isEmpty()) {
                queryWrapper.and(wrapper -> wrapper
                        .like("username", queryDTO.getKeyword())
                        .or()
                        .like("phone", queryDTO.getKeyword())
                        .or()
                        .eq("id", queryDTO.getKeyword())  // 直接匹配ID
                );
            }

            if (queryDTO.getStatus() != null) {
                queryWrapper.eq("status", queryDTO.getStatus());
            }
        }

        // 排除已删除的用户
        queryWrapper.ne("status", -1);

        queryWrapper.orderByDesc("created_at");

        // 执行分页查询
        Page<User> userPage = this.baseMapper.selectPage(
                new Page<>(page.getCurrent(), page.getSize()),
                queryWrapper
        );

        // 转换为 VO
        List<UserVO> userVOList = userPage.getRecords().stream().map(user -> {
            UserVO vo = new UserVO();
            BeanUtils.copyProperties(user, vo);
            return vo;
        }).collect(Collectors.toList());

        // 简化：直接使用 PageResult 的构造函数
        return new PageResult<>(
                userVOList,
                userPage.getTotal(),
                (int) userPage.getCurrent(),
                (int) userPage.getSize()
        );
    }

    @Override
    public boolean updateUserStatus(Long userId, Integer status) {
        // 验证状态值是否有效
        if (status == null || (status != 0 && status != 1)) {
            return false;
        }

        // 查找用户
        User user = this.getById(userId);
        if (user == null) {
            return false;
        }

        // 更新状态
        user.setStatus(status);
        return this.updateById(user);
    }
}
