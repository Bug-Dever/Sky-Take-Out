package com.OrderMate.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.OrderMate.constant.MessageConstant;
import com.OrderMate.constant.StatusConstant;
import com.OrderMate.dto.DishDTO;
import com.OrderMate.dto.DishPageQueryDTO;
import com.OrderMate.entity.Dish;
import com.OrderMate.entity.DishFlavor;
import com.OrderMate.entity.Setmeal;
import com.OrderMate.exception.DeletionNotAllowedException;
import com.OrderMate.mapper.DishFlavorMapper;
import com.OrderMate.mapper.DishMapper;
import com.OrderMate.mapper.SetmealDishMapper;
import com.OrderMate.mapper.SetmealMapper;
import com.OrderMate.result.PageResult;
import com.OrderMate.service.DishService;
import com.OrderMate.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: DishServiceImpl
 * Package: com.OrderMate.service.impl
 * Description:
 *
 * @Author Gush
 * @Create 2024-02-22 17:15
 */
@Service
@Slf4j
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    // 新增菜品和对应的口味
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);

        // 向菜品表插入1条数据
        dishMapper.insert(dish);
        // 获取insert语句生成的主键值
        Long dishId = dish.getId();

        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors != null && flavors.size() > 0) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);
            });
            // 向口味表插入n条数据
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /*
    * 菜品分页查询
    * */
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());

        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO); // 返回值是该插件固定的
        long total = page.getTotal();
        List<DishVO> records = page.getResult();
        PageResult pageResult = new PageResult(total,records);
        return pageResult;
    }

    /*
    * 菜品批量删除
    * */
    @Transactional
    public void deleteBatch(List<Long> ids) {
        // 判断当前菜品是否能够删除 -》 有无启售中？
        for (Long id : ids) {
            Dish dish = dishMapper.getById(id);
            if(dish.getStatus() == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        // 判断当前菜品是否能够删除 -》 有无被套餐关联？
        List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
        if(setmealIds != null && setmealIds.size() > 0) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
//        // 删除菜品表中的菜品数据
//        for (Long id : ids) {
//            dishMapper.deleteById(id);
//        }
//
//        // 删除菜品关联的口味数据
//        for (Long id : ids) {
//            dishFlavorMapper.deleteByDishId(id);
//        }
        // 根据菜品id集合批量删除菜品数据
        dishMapper.deleteByIds(ids);
        // 根据菜品id集合批量删除口味数据
        dishFlavorMapper.deleteByDishIds(ids);
    }

    /*
    * 根据id查询对应的菜品和口味数据
    * */
    public DishVO getByIdWithFlavor(Long id) {
        // 根据id查询菜品数据
        Dish dish = dishMapper.getById(id);
        // 根据菜品id查询口味数据
        List<DishFlavor> dishFlavors = dishFlavorMapper.getByDishId(id);
        // 将查询到的数据封装到DishVO
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(dishFlavors);
        return dishVO;
    }

    /*
    * 根据id修改菜品的基本信息和口味信息
    * */
    public void updateWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        // 修改菜品的基本信息
        dishMapper.update(dish);
        // 删除原有的口味数据
        dishFlavorMapper.deleteByDishId(dishDTO.getId());
        // 重新插入口味数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors != null && flavors.size() > 0) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishDTO.getId());
            });
            // 向口味表插入n条数据
            dishFlavorMapper.insertBatch(flavors);
        }
    }

    /*
    * 菜品启售停售
    * */
    @Transactional
    public void startOrStop(Integer status,Long id) {
        Dish dish = Dish.builder()
                .status(status)
                .id(id)
                .build();
        dishMapper.update(dish);

        // 如果菜品停售，包含此菜品的套餐也要停售
        if(status == StatusConstant.DISABLE) {
            List<Long> dishIds = new ArrayList<>();
            dishIds.add(id);
            List<Long> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(dishIds);
            if(setmealIds != null && setmealIds.size() > 0) {
                for(Long setmealId : setmealIds) {
                    Setmeal setmeal = Setmeal.builder()
                            .status(status)
                            .id(setmealId)
                            .build();
                    setmealMapper.update(setmeal);
                }
            }
        }
    }

    /*
    * 根据分类id查询菜品
    * */
    public List<Dish> list(Long categoryId) {
        Dish dish = Dish.builder()
                .status(StatusConstant.ENABLE)
                .categoryId(categoryId)
                .build();
        List<Dish> list = dishMapper.getByCategoryId(dish);
        return list;
    }

    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.getByCategoryId(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.getByDishId(d.getId());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }
}
