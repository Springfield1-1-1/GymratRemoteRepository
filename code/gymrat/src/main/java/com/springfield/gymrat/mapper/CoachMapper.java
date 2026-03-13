package com.springfield.gymrat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.springfield.gymrat.entity.Coach;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CoachMapper extends BaseMapper<Coach> {

    List<Coach> selectByStoreId(@Param("storeId") Long storeId);
}
