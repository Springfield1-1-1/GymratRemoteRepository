package com.springfield.gymrat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.springfield.gymrat.entity.Coach;
import com.springfield.gymrat.entity.GymStore;
import com.springfield.gymrat.mapper.CoachMapper;
import com.springfield.gymrat.mapper.GymStoreMapper;
import com.springfield.gymrat.service.GymStoreService;
import com.springfield.gymrat.vo.CoachVO;
import com.springfield.gymrat.vo.GymStoreVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    private GymStoreVO convertToVO(GymStore store) {
        return GymStoreVO.builder()
                .id(store.getId())
                .storeName(store.getStoreName())
                .storeImage(store.getStoreImage())
                .city(store.getCity())
                .address(store.getAddress())
                .phone(store.getPhone())
                .coaches(new ArrayList<>())
                .build();
    }
}
