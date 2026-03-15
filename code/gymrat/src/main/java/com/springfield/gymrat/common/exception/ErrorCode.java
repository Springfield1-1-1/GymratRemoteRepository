package com.springfield.gymrat.common.exception;

import lombok.Getter;

/**
 * 错误码枚举
 */
@Getter
public enum ErrorCode {

    SUCCESS(200, "成功"),

    // 参数错误 1000-1999
    PARAMS_ERROR(1000, "参数错误"),
    PARAMS_NULL(1001, "参数为空"),
    PARAMS_VALIDATE_FAILED(1002, "参数校验失败"),

    // 用户错误 2000-2999
    USER_NOT_EXIST(2000, "用户不存在"),
    USER_EXIST(2001, "用户已存在"),
    USER_PASSWORD_ERROR(2002, "密码错误"),
    USER_NOT_LOGIN(2003, "用户未登录"),
    USER_DISABLED(2004, "用户已被禁用"),
    USERNAME_ALREADY_EXISTS(1002, "用户名已存在"),

    // 业务错误 3000-3999
    BUSINESS_ERROR(3000, "业务错误"),

    // 系统错误 5000-5999
    SYSTEM_ERROR(5000, "系统错误"),
    DATABASE_ERROR(5001, "数据库错误");

    private final Integer code;
    private final String message;

    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
