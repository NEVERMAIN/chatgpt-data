package com.myapp.chatgpt.data.domain.order.service.pay.context;

import com.myapp.chatgpt.data.domain.order.model.entity.PrePayOrderEntity;

/**
 * @description: 抽象策略产品类
 * @author: 云奇迹
 * @date: 2024/3/25
 */
public abstract  class AbstractPayContext {

    public abstract String execute(PrePayOrderEntity prePayOrderEntity);

}
