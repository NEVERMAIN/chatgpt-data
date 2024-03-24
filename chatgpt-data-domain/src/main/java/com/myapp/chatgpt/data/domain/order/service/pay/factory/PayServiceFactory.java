package com.myapp.chatgpt.data.domain.order.service.pay.factory;

import com.myapp.chatgpt.data.domain.order.model.vo.PayTypeVO;
import com.myapp.chatgpt.data.domain.order.service.pay.IPayService;
import com.myapp.chatgpt.data.domain.order.service.pay.impl.WeiXinPayService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 支付服务的工厂类
 * @author: 云奇迹
 * @date: 2024/3/24
 */
@Component
public class PayServiceFactory {

    @Resource
    private IPayService weiXinNative;

    @Resource
    private IPayService aliPay;

    public static Map<Integer, IPayService> payServiceGroups = new HashMap<>();

    @PostConstruct
    public void  init() {
        payServiceGroups.put(PayTypeVO.WEIXIN_NATIVE.getCode(), weiXinNative);
        payServiceGroups.put(PayTypeVO.ALIPAY.getCode(), aliPay);
    }

    /**
     * 获取支付服务
     * @param type
     * @return
     */
    public IPayService getPayService(Integer type) {
        return payServiceGroups.get(type);

    }


}
