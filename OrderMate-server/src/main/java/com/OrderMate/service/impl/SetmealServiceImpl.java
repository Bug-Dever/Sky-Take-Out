package com.OrderMate.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.OrderMate.constant.MessageConstant;
import com.OrderMate.constant.StatusConstant;
import com.OrderMate.dto.SetmealDTO;
import com.OrderMate.dto.SetmealPageQueryDTO;
import com.OrderMate.entity.Dish;
import com.OrderMate.entity.Setmeal;
import com.OrderMate.entity.SetmealDish;
import com.OrderMate.exception.DeletionNotAllowedException;
import com.OrderMate.mapper.DishMapper;
import com.OrderMate.mapper.SetmealDishMapper;
import com.OrderMate.mapper.SetmealMapper;
import com.OrderMate.result.PageResult;
import com.OrderMate.service.SetmealService;
import com.OrderMate.vo.DishItemVO;
import com.OrderMate.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: SetmealServiceImpl
 * Package: com.OrderMate.service.impl
 * Description:
 *
 * @Author Gush
 * @Create 2024-02-25 13:08
 */
@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private DishMapper dishMapper;
    /*
    * 新增套餐
    * */
    public void saveWithDish(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        // 插入1条套餐数据
        setmealMapper.insert(setmeal);

        // 插入与套餐关联的菜品信息
        Long setmealId = setmeal.getId();
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if(setmealDishes != null && setmealDishes.size() > 0) {
            setmealDishes.forEach(setmealDish -> {
                setmealDish.setSetmealId(setmealId);
            });
        }
        setmealDishMapper.insertBatch(setmealDishes);

    }

    /*
    * 套餐分页查询
    * */
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());
    }

    /*
    * 批量删除套餐
    * */
    @Transactional
    public void deleteBatch(List<Long> ids) {
        // 判断套餐是否在售
        ids.forEach(id -> {
            Setmeal setmeal = setmealMapper.getById(id);
            if(setmeal.getStatus() == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        });
        // 删除套餐
        // delete from set_meal where id in (?,?,?)
        setmealMapper.deleteByIds(ids);
        // 删除套餐对应的菜品
        setmealDishMapper.deleteByIds(ids);
    }

    /*
    * 根据id查询套餐
    * */
    public SetmealVO getByIdWithDish(Long id) {
        // 获取套餐
        Setmeal setmeal = setmealMapper.getById(id);
        // 获取套餐关联的菜品
        List<SetmealDish> setmealDishes = setmealDishMapper.getById(id);
        // 封装到VO对象中
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal,setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);

        return setmealVO;
    }

    /*
    * 修改套餐
    * */
    public void update(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        // 修改套餐表
        setmealMapper.update(setmeal);
        // 删除关联的菜品
        List<Long> setmealIds = new ArrayList<>();
        setmealIds.add(setmealDTO.getId());

        setmealDishMapper.deleteByIds(setmealIds);
        // 重新插入修改后关联的菜品
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        if(setmealDishes != null && setmealDishes.size() > 0) {
            setmealDishes.forEach(setmealDish -> {
                setmealDish.setSetmealId(setmealDTO.getId());
            });
        }
        setmealDishMapper.insertBatch(setmealDishes);

    }

    /*
    * 启用或禁用套餐
    * */
    public void startOrStop(Integer status, Long id) {

        // 启售时不能有菜品处于停售状态
        if(status == StatusConstant.ENABLE) {
            List<Dish> dishes = setmealDishMapper.getBySetmealId(id);// 根据套餐id获取套餐内包含的菜品
            dishes.forEach(dish -> {
                if(dish.getStatus() == StatusConstant.DISABLE) {
                    throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                }
            });
        }
        // 启售时没有菜品处于停售状态 & 套餐停售
        Setmeal setmeal = Setmeal.builder()
                .status(status)
                .id(id)
                .build();
        setmealMapper.update(setmeal);
    }

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }
}
