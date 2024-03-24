package com.myapp.chatgpt.data.domain.order.service.pay.impl;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.myapp.chatgpt.data.domain.order.model.entity.PayOrderEntity;
import com.myapp.chatgpt.data.domain.order.model.vo.PayStatusVo;
import com.myapp.chatgpt.data.domain.order.service.pay.IPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @description: 支付宝支付-沙箱测试
 * @author: 云奇迹
 * @date: 2024/3/24
 */
@Slf4j
@Service(value = "aliPay")
public class AliPayService implements IPayService {


    @Value("${alipay.notifyUrl}")
    private String notifyUrl;

    @Value("${alipay.returnUrl}")
    private String returnUrl;

    @Autowired(required = false)
    private AlipayClient alipayClient;

    @Override
    public PayOrderEntity doPrepayOrder(String openid, String orderId, String productName, BigDecimal totalAmount) {

        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(notifyUrl);
        request.setReturnUrl(returnUrl);

        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", orderId);
        bizContent.put("total_amount", totalAmount.toString());
        bizContent.put("subject", productName);
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");
        request.setBizContent(bizContent.toString());

        try {
            String form = alipayClient.pageExecute(request).getBody();

            PayOrderEntity payOrderEntity = PayOrderEntity.builder()
                    .openid(openid)
                    .orderId(orderId)
                    .payUrl(form)
                    .payStatus(PayStatusVo.WAIT)
                    .build();

            return payOrderEntity;

        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }

    }
}
