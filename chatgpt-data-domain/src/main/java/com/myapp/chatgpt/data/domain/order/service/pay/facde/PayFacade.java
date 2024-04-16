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

    /**
     * 执行支付操作的方法。
     * @param prePayOrderEntity 预支付订单实体，包含支付所需详细信息。
     * @param payType 支付类型，决定使用哪种支付方式。
     * @return 返回支付结果，通常为支付成功或失败的提示信息。
     */
    public String pay(PrePayOrderEntity prePayOrderEntity, Integer payType) {
        // 根据支付类型获取相应的支付上下文
        PayContext context = payContextFactory.getContext(payType);
        // 使用获取的支付上下文执行支付操作
        return context.execute(prePayOrderEntity);
    }


}
