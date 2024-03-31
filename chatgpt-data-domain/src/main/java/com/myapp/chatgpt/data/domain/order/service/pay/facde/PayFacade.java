package com.myapp.chatgpt.data.domain.order.service.pay.facde;

import com.myapp.chatgpt.data.domain.order.model.entity.PrePayOrderEntity;
import com.myapp.chatgpt.data.domain.order.service.pay.context.PayContext;
import com.myapp.chatgpt.data.domain.order.service.pay.factory.PayContextFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @description: 支付服务的门面
 * @author: 云奇迹
 * @date: 2024/3/25
 */
@Component
public class PayFacade {

    @Resource
    private PayContextFactory payContextFactory;

    public String pay(PrePayOrderEntity prePayOrderEntity, Integer payType) {
        // 获取 payContext 对象
        PayContext context = payContextFactory.getContext(payType);
        // 调用支付方法
        return context.execute(prePayOrderEntity);
    }

}
