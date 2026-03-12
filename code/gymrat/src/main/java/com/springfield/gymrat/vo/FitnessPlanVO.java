package com.springfield.gymrat.vo;

import com.springfield.gymrat.vo.PlanDayItemVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FitnessPlanVO {

    private Long id;

    private String userId;

    private PlanDataVO planData;

    private String createTime;

    private String updateTime;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlanDataVO {
        private List<PlanDayItemVO> day1;
        private List<PlanDayItemVO> day2;
        private List<PlanDayItemVO> day3;
        private List<PlanDayItemVO> day4;
        private List<PlanDayItemVO> day5;
    }
}
