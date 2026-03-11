package com.springfield.gymrat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springfield.gymrat.entity.EquipmentCategory;
import com.springfield.gymrat.mapper.EquipmentCategoryMapper;
import com.springfield.gymrat.service.EquipmentCategoryService;
import com.springfield.gymrat.vo.CategoryVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipmentCategoryServiceImpl extends ServiceImpl<EquipmentCategoryMapper, EquipmentCategory>
        implements EquipmentCategoryService {

    @Override
    public List<CategoryVO> getAllCategories() {
        List<EquipmentCategory> categories = baseMapper.selectCategoriesWithCount();
        return categories.stream().map(category -> {
            CategoryVO vo = new CategoryVO();
            BeanUtils.copyProperties(category, vo);
            vo.setEquipmentCount(category.getEquipmentCount());
            return vo;
        }).collect(Collectors.toList());
    }

    @Override
    public EquipmentCategory getByCategoryCode(String categoryCode) {
        return baseMapper.selectByCategoryCode(categoryCode);
    }
}
