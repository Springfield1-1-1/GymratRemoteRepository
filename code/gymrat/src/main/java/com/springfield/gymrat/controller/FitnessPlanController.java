package com.springfield.gymrat.controller;

import com.springfield.gymrat.common.Result;
import com.springfield.gymrat.common.context.UserContext;
import com.springfield.gymrat.dto.FitnessPlanSaveDTO;
import com.springfield.gymrat.dto.FitnessPlanUpdateDTO;
import com.springfield.gymrat.service.FitnessPlanService;
import com.springfield.gymrat.vo.FitnessPlanVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fitness-plan")
@CrossOrigin(origins = "*")
public class FitnessPlanController {

    @Autowired
    private FitnessPlanService fitnessPlanService;

    @PostMapping("/save")
    public Result<FitnessPlanVO> savePlan(@RequestBody FitnessPlanSaveDTO saveDTO) {
        System.out.println("========================================");
        System.out.println("💾 保存健身计划");
        System.out.println("----------------------------------------");

        // 优先从 UserContext 获取
        Long userId = UserContext.getCurrentUserId();
        System.out.println("👤 UserContext userId: " + userId);
        System.out.println("📦 saveDTO.userId: " + saveDTO.getUserId());
        System.out.println("📦 saveDTO.planData: " + saveDTO.getPlanData());
        System.out.println("========================================");

        if (userId == null) {
            System.out.println("❌ 错误：用户未登录");
            return Result.error("用户未登录");
        }

        // 使用 UserContext 的 userId（更安全）
        saveDTO.setUserId(userId.toString());

        try {
            FitnessPlanVO result = fitnessPlanService.savePlan(saveDTO);
            System.out.println("✅ 保存成功，planId: " + result.getId());
            return Result.success("保存成功", result);
        } catch (Exception e) {
            System.out.println("❌ 保存失败：" + e.getMessage());
            e.printStackTrace();
            return Result.error("保存失败：" + e.getMessage());
        }
    }

    @GetMapping("/current")
    public Result<FitnessPlanVO> getCurrentPlan() {
        Long userId = UserContext.getCurrentUserId();
        if (userId == null) {
            return Result.error("用户未登录");
        }
        FitnessPlanVO result = fitnessPlanService.getCurrentPlan(userId.toString());
        return Result.success("获取成功", result);
    }

    @PutMapping("/update")
    public Result<FitnessPlanVO> updatePlan(@RequestBody FitnessPlanUpdateDTO updateDTO) {
        Long userId = UserContext.getCurrentUserId();
        if (userId == null) {
            return Result.error("用户未登录");
        }

        updateDTO.setUserId(userId.toString());
        FitnessPlanVO result = fitnessPlanService.updatePlan(updateDTO);
        return Result.success("更新成功", result);
    }

    @DeleteMapping("/delete")
    public Result<String> deletePlan(@RequestParam Long id) {
        fitnessPlanService.deletePlan(id);
        return Result.success("删除成功", "success");
    }
}
