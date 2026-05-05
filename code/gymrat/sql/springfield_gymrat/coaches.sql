create table coaches
(
    id           bigint auto_increment comment '教练 ID'
        primary key,
    store_id     bigint                             not null comment '所属门店 ID',
    coach_name   varchar(50)                        not null comment '教练姓名',
    coach_avatar varchar(500)                       null comment '教练头像 URL',
    specialty    varchar(200)                       null comment '特长',
    status       tinyint  default 1                 null comment '状态：1-在职，0-离职',
    create_time  datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time  datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint coaches_ibfk_1
        foreign key (store_id) references gym_stores (id)
            on delete cascade
)
    comment '教练表';

create index idx_status
    on coaches (status);

create index idx_store_id
    on coaches (store_id);

