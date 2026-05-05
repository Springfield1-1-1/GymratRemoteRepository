create table equipment_category
(
    id            int auto_increment comment '分类ID'
        primary key,
    category_code varchar(50)                        not null comment '分类代码（英文标识）',
    category_name varchar(100)                       not null comment '分类名称',
    icon          varchar(50)                        null comment '图标名称',
    sort_order    int      default 0                 null comment '排序',
    create_time   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time   datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint uk_category_code
        unique (category_code)
)
    comment '器械分类表';

