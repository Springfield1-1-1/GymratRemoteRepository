package com.springfield.gymrat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.springfield.gymrat.entity.GymStore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface GymStoreMapper extends BaseMapper<GymStore> {

    List<GymStore> selectByCity(@Param("city") String city);
}
