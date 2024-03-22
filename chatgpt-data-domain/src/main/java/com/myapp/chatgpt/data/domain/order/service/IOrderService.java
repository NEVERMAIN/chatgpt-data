package com.myapp.chatgpt.data.domain.order.service;

import com.myapp.chatgpt.data.domain.order.model.aggregates.CreateOrderAggregate;
import com.myapp.chatgpt.data.domain.order.model.entity.PayOrderEntity;
import com.myapp.chatgpt.data.domain.order.model.entity.ShopCarEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description: 订单服务类接口
 * @author: 云奇迹
 * @date: 2024/3/22
 */
public interface IOrderService {

    /**
     * 创建订单
     *
     * @param shopCarEntity
     * @return
     */
    PayOrderEntity createOrder(ShopCarEntity shopCarEntity);

    /**
     * 修改订单状态:订单支付成功
     */
    boolean changeOrderPaySuccess(String orderId, String transactionId, BigDecimal totalAmount, Date payTime);

    /**
     * 查询订单信息
     */
    CreateOrderAggregate queryOrder(String orderId);

    /**
     * 订单商品发货
     */
    void deliverGoods(String orderId);


}
