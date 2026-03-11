package com.springfield.gymrat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.springfield.gymrat.entity.EquipmentCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EquipmentCategoryMapper extends BaseMapper<EquipmentCategory> {

    /**
     * 查询所有分类及其器械数量
     */
    @Select("SELECT ec.*, COUNT(e.id) as equipment_count " +
            "FROM equipment_category ec " +
            "LEFT JOIN equipment e ON ec.id = e.category_id " +
            "GROUP BY ec.id , ec.sort_order " +
            "ORDER BY ec.sort_order")
    List<EquipmentCategory> selectCategoriesWithCount();

    /**
     * 根据分类代码查询
     */
    @Select("SELECT * FROM equipment_category WHERE category_code = #{categoryCode}")
    EquipmentCategory selectByCategoryCode(@Param("categoryCode") String categoryCode);
}
