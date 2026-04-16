package com.springfield.gymrat.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("users") //没有这个注解，MP 会默认使用类名的小写形式
public class User implements Serializable{
    private static final long serialVersionUID = 1L;


    @TableId(type = IdType.AUTO)    // 主键自增
    private Long id;

    private String username;

    private String phone;

    private String passwordHash;

    private String avatarUrl;

    private Integer status = 1;

    private Boolean isVerified = false;

    private LocalDateTime lastLoginAt;

    private String lastLoginIp;

    @TableField(fill = FieldFill.INSERT) // 插入时自动填充
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE) // 插入和更新时自动填充
    private LocalDateTime updatedAt;

}
