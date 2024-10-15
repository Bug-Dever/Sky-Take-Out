package com.OrderMate.service;

import com.OrderMate.dto.*;
import com.OrderMate.result.PageResult;
import com.OrderMate.vo.OrderPaymentVO;
import com.OrderMate.vo.OrderStatisticsVO;
import com.OrderMate.vo.OrderSubmitVO;
import com.OrderMate.vo.OrderVO;

/**
 * ClassName: OrderService
 * Package: com.OrderMate.service
 * Description:
 *
 * @Author Gush
 * @Create 2024-03-03 16:44
 */
public interface OrderService {
    OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO);

    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    PageResult pageQuery(int page, int pageSize, Integer status);

    OrderVO details(Long id);

    void cancel(Long id) throws Exception;

    void repetition(Long id);

    PageResult conditionSearch(OrdersPageQueryDTO ordersPageQueryDTO);

    OrderStatisticsVO statistics();

    void confirm(OrdersConfirmDTO ordersConfirmDTO);

    void rejection(OrdersRejectionDTO ordersRejectionDTO);

    void cancelOrder(OrdersCancelDTO ordersCancelDTO);

    void delivery(Long id);

    void complete(Long id);

    void reminder(Long id);
}
