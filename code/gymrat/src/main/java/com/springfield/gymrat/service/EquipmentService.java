package com.springfield.gymrat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.springfield.gymrat.dto.EquipmentQueryDTO;
import com.springfield.gymrat.entity.Equipment;
import com.springfield.gymrat.vo.EquipmentVO;
import com.springfield.gymrat.vo.PageResult;

public interface EquipmentService extends IService<Equipment> {

    /**
     * 分页查询器械列表
     */
    PageResult<EquipmentVO> getEquipmentPage(EquipmentQueryDTO query);

    /**
     * 根据分类ID统计器械数量
     */
    Long countByCategoryId(Integer categoryId);

    /**
     * 根据分类代码统计器械数量
     */
    Long countByCategoryCode(String categoryCode);
}
