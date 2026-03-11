package com.springfield.gymrat.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springfield.gymrat.dto.EquipmentQueryDTO;
import com.springfield.gymrat.entity.Equipment;
import com.springfield.gymrat.mapper.EquipmentMapper;
import com.springfield.gymrat.service.EquipmentService;
import com.springfield.gymrat.vo.EquipmentVO;
import com.springfield.gymrat.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EquipmentServiceImpl extends ServiceImpl<EquipmentMapper, Equipment>
        implements EquipmentService {

    @Autowired
    private EquipmentMapper equipmentMapper;

    @Override
    public PageResult<EquipmentVO> getEquipmentPage(EquipmentQueryDTO query) {
        Page<EquipmentVO> page = new Page<>(query.getPage(), query.getPageSize());
        Page<EquipmentVO> resultPage = (Page<EquipmentVO>) equipmentMapper.selectEquipmentPage(page, query);

        return new PageResult<>(
                resultPage.getRecords(),
                resultPage.getTotal(),
                query.getPage(),
                query.getPageSize()
        );
    }

    @Override
    public Long countByCategoryId(Integer categoryId) {
        return equipmentMapper.countByCategoryId(categoryId);
    }

    @Override
    public Long countByCategoryCode(String categoryCode) {
        return equipmentMapper.countByCategoryCode(categoryCode);
    }
}
