package com.myapp.chatgpt.data.domain.order.service.pay.factory;

/**
 * @description: 抽象工厂类
 * @author: 云奇迹
 * @date: 2024/3/25
 */
public abstract class AbstractPayContextFactory<T> {

    /**
     * 获取策略
     * @param payType
     * @return
     */
    public abstract  T getContext(Integer payType);

}
