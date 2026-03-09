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
@TableName("users")
public class User implements Serializable{
    private static final long serialVersionUID = 1L;


    @TableId(type = IdType.AUTO)
    private Long id;

    private String username;

    private String phone;

    private String passwordHash;

    private String avatarUrl;

    private Integer status = 1;

    private Boolean isVerified = false;

    private LocalDateTime lastLoginAt;

    private String lastLoginIp;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

}
