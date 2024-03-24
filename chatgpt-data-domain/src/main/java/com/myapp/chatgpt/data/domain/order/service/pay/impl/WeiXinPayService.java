package com.myapp.chatgpt.data.domain.order.service.pay.impl;

import com.myapp.chatgpt.data.domain.order.model.entity.PayOrderEntity;
import com.myapp.chatgpt.data.domain.order.model.vo.PayStatusVo;
import com.myapp.chatgpt.data.domain.order.repository.IOrderRepository;
import com.myapp.chatgpt.data.domain.order.service.pay.IPayService;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.payments.nativepay.model.Amount;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayRequest;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @description: 微信支付服务类
 * @author: 云奇迹
 * @date: 2024/3/24
 */
@Slf4j
@Service(value = "weiXinNative")
public class WeiXinPayService implements IPayService {

    @Value("${wxpay.config.appid}")
    private String appid;

    @Value("${wxpay.config.merchantId}")
    private String merchantId;

    @Value("${wxpay.config.notify-url}")
    private String notifyUrl;

    @Resource
    private IOrderRepository orderRepository;

    @Autowired(required = false)
    private NativePayService nativePayService;

    @Override
    public PayOrderEntity doPrepayOrder(String openid, String orderId, String productName, BigDecimal totalAmount) {
        // 1. 预支付处理创建请求
        PrepayRequest request = new PrepayRequest();
        Amount amount = new Amount();
        // 注意这里将totalAmount乘以100并转为整数，因为微信支付通常要求金额以分为单位(即元×100)
        amount.setTotal(totalAmount.multiply(new BigDecimal(100)).intValue());
        request.setAmount(amount);
        request.setAppid(appid);
        request.setMchid(merchantId);
        request.setNotifyUrl(notifyUrl);
        // 设置订单号
        request.setOutTradeNo(orderId);

        // 2. 创建微信支付单，如果你有多种支付方式，则可以根据支付类型的策略模式进行创建支付单
        String codeUrl = "";
        if (null != nativePayService) {
            PrepayResponse prepay = nativePayService.prepay(request);
            codeUrl = prepay.getCodeUrl();
        } else {
            codeUrl = "因未配置支付渠道,所以暂时不能生成支付URL";
        }
        PayOrderEntity payOrderEntity = PayOrderEntity.builder()
                .openid(openid)
                .orderId(orderId)
                .payUrl(codeUrl)
                .payStatus(PayStatusVo.WAIT)
                .build();

        return payOrderEntity;
    }
}
