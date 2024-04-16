package com.myapp.chatgpt.data.domain.order.service.pay.factory;

import com.myapp.chatgpt.data.domain.order.model.vo.PayTypeVO;
import com.myapp.chatgpt.data.domain.order.service.pay.IPayStrategy;
import com.myapp.chatgpt.data.domain.order.service.pay.context.PayContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: 支付服务的工厂类
 * @author: 云奇迹
 * @date: 2024/3/24
 */
@Component
public class PayContextFactory extends AbstractPayContextFactory<PayContext>{

    @Resource
    private IPayStrategy weiXinNative;

    @Resource
    private IPayStrategy aliPay;

    @Resource
    private IPayStrategy nativePay;

    public static final Map<Integer, PayContext> PAY_CONTEXTS = new ConcurrentHashMap<>();

    /**
     * 初始化 Map 容器
     * 该方法没有参数和返回值，它主要用于在类的实例化后，初始化一个特定的Map容器。
     * 在这个方法中，将不同的支付类型与相应的支付上下文映射起来。
     */
    @PostConstruct
    public void init(){
        // 将微信支付和其支付上下文映射到PAY_CONTEXTS容器中
        PAY_CONTEXTS.put(PayTypeVO.WEIXIN_NATIVE.getCode(), new PayContext(weiXinNative));
        // 将支付宝支付和其支付上下文映射到PAY_CONTEXTS容器中
        PAY_CONTEXTS.put(PayTypeVO.ALIPAY.getCode(), new PayContext(aliPay));
        // 注入 扫码支付
        PAY_CONTEXTS.put(PayTypeVO.NATIVE.getCode(), new PayContext(nativePay));
    }


    /**
     * 根据支付类型获取支付上下文
     * @param payType 支付类型，用于确定具体的支付方式
     * @return PayContext 支付上下文对象，包含了与支付相关的所有信息
     * @throws RuntimeException 如果传入的支付类型不被支持，则抛出异常
     */
    @Override
    public PayContext getContext(Integer payType) {
        // 根据 payType 获取对应的 PayTypeVO 实例
        PayTypeVO payTypeVO = PayTypeVO.get(payType);
        if(payTypeVO == null){
            // 如果找不到对应的 PayTypeVO 实例，抛出异常
            throw new RuntimeException("payType not supported");
        }
        // 通过 PayTypeVO 的代码从 Map 中获取对应的 PayContext
        return PAY_CONTEXTS.get(payTypeVO.getCode());
    }

}
