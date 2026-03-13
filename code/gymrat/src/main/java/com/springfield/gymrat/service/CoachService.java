package com.springfield.gymrat.service;

import com.springfield.gymrat.vo.CoachVO;

import java.util.List;

public interface CoachService {

    List<CoachVO> getCoachesByStoreId(Long storeId);
}
