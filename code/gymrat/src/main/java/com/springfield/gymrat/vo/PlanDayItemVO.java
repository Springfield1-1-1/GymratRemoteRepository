package com.springfield.gymrat.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanDayItemVO {

    private Long id;

    private String name;

    private Integer sets;

    private String reps;

    private Double weight;

    private Integer order;
}
