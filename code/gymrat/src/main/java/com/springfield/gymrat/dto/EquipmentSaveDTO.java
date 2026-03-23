package com.springfield.gymrat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EquipmentSaveDTO {

    private Integer id;

    @NotBlank(message = "器械名称不能为空")
    private String name;

    private String imageUrl;

    @NotBlank(message = "目标肌群不能为空")
    private String targetMuscles;

    @NotBlank(message = "使用方法描述不能为空")
    private String usageDescription;

    @NotNull(message = "分类ID不能为空")
    private Integer categoryId;

    private Integer sortOrder;
}
