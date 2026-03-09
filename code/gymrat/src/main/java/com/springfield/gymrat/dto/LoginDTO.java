package com.springfield.gymrat.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class LoginDTO {

    @NotBlank(message = "账号不能为空")
    private String identifier;

    @NotBlank(message = "密码不能为空")
    private String password;
}