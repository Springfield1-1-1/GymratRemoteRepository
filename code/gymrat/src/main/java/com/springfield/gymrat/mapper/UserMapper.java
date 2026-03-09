package com.springfield.gymrat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.springfield.gymrat.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    int countByUsername(@Param("username") String username);


    int countByPhone(@Param("phone") String phone);

}
