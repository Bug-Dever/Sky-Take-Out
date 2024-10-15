package com.OrderMate.mapper;

import com.github.pagehelper.Page;
import com.OrderMate.annotation.AutoFill;
import com.OrderMate.dto.DishPageQueryDTO;
import com.OrderMate.entity.Dish;
import com.OrderMate.enumeration.OperationType;
import com.OrderMate.vo.DishVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * ClassName: DishMapper
 * Package: com.sky.mapper
 * Description:
 *
 * @Author Gush
 * @Create 2024-02-19 21:17
 */
@Mapper
public interface DishMapper {
    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);

    // 加入菜品
    @AutoFill(OperationType.INSERT)
    void insert(Dish dish);

    Page<DishVO> pageQuery(DishPageQueryDTO dishPageQueryDTO);

    @Select("select * from dish where id = #{id}")
    Dish getById(Long id);

    @Delete("delete from dish where id = #{id}")
    void deleteById(Long id);

    void deleteByIds(List<Long> ids);

    @AutoFill(OperationType.UPDATE)
    void update(Dish dish);

    List<Dish> getByCategoryId(Dish dish);
}
