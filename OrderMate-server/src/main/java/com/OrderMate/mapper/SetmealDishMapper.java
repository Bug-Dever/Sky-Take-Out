package com.OrderMate.mapper;

import com.OrderMate.entity.Dish;
import com.OrderMate.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * ClassName: SetmealDishMapper
 * Package: com.sky.mapper
 * Description:
 *
 * @Author Gush
 * @Create 2024-02-22 21:24
 */
/*
* 根据DishId查询关联的套餐ID
* */
@Mapper
public interface SetmealDishMapper {
    List<Long> getSetmealIdsByDishIds(List<Long> DishIds);

    void insertBatch(List<SetmealDish> setmealDishes);

    void deleteByIds(List<Long> ids);

    @Select("select * from setmeal_dish where setmeal_id = #{setmealId}")
    List<SetmealDish> getById(Long setmealId);

    List<Dish> getBySetmealId(Long id);
}
