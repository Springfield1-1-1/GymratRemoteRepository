package com.springfield.gymrat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.springfield.gymrat.entity.EquipmentCategory;
import com.springfield.gymrat.vo.CategoryVO;

import java.util.List;

public interface EquipmentCategoryService extends IService<EquipmentCategory> {

    /**
     * 获取所有分类（包含器械数量）
     */
    List<CategoryVO> getAllCategories();

    /**
     * 根据分类代码获取分类
     */
    EquipmentCategory getByCategoryCode(String categoryCode);
}
