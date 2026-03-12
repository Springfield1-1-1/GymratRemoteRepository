package com.springfield.gymrat.service;

import com.springfield.gymrat.dto.FitnessPlanSaveDTO;
import com.springfield.gymrat.dto.FitnessPlanUpdateDTO;
import com.springfield.gymrat.vo.FitnessPlanVO;

public interface FitnessPlanService {

    FitnessPlanVO savePlan(FitnessPlanSaveDTO saveDTO);

    FitnessPlanVO getCurrentPlan(String userId);

    FitnessPlanVO updatePlan(FitnessPlanUpdateDTO updateDTO);

    void deletePlan(Long id);
}
