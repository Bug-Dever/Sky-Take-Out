package com.OrderMate.service;

import com.OrderMate.dto.SetmealDTO;
import com.OrderMate.dto.SetmealPageQueryDTO;
import com.OrderMate.entity.Setmeal;
import com.OrderMate.result.PageResult;
import com.OrderMate.vo.DishItemVO;
import com.OrderMate.vo.SetmealVO;

import java.util.List;

/**
 * ClassName: SetmealService
 * Package: com.OrderMate.service
 * Description:
 *
 * @Author Gush
 * @Create 2024-02-25 13:08
 */
public interface SetmealService {

    void saveWithDish(SetmealDTO setmealDTO);

    PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    void deleteBatch(List<Long> ids);

    SetmealVO getByIdWithDish(Long id);

    void update(SetmealDTO setmealDTO);

    void startOrStop(Integer status, Long id);

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    List<DishItemVO> getDishItemById(Long id);
}
