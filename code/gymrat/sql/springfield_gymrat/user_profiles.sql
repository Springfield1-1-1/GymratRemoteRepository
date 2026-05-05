create table user_profiles
(
    user_id      bigint unsigned                        not null comment '用户ID，关联users.id'
        primary key,
    gender       tinyint      default 0                 null comment '性别：0-未知 1-男 2-女',
    birthday     date                                   null comment '生日',
    height       int                                    null comment '身高（cm）',
    weight       decimal(5, 2)                          null comment '体重（kg）',
    fitness_goal varchar(50)                            null comment '健身目标：增肌/减脂',
    bio          varchar(200) default ''                null comment '个人简介',
    updated_at   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint fk_profile_user
        foreign key (user_id) references users (id)
            on delete cascade
)
    comment '用户资料表';

create index idx_fitness_goal
    on user_profiles (fitness_goal);

