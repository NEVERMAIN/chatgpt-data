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

    public static final Map<Integer, PayContext> payContexts = new ConcurrentHashMap<>();

    /** 初始化 Map 容器 */
    @PostConstruct
    public void init(){
        payContexts.put(PayTypeVO.WEIXIN_NATIVE.getCode(), new PayContext(weiXinNative));
        payContexts.put(PayTypeVO.ALIPAY.getCode(), new PayContext(aliPay));
    }


    @Override
    public PayContext getContext(Integer payType) {
        // 根据 payType 定义枚举类
        PayTypeVO payTypeVO = PayTypeVO.get(payType);
        if(payTypeVO == null){
            throw new RuntimeException("payType not supported");
        }
        // 从 Map 中获取 payContext
        return payContexts.get(payTypeVO.getCode());
    }
}
