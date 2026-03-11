package com.springfield.gymrat.controller;

import com.springfield.gymrat.dto.EquipmentQueryDTO;
import com.springfield.gymrat.service.EquipmentCategoryService;
import com.springfield.gymrat.service.EquipmentService;
import com.springfield.gymrat.vo.CategoryVO;
import com.springfield.gymrat.vo.PageResult;
import com.springfield.gymrat.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipment")
public class EquipmentController {

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private EquipmentCategoryService categoryService;

    /**
     * 获取器械分类列表
     */
    @GetMapping("/categories")
    public Result<List<CategoryVO>> getCategories() {
        List<CategoryVO> categories = categoryService.getAllCategories();
        return Result.success(categories);
    }

    /**
     * 分页查询器械列表
     */
    @GetMapping("/list")
    public Result<PageResult<com.springfield.gymrat.vo.EquipmentVO>> getEquipmentList(
            @RequestParam(required = false) String categoryCode,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {

        EquipmentQueryDTO query = new EquipmentQueryDTO();
        query.setCategoryCode(categoryCode);
        query.setKeyword(keyword);
        query.setPage(page);
        query.setPageSize(pageSize);

        PageResult<com.springfield.gymrat.vo.EquipmentVO> result = equipmentService.getEquipmentPage(query);
        return Result.success(result);
    }

    /**
     * 根据分类ID统计器械数量
     */
    @GetMapping("/count")
    public Result<Long> countByCategoryId(@RequestParam(required = false) Integer categoryId,
                                          @RequestParam(required = false) String categoryCode) {
        Long count;
        if (categoryId != null) {
            count = equipmentService.countByCategoryId(categoryId);
        } else if (categoryCode != null) {
            count = equipmentService.countByCategoryCode(categoryCode);
        } else {
            count = equipmentService.count();
        }
        return Result.success(count);
    }
}
