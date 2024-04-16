package com.myapp.chatgpt.data.domain.order.service.pay.context;

import com.myapp.chatgpt.data.domain.order.model.entity.PrePayOrderEntity;

/**
 * @description: 上下文对象:关联抽象策略类，并调用策略类的方法
 * @author: 云奇迹
 * @date: 2024/3/25
 */
public abstract  class AbstractPayContext {

    /**
     * 执行预支付订单的操作。
     * 该方法是一个抽象方法，需要在具体实现类中定义其具体行为。
     *
     * @param prePayOrderEntity 预支付订单实体对象，包含订单的详细信息。
     * @return 返回执行操作后的结果，通常是一个字符串表示的成功或失败信息。
     */
    public abstract String execute(PrePayOrderEntity prePayOrderEntity);

}
