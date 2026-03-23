package com.springfield.gymrat.vo;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserVO {

    private Long id;

    private String avatarUrl;

    private String username;

    private String phone;

    private Integer status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
