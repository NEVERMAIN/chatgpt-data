package com.myapp.chatgpt.data.domain.order.service;

import com.myapp.chatgpt.data.domain.order.model.entity.PayOrderEntity;
import com.myapp.chatgpt.data.domain.order.model.entity.ShopCarEntity;

/**
 * @description: 订单服务类接口
 * @author: 云奇迹
 * @date: 2024/3/22
 */
public interface IOrderService {

    /**
     * 创建订单
     * @param shopCarEntity
     * @return
     */
    PayOrderEntity createOrder(ShopCarEntity shopCarEntity);
}
