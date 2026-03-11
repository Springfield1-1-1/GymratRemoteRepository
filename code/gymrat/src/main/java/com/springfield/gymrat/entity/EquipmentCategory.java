package com.springfield.gymrat.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("equipment_category")
public class EquipmentCategory {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @TableField("category_code")
    private String categoryCode;

    @TableField("category_name")
    private String categoryName;

    @TableField("icon")
    private String icon;

    @TableField("sort_order")
    private Integer sortOrder;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    // 关联的器械数量（非数据库字段）
    @TableField(exist = false)
    private Integer equipmentCount;
}
