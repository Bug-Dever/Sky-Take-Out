package com.OrderMate.mapper;

import com.OrderMate.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * ClassName: OrderDetailMapper
 * Package: com.sky.mapper
 * Description:
 *
 * @Author Gush
 * @Create 2024-03-03 16:48
 */
@Mapper
public interface OrderDetailMapper {

    // 批量插入订单明细数据
    void insertBatch(List<OrderDetail> orderDetailList);

    @Select("select * from order_detail where order_id = #{id}")
    List<OrderDetail> getByOrderId(Long id);
}
