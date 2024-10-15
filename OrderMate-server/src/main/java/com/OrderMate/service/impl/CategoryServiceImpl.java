package com.OrderMate.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.OrderMate.constant.MessageConstant;
import com.OrderMate.constant.StatusConstant;
import com.OrderMate.dto.CategoryDTO;
import com.OrderMate.dto.CategoryPageQueryDTO;
import com.OrderMate.entity.Category;
import com.OrderMate.exception.DeletionNotAllowedException;
import com.OrderMate.mapper.CategoryMapper;
import com.OrderMate.mapper.DishMapper;
import com.OrderMate.mapper.SetmealMapper;
import com.OrderMate.result.PageResult;
import com.OrderMate.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ClassName: CategoryServiceImpl
 * Package: com.OrderMate.service.impl
 * Description:
 *
 * @Author Gush
 * @Create 2024-02-19 10:59
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    // 新增分类
    public void save(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO,category);
        category.setStatus(StatusConstant.DISABLE); // 默认为禁用状态
//        category.setCreateTime(LocalDateTime.now());
//        category.setCreateUser(BaseContext.getCurrentId());
//        category.setUpdateTime(LocalDateTime.now());
//        category.setUpdateUser(BaseContext.getCurrentId());

        categoryMapper.insert(category);
    }

    // 分类分页查询
    public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize());

        Page<Category> page = categoryMapper.pageQuery(categoryPageQueryDTO);
        long total = page.getTotal();
        List<Category> records = page.getResult();
        PageResult pageResult = new PageResult(total,records);
        return pageResult;
    }

    // 启用禁用分类
    public void startOrStop(Integer status, Long id) {
        Category category = Category.builder()
                .status(status)
                .id(id)
                .build();
        categoryMapper.update(category);
    }

    // 修改分类
    public void update(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO,category);
//        category.setUpdateTime(LocalDateTime.now());
//        category.setUpdateUser(BaseContext.getCurrentId());

        categoryMapper.update(category);
    }

    // 根据id删除分类
    public void deleteById(Long id) {
        // 查询当前分类是否关联了菜品或套餐，如果关联了就抛出业务异常
        Integer count = dishMapper.countByCategoryId(id);
        if(count > 0) {
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }
        count = setmealMapper.countByCategoryId(id);
        if(count > 0) {
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }

        categoryMapper.deleteById(id);
    }

    // 根据类型查询分类
    public List<Category> list(Integer type) {
        return categoryMapper.list(type);
    }
}
