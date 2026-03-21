package com.springfield.gymrat.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {
    USER("user", "普通用户"),
    ADMIN("admin", "管理员");

    private final String code;
    private final String description;
}
