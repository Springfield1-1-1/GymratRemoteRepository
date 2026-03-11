package com.springfield.gymrat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.springfield.gymrat.dto.EquipmentQueryDTO;
import com.springfield.gymrat.entity.Equipment;
import com.springfield.gymrat.vo.EquipmentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EquipmentMapper extends BaseMapper<Equipment> {

    /**
     * 分页查询器械列表
     */
    IPage<EquipmentVO> selectEquipmentPage(Page<EquipmentVO> page, @Param("query") EquipmentQueryDTO query);

    /**
     * 根据分类ID统计器械数量
     */
    @Select("SELECT COUNT(*) FROM equipment WHERE category_id = #{categoryId}")
    Long countByCategoryId(@Param("categoryId") Integer categoryId);

    /**
     * 根据分类代码统计器械数量
     */
    @Select("SELECT COUNT(*) FROM equipment e " +
            "JOIN equipment_category ec ON e.category_id = ec.id " +
            "WHERE ec.category_code = #{categoryCode}")
    Long countByCategoryCode(@Param("categoryCode") String categoryCode);
}
