package com.springfield.gymrat.dto;

import lombok.Data;

@Data
public class LoginResultDTO {
    private Long userId;
    private String username;
    private String token;
    private String tokenType = "Bearer";
    private Long expiresIn;  // 过期时间（秒）
    private String avatarUrl;
    private String role;
}
