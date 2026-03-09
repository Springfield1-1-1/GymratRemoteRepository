package com.springfield.gymrat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.springfield.gymrat.dto.RegisterDTO;
import com.springfield.gymrat.dto.LoginDTO;
import com.springfield.gymrat.entity.User;
public interface UserService extends IService<User>{
    Long register(RegisterDTO dto);

}
