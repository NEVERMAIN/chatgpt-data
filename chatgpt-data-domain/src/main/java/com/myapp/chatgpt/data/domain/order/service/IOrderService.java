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
     * @param shopCarEntity 购物车
     * @return
     */
    PayOrderEntity createOrder(ShopCarEntity shopCarEntity);

    /**
     * 修改订单状态:订单支付成功
     * @param orderId 订单ID
     * @param transactionId 交易单号
     * @param payAmount 支付金额
     * @param payTime   支付时间
     * @return
     */
    boolean changeOrderPaySuccess(String orderId, String transactionId, BigDecimal payAmount, Date payTime);

    /**
     * 查询订单信息
     * @param orderId 订单ID
     * @return
     */
    CreateOrderAggregate queryOrder(String orderId);

    /**
     * 订单商品发货
     * @param orderId 订单ID
     */
    void deliverGoods(String orderId);

    /**
     * 查询待补货的订单
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
     * @param orderId 订单ID
     * @return
     */
    boolean changeOrderClose(String orderId);

    /**
     * 查询商品
     * @return
     */
    List<ProductEntity> queryProductList();
}
