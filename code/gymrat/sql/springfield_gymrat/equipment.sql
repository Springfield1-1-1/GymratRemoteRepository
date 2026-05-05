create table equipment
(
    id                int auto_increment comment '器械ID'
        primary key,
    name              varchar(100)                       not null comment '器械名称',
    image_url         varchar(500)                       null comment '图片URL',
    target_muscles    varchar(200)                       not null comment '目标肌群',
    usage_description text                               not null comment '使用方法描述',
    category_id       int                                not null comment '分类ID',
    sort_order        int      default 0                 null comment '排序',
    create_time       datetime default CURRENT_TIMESTAMP null comment '创建时间',
    update_time       datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    constraint fk_equipment_category
        foreign key (category_id) references equipment_category (id)
            on delete cascade
)
    comment '器械表';

create index idx_category_id
    on equipment (category_id);

create index idx_name
    on equipment (name);

