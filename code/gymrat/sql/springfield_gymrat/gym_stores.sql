create table gym_stores
(
    id          bigint auto_increment comment '门店 ID'
        primary key,
    store_name  varchar(100)                       not null comment '门店名称',
    store_image varchar(500)                       null comment '门店图片 URL',
    city        varchar(50)                        not null comment '城市',
    address     varchar(200)                       not null comment '具体地址',
    phone       varchar(20)                        null comment '联系电话',
    status      tinyint  default 1                 null comment '状态：1-营业中，0-已关闭',
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间'
)
    comment '健身门店表';

create index idx_city
    on gym_stores (city);

create index idx_status
    on gym_stores (status);

