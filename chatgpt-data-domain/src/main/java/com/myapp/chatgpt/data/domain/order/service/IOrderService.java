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
     * @param shopCarEntity 购物车实体，包含选择的商品和其他订单信息
     * @return 返回创建的订单实体
     */
    PayOrderEntity createOrder(ShopCarEntity shopCarEntity);

    /**
     * 修改订单状态为支付成功
     * @param orderId 订单ID，用于标识需要修改状态的订单
     * @param transactionId 交易单号，证明支付已经成功
     * @param payAmount 支付金额，记录实际支付的金额
     * @param payTime 支付时间，记录支付的具体时间
     * @return 返回布尔值，表示订单状态是否成功修改为支付成功
     */
    boolean changeOrderPaySuccess(String orderId, String transactionId, BigDecimal payAmount, Date payTime);

    /**
     * 查询订单信息
     * @param orderId 订单ID，用于查询特定订单的信息
     * @return 返回订单聚合实体，包含订单及其相关联的信息
     */
    CreateOrderAggregate queryOrder(String orderId);

    /**
     * 订单商品发货操作
     * @param orderId 订单ID，标记需要发货的订单
     */
    void deliverGoods(String orderId);

    /**
     * 查询需要补货的订单
     * @return 返回订单ID列表，包含需要补货的订单
     */
    List<String> queryReplenishmentOrder();

    /**
     * 查询没有支付回调的订单
     * @return 返回订单ID列表，包含未收到支付回调的订单
     */
    List<String> queryNoPayNotifyOrder();

    /**
     * 查询即将超时需要关闭的订单
     * @return 返回订单ID列表，包含即将超时的订单
     */
    List<String> queryTimeoutCloseOrderList();

    /**
     * 更新订单状态为已取消
     * @param orderId 订单ID，标识需要取消的订单
     * @return 返回布尔值，表示订单状态是否成功修改为已取消
     */
    boolean changeOrderClose(String orderId);

    /**
     * 查询所有商品信息
     * @return 返回商品实体列表，包含所有商品的信息
     */
    List<ProductEntity> queryProductList();

}
