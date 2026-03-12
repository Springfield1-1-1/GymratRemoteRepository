package com.springfield.gymrat.dto;

import com.springfield.gymrat.dto.FitnessPlanSaveDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FitnessPlanUpdateDTO {

    private Long id;

    private String userId;

    private FitnessPlanSaveDTO.PlanDataDTO planData;
}
