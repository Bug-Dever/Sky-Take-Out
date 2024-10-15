package com.OrderMate.mapper;

import com.OrderMate.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * ClassName: UserMapper
 * Package: com.sky.mapper
 * Description:
 *
 * @Author Gush
 * @Create 2024-02-26 15:58
 */
@Mapper
public interface UserMapper {

    /*
    * 根据openid查询用户
    * */
    @Select("select * from user where openid = #{openid}")
    User getByOpenid(String openid);

    /*
    * 插入一条数据
    * */
    void insert(User user);

    @Select("select * from user where id = #{userId}")
    User getById(Long userId);
}
