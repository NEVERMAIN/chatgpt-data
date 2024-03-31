package com.myapp.chatgpt.data.domain.order.service.pay.context;

import com.myapp.chatgpt.data.domain.order.model.entity.PrePayOrderEntity;
import com.myapp.chatgpt.data.domain.order.service.pay.IPayStrategy;

/**
 * @description: 策略产品具体类
 * @author: 云奇迹
 * @date: 2024/3/25
 */
public class PayContext extends AbstractPayContext {
    private IPayStrategy payStrategy;

    public PayContext(IPayStrategy payStrategy){
        this.payStrategy = payStrategy;
    }

    @Override
    public String execute(PrePayOrderEntity prePayOrder) {
        return payStrategy.doPrepayOrder(prePayOrder);
    }
}
