package com.springfield.gymrat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FitnessPlanSaveDTO {

    private String userId;

    private PlanDataDTO planData;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlanDataDTO {
        private List<PlanItemDTO> day1;
        private List<PlanItemDTO> day2;
        private List<PlanItemDTO> day3;
        private List<PlanItemDTO> day4;
        private List<PlanItemDTO> day5;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlanItemDTO {
        private String name;
        private Integer sets;
        private String reps;
        private Double weight;
        private Integer order;
    }
}
