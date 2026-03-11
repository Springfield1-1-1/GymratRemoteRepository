package com.springfield.gymrat.vo;

import lombok.Data;

@Data
public class CategoryVO {

    private Integer id;

    private String categoryCode;

    private String categoryName;

    private String icon;

    private Integer equipmentCount;
}
