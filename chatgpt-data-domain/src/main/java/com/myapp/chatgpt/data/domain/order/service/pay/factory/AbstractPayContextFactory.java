package com.myapp.chatgpt.data.domain.order.service.pay.factory;

/**
 * @description: 抽象工厂类
 * @author: 云奇迹
 * @date: 2024/3/25
 */
public abstract class AbstractPayContextFactory<T> {

    /**
     * 获取指定支付类型的策略对象。
     * 这是一个抽象方法，用于在具体实现中根据支付类型返回相应的策略对象。
     *
     * @param payType 支付类型，通常是一个整数值，代表不同的支付方式，如支付宝、微信支付等。
     * @return 返回一个策略对象，该对象的具体类型依赖于实现类。
     */
    public abstract T getContext(Integer payType);


}
