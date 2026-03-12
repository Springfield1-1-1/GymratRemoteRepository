package com.springfield.gymrat.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springfield.gymrat.common.exception.BusinessException;
import com.springfield.gymrat.dto.FitnessPlanSaveDTO;
import com.springfield.gymrat.dto.FitnessPlanUpdateDTO;
import com.springfield.gymrat.entity.FitnessPlan;
import com.springfield.gymrat.mapper.FitnessPlanMapper;
import com.springfield.gymrat.service.FitnessPlanService;
import com.springfield.gymrat.vo.FitnessPlanVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;

@Service
public class FitnessPlanServiceImpl implements FitnessPlanService {

    @Autowired
    private FitnessPlanMapper fitnessPlanMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FitnessPlanVO savePlan(FitnessPlanSaveDTO saveDTO) {
        // 先查询该用户是否已有计划
        FitnessPlan existingPlan = fitnessPlanMapper.selectLatestByUserId(saveDTO.getUserId());

        if (existingPlan != null) {
            // 已有计划，执行更新
            try {
                existingPlan.setPlanData(objectMapper.writeValueAsString(saveDTO.getPlanData()));
                existingPlan.setUpdateTime(java.time.LocalDateTime.now());
            } catch (JsonProcessingException e) {
                throw new BusinessException("计划数据转换失败");
            }

            fitnessPlanMapper.updateById(existingPlan);
            return buildFitnessPlanVO(existingPlan);
        } else {
            // 没有计划，执行插入
            FitnessPlan fitnessPlan = new FitnessPlan();
            fitnessPlan.setUserId(saveDTO.getUserId());
            try {
                fitnessPlan.setPlanData(objectMapper.writeValueAsString(saveDTO.getPlanData()));
            } catch (JsonProcessingException e) {
                throw new BusinessException("计划数据转换失败");
            }

            // 手动设置时间
            fitnessPlan.setCreateTime(java.time.LocalDateTime.now());
            fitnessPlan.setUpdateTime(java.time.LocalDateTime.now());

            fitnessPlanMapper.insert(fitnessPlan);
            return buildFitnessPlanVO(fitnessPlan);
        }
    }

    @Override
    public FitnessPlanVO getCurrentPlan(String userId) {
        FitnessPlan fitnessPlan = fitnessPlanMapper.selectLatestByUserId(userId);
        if (fitnessPlan == null) {
            return null;
        }
        return buildFitnessPlanVO(fitnessPlan);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FitnessPlanVO updatePlan(FitnessPlanUpdateDTO updateDTO) {
        FitnessPlan existingPlan = fitnessPlanMapper.selectById(updateDTO.getId());
        if (existingPlan == null) {
            throw new BusinessException("计划不存在");
        }

        if (!existingPlan.getUserId().equals(updateDTO.getUserId())) {
            throw new BusinessException("无权修改该计划");
        }

        try {
            existingPlan.setPlanData(objectMapper.writeValueAsString(updateDTO.getPlanData()));
        } catch (JsonProcessingException e) {
            throw new BusinessException("计划数据转换失败");
        }

        fitnessPlanMapper.updateById(existingPlan);

        return buildFitnessPlanVO(existingPlan);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePlan(Long id) {
        fitnessPlanMapper.deleteById(id);
    }

    private FitnessPlanVO buildFitnessPlanVO(FitnessPlan fitnessPlan) {
        FitnessPlanVO.PlanDataVO planDataVO;
        try {
            planDataVO = objectMapper.readValue(
                    fitnessPlan.getPlanData(),
                    FitnessPlanVO.PlanDataVO.class
            );
        } catch (JsonProcessingException e) {
            throw new BusinessException("计划数据解析失败");
        }

        return FitnessPlanVO.builder()
                .id(fitnessPlan.getId())
                .userId(fitnessPlan.getUserId())
                .planData(planDataVO)
                .createTime(fitnessPlan.getCreateTime().format(FORMATTER))
                .updateTime(fitnessPlan.getUpdateTime().format(FORMATTER))
                .build();
    }
}
