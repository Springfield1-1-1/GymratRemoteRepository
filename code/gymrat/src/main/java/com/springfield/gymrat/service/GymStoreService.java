package com.springfield.gymrat.service;

import com.springfield.gymrat.vo.GymStoreVO;

import java.util.List;

public interface GymStoreService {

    List<GymStoreVO> getStoresByCity(String city);

    GymStoreVO getStoreDetail(Long storeId);
}
