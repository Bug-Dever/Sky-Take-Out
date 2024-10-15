package com.OrderMate.service;

import com.OrderMate.dto.DishDTO;
import com.OrderMate.dto.DishPageQueryDTO;
import com.OrderMate.entity.Dish;
import com.OrderMate.result.PageResult;
import com.OrderMate.vo.DishVO;

import java.util.List;

/**
 * ClassName: DishService
 * Package: com.OrderMate.service
 * Description:
 *
 * @Author Gush
 * @Create 2024-02-22 17:13
 */
public interface DishService {
    void saveWithFlavor(DishDTO dishDTO);

    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    void deleteBatch(List<Long> ids);

    DishVO getByIdWithFlavor(Long id);

    void updateWithFlavor(DishDTO dishDTO);

    void startOrStop(Integer status,Long id);

    List<Dish> list(Long categoryId);

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    List<DishVO> listWithFlavor(Dish dish);
}
