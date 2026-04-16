package com.springfield.gymrat.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.springfield.gymrat.dto.EquipmentQueryDTO;
import com.springfield.gymrat.dto.EquipmentSaveDTO;
import com.springfield.gymrat.entity.Equipment;
import com.springfield.gymrat.mapper.EquipmentMapper;
import com.springfield.gymrat.service.EquipmentService;
import com.springfield.gymrat.vo.EquipmentVO;
import com.springfield.gymrat.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveEquipment(EquipmentSaveDTO dto) {
        Equipment equipment;

        if (dto.getId() != null && dto.getId() > 0) {
            // 更新
            equipment = this.getById(dto.getId());
            if (equipment == null) {
                return false;
            }
            // 只更新允许修改的字段
            equipment.setName(dto.getName());
            equipment.setImageUrl(dto.getImageUrl());
            equipment.setTargetMuscles(dto.getTargetMuscles());
            equipment.setUsageDescription(dto.getUsageDescription());
            equipment.setCategoryId(dto.getCategoryId());
            equipment.setSortOrder(dto.getSortOrder());
            equipment.setUpdateTime(LocalDateTime.now());
        } else {
            // 新增
            equipment = new Equipment();
            equipment.setName(dto.getName());
            equipment.setImageUrl(dto.getImageUrl());
            equipment.setTargetMuscles(dto.getTargetMuscles());
            equipment.setUsageDescription(dto.getUsageDescription());
            equipment.setCategoryId(dto.getCategoryId());
            equipment.setSortOrder(dto.getSortOrder() != null ? dto.getSortOrder() : 0);

            // 设置创建时间和更新时间
            LocalDateTime now = LocalDateTime.now();
            equipment.setCreateTime(now);
            equipment.setUpdateTime(now);
        }

        return this.saveOrUpdate(equipment);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteEquipment(Integer id) {
        if (id == null || id <= 0) {
            return false;
        }

        Equipment equipment = this.getById(id);
        if (equipment == null) {
            return false;
        }

        // 由于外键设置了 ON DELETE CASCADE，删除器械会自动删除关联数据
        return this.removeById(id);
    }
}
