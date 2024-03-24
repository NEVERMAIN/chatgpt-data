package com.myapp.chatgpt.data.domain.order.service.pay;

import com.myapp.chatgpt.data.domain.order.model.entity.PayOrderEntity;

import java.math.BigDecimal;

/**
 * @description: 支付接口
 * @author: 云奇迹
 * @date: 2024/3/24
 */
public interface IPayService {

    /**
     * 创建支付的方法
     * @param openid
     * @param orderId
     * @param productName
     * @param totalAmount
     * @return
     */
    PayOrderEntity doPrepayOrder(String openid, String orderId, String productName, BigDecimal totalAmount);

}
