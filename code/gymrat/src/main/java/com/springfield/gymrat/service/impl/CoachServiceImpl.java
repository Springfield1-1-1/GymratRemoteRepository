package com.springfield.gymrat.service.impl;

import com.springfield.gymrat.entity.Coach;
import com.springfield.gymrat.mapper.CoachMapper;
import com.springfield.gymrat.service.CoachService;
import com.springfield.gymrat.vo.CoachVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CoachServiceImpl implements CoachService {

    @Autowired
    private CoachMapper coachMapper;

    @Override
    public List<CoachVO> getCoachesByStoreId(Long storeId) {
        List<Coach> coaches = coachMapper.selectByStoreId(storeId);
        return coaches.stream()
                .map(coach -> CoachVO.builder()
                        .id(coach.getId())
                        .coachName(coach.getCoachName())
                        .coachAvatar(coach.getCoachAvatar())
                        .specialty(coach.getSpecialty())
                        .build())
                .collect(Collectors.toList());
    }
}
