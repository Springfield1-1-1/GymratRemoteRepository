package com.springfield.gymrat.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.springfield.gymrat.entity.EquipmentCategory;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("equipment")
public class Equipment {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("name")
    private String name;

    @TableField("image_url")
    private String imageUrl;

    @TableField("target_muscles")
    private String targetMuscles;

    @TableField("usage_description")
    private String usageDescription;

    @TableField("category_id")
    private Integer categoryId;

    @TableField("sort_order")
    private Integer sortOrder;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    // 关联的分类信息（非数据库字段）
    @TableField(exist = false)
    private EquipmentCategory category;
}
