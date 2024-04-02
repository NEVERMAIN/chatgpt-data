package com.myapp.chatgpt.data.domain.order.service.pay;

import com.myapp.chatgpt.data.domain.order.model.entity.PrePayOrderEntity;


/**
 * @description: 支付接口 -- 抽象策略角色
 * @author: 云奇迹
 * @date: 2024/3/24
 */
public interface IPayStrategy {

    /**
     * 创建支付的方法
     * @return 微信支付返回二维码
     */
    String doPrepayOrder(PrePayOrderEntity prePayOrderEntity);



}
