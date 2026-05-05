create table fitness_plans
(
    id          bigint auto_increment comment '主键 ID'
        primary key,
    user_id     varchar(50)                        not null comment '用户 ID',
    plan_data   json                               not null comment '计划数据（JSON 格式）',
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间'
)
    comment '健身计划表';

create index idx_user_id
    on fitness_plans (user_id);

