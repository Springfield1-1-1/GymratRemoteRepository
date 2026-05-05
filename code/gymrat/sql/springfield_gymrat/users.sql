create table users
(
    id            bigint unsigned auto_increment comment '用户ID，主键'
        primary key,
    username      varchar(50)                                                                                                                                                                                                                                                                                                                               not null comment '用户名，唯一，用于登录',
    phone         varchar(20)                                                                                                                                                                                                                                                                                                                               not null comment '手机号，唯一',
    password_hash varchar(255)                                                                                                                                                                                                                                                                                                                              not null comment 'BCrypt加密后的密码',
    avatar_url    varchar(500) default 'https://springfield-gymrat.oss-cn-beijing.aliyuncs.com/%E7%94%A8%E6%88%B7%E5%A4%B4%E5%83%8F/%E9%BB%98%E8%AE%A4%E5%A4%B4%E5%83%8F.jpg?Expires=1772990748&OSSAccessKeyId=TMP.3Komxt69JwB1JapoCELdsmFZt9AKPPQBHCp6xUSg6WfzQ3Qubhy8HgBMQrnMHBaY6iAHMf6BVA5cJSZrco72oABcX1cr1Q&Signature=CbhfawiWZPMCiV7pkRdWmQfOdGo%3D' null comment 'OSS存储的头像地址',
    status        tinyint      default 1                                                                                                                                                                                                                                                                                                                    not null comment '账号状态：1-正常 0-禁用',
    is_verified   tinyint      default 0                                                                                                                                                                                                                                                                                                                    not null comment '手机号是否已验证：0-未验证 1-已验证',
    last_login_at datetime                                                                                                                                                                                                                                                                                                                                  null,
    last_login_ip varchar(45)                                                                                                                                                                                                                                                                                                                               null,
    created_at    datetime     default CURRENT_TIMESTAMP                                                                                                                                                                                                                                                                                                    not null,
    updated_at    datetime     default CURRENT_TIMESTAMP                                                                                                                                                                                                                                                                                                    not null on update CURRENT_TIMESTAMP,
    constraint uk_phone
        unique (phone),
    constraint uk_username
        unique (username)
)
    comment '用户核心表';

