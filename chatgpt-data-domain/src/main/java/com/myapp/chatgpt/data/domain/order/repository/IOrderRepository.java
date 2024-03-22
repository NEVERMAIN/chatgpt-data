package com.myapp.chatgpt.data.domain.order.repository;

import com.myapp.chatgpt.data.domain.order.model.aggregates.CreateOrderAggregate;
import com.myapp.chatgpt.data.domain.order.model.entity.*;

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
}
