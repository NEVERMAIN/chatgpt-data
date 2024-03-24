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
     * @param shopCarEntity
     * @return
     */
    UnpaidOrderEntity queryUnpaidOrder(ShopCarEntity shopCarEntity);

    /**
     * 查询商品详细信息
     * @param productId
     * @return
     */
    ProductEntity queryProduct(Integer productId);

    /**
     * 保存订单
     * @param createOrderAggregate
     * @return
     */
    void saveOrder(CreateOrderAggregate createOrderAggregate);

    /**
     * 更新订单的状态-创建支付二维码
     * @param payOrderEntity
     */
    void updateOrderPayInfo(PayOrderEntity payOrderEntity);

    /**
     * 更新订单的状态-修改为已支付状态
     * @return
     */
    boolean changeOrderPaySuccess(String orderId, String transactionId, BigDecimal payAmount, Date payTime);
    /**
     * 发货
     */
    void deliverGoods(String orderId);

    /**
     * 查询订单
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
     */
    List<String> queryNoPayNotifyOrder();

    /**
     * 查询超时关单的订单
     * @return
     */
    List<String> queryTimeOutCloseOrderList();

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
