package com.springfield.gymrat.controller;

import com.springfield.gymrat.common.context.UserContext;
import com.springfield.gymrat.service.GymStoreService;
import com.springfield.gymrat.vo.GymStoreVO;
import com.springfield.gymrat.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gym-stores")
@CrossOrigin(origins = "*")
public class GymStoreController {

    @Autowired
    private GymStoreService gymStoreService;

    @GetMapping
    public Result<List<GymStoreVO>> getStoresByCity(@RequestParam(required = false) String city) {
        Long userId = UserContext.getCurrentUserId();
        if (userId == null) {
            return Result.error("用户未登录");
        }

        List<GymStoreVO> stores = gymStoreService.getStoresByCity(city);
        return Result.success("获取成功", stores);
    }

    @GetMapping("/{storeId}")
    public Result<GymStoreVO> getStoreDetail(@PathVariable Long storeId) {
        Long userId = UserContext.getCurrentUserId();
        if (userId == null) {
            return Result.error("用户未登录");
        }

        GymStoreVO store = gymStoreService.getStoreDetail(storeId);
        if (store == null) {
            return Result.error("门店不存在");
        }
        return Result.success("获取成功", store);
    }
}
