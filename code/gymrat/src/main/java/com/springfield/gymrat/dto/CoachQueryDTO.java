package com.springfield.gymrat.dto;

import lombok.Data;

@Data
public class CoachQueryDTO {

    private String keyword;

    private Long storeId;

    private Integer status;
}
