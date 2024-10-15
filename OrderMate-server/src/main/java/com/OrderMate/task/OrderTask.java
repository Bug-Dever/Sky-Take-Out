package com.OrderMate.task;

import com.OrderMate.entity.Orders;
import com.OrderMate.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ClassName: MyTask
 * Package: com.OrderMate.task
 * Description:
 *
 * @Author Gush
 * @Create 2024-03-05 15:20
 */
@Component
@Slf4j
public class OrderTask {
    @Autowired
    private OrderMapper orderMapper;

    /*
    * 处理超时订单的方法
    * */
    @Scheduled(cron = "0 * * * * ?") // 每分钟触发一次
    public void processTimeoutOrder() {
        log.info("定时处理超时订单：{}", LocalDateTime.now());
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.PENDING_PAYMENT,LocalDateTime.now().minusMinutes(15));
        if(ordersList != null && ordersList.size() > 0) {
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.CANCELLED);
                orders.setCancelReason("支付超时，自动取消");
                orders.setCancelTime(LocalDateTime.now());
                orderMapper.update(orders);
            }
        }
    }

    /*
    * 处理一直处于派送中的订单
    * */
    @Scheduled(cron = "0 0 1 * * ?") // 每天凌晨1店触发一次
    public void processDeliveryOrder() {
        log.info("定时处理处于派送中的订单：{}", LocalDateTime.now());
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTimeLT(Orders.DELIVERY_IN_PROGRESS, LocalDateTime.now().minusHours(1));
        if(ordersList != null && ordersList.size() > 0) {
            for (Orders orders : ordersList) {
                orders.setStatus(Orders.COMPLETED);
                orders.setDeliveryTime(LocalDateTime.now());
            }
        }
    }
}
