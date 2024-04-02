package com.myapp.chatgpt.data.domain.order.repository;

import com.myapp.chatgpt.data.domain.order.model.aggregates.CreateOrderAggregate;
import com.myapp.chatgpt.data.domain.order.model.entity.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @description: 订单服务仓储对象
 * @author: 云奇迹
 * @date: 2024/3/22
 */
public interface IOrderRepository {

    /**
     * 查询没有支付的订单
     * @param shopCarEntity 购物车对象
     * @return
     */
    UnpaidOrderEntity queryUnpaidOrder(ShopCarEntity shopCarEntity);

    /**
     * 查询商品详细信息
     * @param productId 商品ID
     * @return
     */
    ProductEntity queryProduct(Integer productId);

    /**
     * 保存订单
     * @param createOrderAggregate 创建订单的聚合对象
     * @return
     */
    void saveOrder(CreateOrderAggregate createOrderAggregate);

    /**
     * 更新订单的状态-创建支付二维码
     * @param payOrderEntity 支付订单对象
     */
    void updateOrderPayInfo(PayOrderEntity payOrderEntity);

    /**
     * 更新订单的状态-修改为已支付状态
     * @param orderId 订单Id
     * @param transactionId 交易单号
     * @param payAmount 支付金额
     * @param payTime   支付时间
     * @return
     */
    boolean changeOrderPaySuccess(String orderId, String transactionId, BigDecimal payAmount, Date payTime);
    /**
     * 发货
     * @param orderId 订单ID
     */
    void deliverGoods(String orderId);

    /**
     * 查询订单
     * @param orderId 订单ID
     * @return
     */
    CreateOrderAggregate queryOrder(String orderId);

    /**
     * 查询待补货订单
     * @return
     */
    List<String> queryReplenishmentOrder();

    /**
     * 查询没有支付回调的订单
     * @return 返回订单ID集合
     */
    List<String> queryNoPayNotifyOrder();

    /**
     * 查询超时关单的订单
     * @return 返回订单ID集合
     */
    List<String> queryTimeOutCloseOrderList();

    /**
     * 更新订单的状态-修改为订单取消
     * @param orderId 订单ID
     * @return
     */
    boolean changeOrderClose(String orderId);

    /**
     * 查询商品
     * @return 所有商品的集合
     */
    List<ProductEntity> queryProductList();

}
