package com.springfield.gymrat.controller;

import com.springfield.gymrat.common.Result;
import com.springfield.gymrat.common.context.UserContext;
import com.springfield.gymrat.service.UserService;
import com.springfield.gymrat.vo.DataOverviewVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    /**
     * 数据概览
     */
    @GetMapping("/data-overview")
    public Result<DataOverviewVO> dataOverview() {
        Long userId = UserContext.getCurrentUserId();
        if (userId == null) {
            return Result.error("未登录");
        }

        DataOverviewVO overview = userService.getDataOverview();
        return Result.success(overview);
    }
}
