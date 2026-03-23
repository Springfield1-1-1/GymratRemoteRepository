package com.springfield.gymrat.service.impl;

import com.springfield.gymrat.dto.CoachSaveDTO;
import com.springfield.gymrat.entity.Coach;
import com.springfield.gymrat.mapper.CoachMapper;
import com.springfield.gymrat.service.CoachService;
import com.springfield.gymrat.vo.CoachVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springfield.gymrat.dto.CoachQueryDTO;
import com.springfield.gymrat.vo.PageResult;

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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveCoach(CoachSaveDTO dto) {
        Coach coach;

        if (dto.getId() != null && dto.getId() > 0) {
            // 更新
            coach = coachMapper.selectById(dto.getId());
            if (coach == null) {
                return false;
            }
            // 只更新允许修改的字段
            coach.setStoreId(dto.getStoreId());
            coach.setCoachName(dto.getCoachName());
            coach.setCoachAvatar(dto.getCoachAvatar());
            coach.setSpecialty(dto.getSpecialty());
            coach.setStatus(dto.getStatus());

            // 自动设置更新时间
            coach.setUpdateTime(LocalDateTime.now());

            return coachMapper.updateById(coach) > 0;
        } else {
            // 新增
            coach = new Coach();
            coach.setStoreId(dto.getStoreId());
            coach.setCoachName(dto.getCoachName());
            coach.setCoachAvatar(dto.getCoachAvatar());
            coach.setSpecialty(dto.getSpecialty());
            coach.setStatus(dto.getStatus());

            // 设置创建时间和更新时间
            LocalDateTime now = LocalDateTime.now();
            coach.setCreateTime(now);
            coach.setUpdateTime(now);

            return coachMapper.insert(coach) > 0;
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteCoach(Long id) {
        if (id == null || id <= 0) {
            return false;
        }

        Coach coach = coachMapper.selectById(id);
        if (coach == null) {
            return false;
        }

        return coachMapper.deleteById(id) > 0;
    }

    @Override
    public PageResult<CoachVO> getCoachList(Page<CoachVO> page, CoachQueryDTO queryDTO) {
        QueryWrapper<Coach> queryWrapper = new QueryWrapper<>();

        // 模糊搜索
        if (queryDTO != null) {
            if (queryDTO.getKeyword() != null && !queryDTO.getKeyword().trim().isEmpty()) {
                queryWrapper.and(wrapper -> wrapper
                        .like("coach_name", queryDTO.getKeyword())
                        .or()
                        .like("specialty", queryDTO.getKeyword())
                );
            }

            if (queryDTO.getStoreId() != null) {
                queryWrapper.eq("store_id", queryDTO.getStoreId());
            }

            if (queryDTO.getStatus() != null) {
                queryWrapper.eq("status", queryDTO.getStatus());
            }
        }

        // 执行分页查询
        Page<Coach> coachPage = coachMapper.selectPage(
                new Page<>(page.getCurrent(), page.getSize()),
                queryWrapper
        );

        // 转换为 VO
        List<CoachVO> coachVOList = coachPage.getRecords().stream().map(coach ->
                CoachVO.builder()
                        .id(coach.getId())
                        .storeId(coach.getStoreId())
                        .coachName(coach.getCoachName())
                        .coachAvatar(coach.getCoachAvatar())
                        .specialty(coach.getSpecialty())
                        .status(coach.getStatus())
                        .build()
        ).collect(Collectors.toList());

        // 构建返回结果
        return new PageResult<>(
                coachVOList,
                coachPage.getTotal(),
                (int) coachPage.getCurrent(),
                (int) coachPage.getSize()
        );
    }
}
