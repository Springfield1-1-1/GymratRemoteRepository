package com.springfield.gymrat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.springfield.gymrat.entity.FitnessPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FitnessPlanMapper extends BaseMapper<FitnessPlan> {

    @Select("SELECT * FROM fitness_plans WHERE user_id = #{userId} ORDER BY id DESC LIMIT 1")
    FitnessPlan selectLatestByUserId(@Param("userId") String userId);
}
