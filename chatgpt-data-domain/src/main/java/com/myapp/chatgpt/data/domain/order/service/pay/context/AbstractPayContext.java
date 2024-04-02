package com.myapp.chatgpt.data.domain.order.service.pay.context;

import com.myapp.chatgpt.data.domain.order.model.entity.PrePayOrderEntity;

/**
 * @description: 上下文对象:关联抽象策略类，并调用策略类的方法
 * @author: 云奇迹
 * @date: 2024/3/25
 */
public abstract  class AbstractPayContext {

    public abstract String execute(PrePayOrderEntity prePayOrderEntity);

}
