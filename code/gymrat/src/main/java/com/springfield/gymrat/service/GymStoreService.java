package com.springfield.gymrat.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springfield.gymrat.dto.GymStoreSaveDTO;
import com.springfield.gymrat.dto.StoreQueryDTO;
import com.springfield.gymrat.vo.GymStoreVO;
import com.springfield.gymrat.vo.PageResult;

import java.util.List;

public interface GymStoreService {

    List<GymStoreVO> getStoresByCity(String city);

    GymStoreVO getStoreDetail(Long storeId);
    boolean saveStore(GymStoreSaveDTO dto);
    boolean deleteStore(Long id);
    PageResult<GymStoreVO> getStoreList(Page<GymStoreVO> page, StoreQueryDTO queryDTO);
}
