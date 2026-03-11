package com.springfield.gymrat.vo;

import lombok.Data;

@Data
public class EquipmentVO {

    private Integer id;

    private String name;

    private String imageUrl;

    private String targetMuscles;

    private String usageDescription;

    private Integer categoryId;

    private String categoryName;

    private String categoryCode;
}
