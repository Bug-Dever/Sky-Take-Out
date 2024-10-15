package com.OrderMate.controller.admin;

import com.OrderMate.dto.DishDTO;
import com.OrderMate.dto.DishPageQueryDTO;
import com.OrderMate.entity.Dish;
import com.OrderMate.result.PageResult;
import com.OrderMate.result.Result;
import com.OrderMate.service.DishService;
import com.OrderMate.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * ClassName: DishController
 * Package: com.OrderMate.controller.admin
 * Description:
 *
 * @Author Gush
 * @Create 2024-02-22 17:10
 */
/*
* 菜品管理
* */
@RestController
@RequestMapping("/admin/dish")
@Slf4j
@Api(tags = "菜品相关接口")
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;
    @PostMapping
    @ApiOperation("新增菜品")
    public Result save(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品:{}",dishDTO);
        dishService.saveWithFlavor(dishDTO);

        // 清理缓存数据
        String key = "dish_" + dishDTO.getCategoryId();
        clearCache(key);

        return Result.success();
    }

    @GetMapping("/page")
    @ApiOperation("菜品分页查询")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO) {
        log.info("菜品分页查询:{}",dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuery(dishPageQueryDTO);

        return Result.success(pageResult);
    }

    @DeleteMapping
    @ApiOperation("批量删除菜品")
    public Result delete(@RequestParam List<Long> ids) {
        log.info("菜品批量删除");
        dishService.deleteBatch(ids);

        // 清理缓存数据
        clearCache("dish_*");

        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询菜品")  // 用于修改菜品时数据回显
    public Result<DishVO> getById(@PathVariable Long id) {
        log.info("根据id查询菜品:{}",id);
        DishVO dishVO = dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }

    @PutMapping
    @ApiOperation("修改菜品")
    public Result update(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品:{}",dishDTO);
        dishService.updateWithFlavor(dishDTO);

        // 清理缓存数据
        clearCache("dish_*");

        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("菜品启售停售")
    public Result startOrStop(@PathVariable Integer status, Long id) {
        log.info("菜品启售停售:{}",status);
        dishService.startOrStop(status,id);

        // 清理缓存数据
        clearCache("dish_*");

        return Result.success();
    }

    @GetMapping("/list")
    @ApiOperation("根据分类id查询菜品")
    public Result<List<Dish>> list(Long categoryId) {
        List<Dish> list = dishService.list(categoryId);
        return Result.success(list);
    }

    private void clearCache(String pattern) {
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }
}
