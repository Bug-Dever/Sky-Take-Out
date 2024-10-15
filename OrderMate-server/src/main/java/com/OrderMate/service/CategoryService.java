package com.OrderMate.service;

import com.OrderMate.dto.CategoryDTO;
import com.OrderMate.dto.CategoryPageQueryDTO;
import com.OrderMate.entity.Category;
import com.OrderMate.result.PageResult;

import java.util.List;

/**
 * ClassName: CategoryService
 * Package: com.OrderMate.service
 * Description:
 *
 * @Author Gush
 * @Create 2024-02-19 10:59
 */
public interface CategoryService {
    // 新增分类
    void save(CategoryDTO categoryDTO);

    // 分类分页查询
    PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    // 启用禁用分类
    void startOrStop(Integer status, Long id);

    // 修改分类
    void update(CategoryDTO categoryDTO);

    // 根据id删除分类
    void deleteById(Long id);

    // 根据类型查询分类
    List<Category> list(Integer type);
}
