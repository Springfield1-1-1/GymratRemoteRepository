package com.springfield.gymrat.dto;

import lombok.Data;

@Data
public class EquipmentQueryDTO {

    /**
     * 分类代码
     */
    private String categoryCode;

    /**
     * 搜索关键词
     */
    private String keyword;

    /**
     * 当前页码
     */
    private Integer page = 1;

    /**
     * 每页数量
     */
    private Integer pageSize = 10;
}
