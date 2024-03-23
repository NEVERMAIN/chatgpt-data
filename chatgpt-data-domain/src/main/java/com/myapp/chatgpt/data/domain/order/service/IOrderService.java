package com.myapp.chatgpt.data.domain.order.service;

import com.myapp.chatgpt.data.domain.order.model.aggregates.CreateOrderAggregate;
import com.myapp.chatgpt.data.domain.order.model.entity.OrderEntity;
import com.myapp.chatgpt.data.domain.order.model.entity.PayOrderEntity;
import com.myapp.chatgpt.data.domain.order.model.entity.ProductEntity;
import com.myapp.chatgpt.data.domain.order.model.entity.ShopCarEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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

    /**
     * 查询待补货的订单
     *
     * @return
     */
    List<String> queryReplenishmentOrder();

    /**
     * 查询没有支付回调的订单
     * @return
     */
    List<String> queryNoPayNotifyOrder();

    /**
     * 查询要超时关单的订单
     * @return
     */
    List<String> queryTimeoutCloseOrderList();

    /**
     * 更新订单的状态-修改为订单取消
     * @param orderId
     * @return
     */
    boolean changeOrderClose(String orderId);

    /**
     * 查询商品
     * @return
     */
    List<ProductEntity> queryProductList();
}
