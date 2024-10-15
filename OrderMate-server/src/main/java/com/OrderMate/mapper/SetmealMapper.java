package com.OrderMate.mapper;

import com.github.pagehelper.Page;
import com.OrderMate.annotation.AutoFill;
import com.OrderMate.dto.SetmealPageQueryDTO;
import com.OrderMate.entity.Setmeal;
import com.OrderMate.enumeration.OperationType;
import com.OrderMate.vo.DishItemVO;
import com.OrderMate.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * ClassName: SetmealMapper
 * Package: com.sky.mapper
 * Description:
 *
 * @Author Gush
 * @Create 2024-02-19 21:18
 */
@Mapper
public interface SetmealMapper {
    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);

    @AutoFill(OperationType.INSERT)
    void insert(Setmeal setmeal);

    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    void deleteByIds(List<Long> ids);

    @Select("select * from setmeal where id = #{id}")
    Setmeal getById(Long id);

    /**
     * 动态条件查询套餐
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 根据套餐id查询菜品选项
     * @param setmealId
     * @return
     */
    @Select("select sd.name, sd.copies, d.image, d.description " +
            "from setmeal_dish sd left join dish d on sd.dish_id = d.id " +
            "where sd.setmeal_id = #{setmealId}")
    List<DishItemVO> getDishItemBySetmealId(Long setmealId);
}
