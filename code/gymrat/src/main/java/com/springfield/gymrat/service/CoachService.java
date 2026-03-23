package com.springfield.gymrat.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springfield.gymrat.dto.CoachQueryDTO;
import com.springfield.gymrat.dto.CoachSaveDTO;
import com.springfield.gymrat.vo.CoachVO;
import com.springfield.gymrat.vo.PageResult;
import com.springfield.gymrat.dto.CoachQueryDTO;

import java.util.List;

public interface CoachService {

    List<CoachVO> getCoachesByStoreId(Long storeId);

    boolean saveCoach(CoachSaveDTO dto);

    boolean deleteCoach(Long id);

    PageResult<CoachVO> getCoachList(Page<CoachVO> page, CoachQueryDTO queryDTO);
}
