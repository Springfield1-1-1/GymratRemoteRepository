package com.springfield.gymrat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springfield.gymrat.dto.GymStoreSaveDTO;
import com.springfield.gymrat.dto.StoreQueryDTO;
import com.springfield.gymrat.entity.Coach;
import com.springfield.gymrat.entity.GymStore;
import com.springfield.gymrat.mapper.CoachMapper;
import com.springfield.gymrat.mapper.GymStoreMapper;
import com.springfield.gymrat.service.GymStoreService;
import com.springfield.gymrat.vo.CoachVO;
import com.springfield.gymrat.vo.GymStoreVO;
import com.springfield.gymrat.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GymStoreServiceImpl implements GymStoreService {

    @Autowired
    private GymStoreMapper gymStoreMapper;

    @Autowired
    private CoachMapper coachMapper;

    @Override
    public List<GymStoreVO> getStoresByCity(String city) {
        List<GymStore> stores = gymStoreMapper.selectByCity(city);
        return stores.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public GymStoreVO getStoreDetail(Long storeId) {
        GymStore store = gymStoreMapper.selectById(storeId);
        if (store == null) {
            return null;
        }

        GymStoreVO vo = convertToVO(store);

        List<Coach> coaches = coachMapper.selectByStoreId(storeId);
        List<CoachVO> coachVOs = coaches.stream()
                .map(coach -> CoachVO.builder()
                        .id(coach.getId())
                        .coachName(coach.getCoachName())
                        .coachAvatar(coach.getCoachAvatar())
                        .specialty(coach.getSpecialty())
                        .build())
                .collect(Collectors.toList());

        vo.setCoaches(coachVOs);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveStore(GymStoreSaveDTO dto) {
        GymStore store;

        if (dto.getId() != null && dto.getId() > 0) {
            // 更新
            store = gymStoreMapper.selectById(dto.getId());
            if (store == null) {
                return false;
            }
            // 只更新允许修改的字段
            store.setStoreName(dto.getStoreName());
            store.setStoreImage(dto.getStoreImage());
            store.setCity(dto.getCity());
            store.setAddress(dto.getAddress());
            store.setPhone(dto.getPhone());
            store.setStatus(dto.getStatus());

            // 自动设置更新时间
            store.setUpdateTime(LocalDateTime.now());

            return gymStoreMapper.updateById(store) > 0;
        } else {
            // 新增
            store = new GymStore();
            store.setStoreName(dto.getStoreName());
            store.setStoreImage(dto.getStoreImage());
            store.setCity(dto.getCity());
            store.setAddress(dto.getAddress());
            store.setPhone(dto.getPhone());
            store.setStatus(dto.getStatus());

            // 设置创建时间和更新时间
            LocalDateTime now = LocalDateTime.now();
            store.setCreateTime(now);
            store.setUpdateTime(now);

            return gymStoreMapper.insert(store) > 0;
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteStore(Long id) {
        if (id == null || id <= 0) {
            return false;
        }

        GymStore store = gymStoreMapper.selectById(id);
        if (store == null) {
            return false;
        }

        // 由于外键设置了 ON DELETE CASCADE，删除门店会自动删除关联教练
        return gymStoreMapper.deleteById(id) > 0;
    }

    @Override
    public PageResult<GymStoreVO> getStoreList(Page<GymStoreVO> page, StoreQueryDTO queryDTO) {
        QueryWrapper<GymStore> queryWrapper = new QueryWrapper<>();

        // 模糊搜索
        if (queryDTO != null) {
            if (queryDTO.getKeyword() != null && !queryDTO.getKeyword().trim().isEmpty()) {
                queryWrapper.and(wrapper -> wrapper
                        .like("store_name", queryDTO.getKeyword())
                        .or()
                        .like("address", queryDTO.getKeyword())
                        .or()
                        .like("phone", queryDTO.getKeyword())
                );
            }

            if (queryDTO.getCity() != null && !queryDTO.getCity().trim().isEmpty()) {
                queryWrapper.eq("city", queryDTO.getCity());
            }

            if (queryDTO.getStatus() != null) {
                queryWrapper.eq("status", queryDTO.getStatus());
            }
        }

        Page<GymStore> storePage = gymStoreMapper.selectPage(
                new Page<>(page.getCurrent(), page.getSize()),
                queryWrapper
        );

        // 转换为 VO
        List<GymStoreVO> storeVOList = storePage.getRecords().stream().map(this::convertToVO).collect(Collectors.toList());

        // 构建返回结果
        return new PageResult<>(
                storeVOList,
                storePage.getTotal(),
                (int) storePage.getCurrent(),
                (int) storePage.getSize()
        );
    }

    private GymStoreVO convertToVO(GymStore store) {
        return GymStoreVO.builder()
                .id(store.getId())
                .storeName(store.getStoreName())
                .storeImage(store.getStoreImage())
                .city(store.getCity())
                .address(store.getAddress())
                .phone(store.getPhone())
                .status(store.getStatus())
                .coaches(new ArrayList<>())
                .build();
    }
}
