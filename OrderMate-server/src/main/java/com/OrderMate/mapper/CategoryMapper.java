package com.OrderMate.mapper;

import com.github.pagehelper.Page;
import com.OrderMate.annotation.AutoFill;
import com.OrderMate.dto.CategoryPageQueryDTO;
import com.OrderMate.entity.Category;
import com.OrderMate.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * ClassName: CategoryMapper
 * Package: com.sky.mapper
 * Description:
 *
 * @Author Gush
 * @Create 2024-02-19 11:02
 */
@Mapper
public interface CategoryMapper {

    @AutoFill(value = OperationType.INSERT)
    @Insert("insert into category (type, name, sort, status, create_time, update_time, create_user, update_user) VALUES " +
            "(#{type},#{name},#{sort},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void insert(Category category);

    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    @AutoFill(value = OperationType.UPDATE)
    void update(Category category);

    @Delete("delete from category where id = #{id}")
    void deleteById(Long id);

    List<Category> list(Integer type);
}
