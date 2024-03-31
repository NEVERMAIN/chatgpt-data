package com.myapp.chatgpt.data.domain.order.service.pay.strategy;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.myapp.chatgpt.data.domain.order.model.entity.PayOrderEntity;
import com.myapp.chatgpt.data.domain.order.model.entity.PrePayOrderEntity;
import com.myapp.chatgpt.data.domain.order.model.vo.PayStatusVo;
import com.myapp.chatgpt.data.domain.order.service.pay.IPayStrategy;
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
public class AliPayStrategy implements IPayStrategy {

    @Value("${alipay.notifyUrl}")
    private String notifyUrl;

    @Value("${alipay.returnUrl}")
    private String returnUrl;

    @Autowired(required = false)
    private AlipayClient alipayClient;

    @Override
    public String doPrepayOrder(PrePayOrderEntity prePayOrderEntity) {

        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(notifyUrl);
        request.setReturnUrl(returnUrl);

        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", prePayOrderEntity.getOrderId());
        bizContent.put("total_amount", prePayOrderEntity.getTotalAmount().toString());
        bizContent.put("subject", prePayOrderEntity.getProductName());
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");
        request.setBizContent(bizContent.toString());

        try {
            String form = alipayClient.pageExecute(request).getBody();
            return form;

        } catch (AlipayApiException e) {
            throw new RuntimeException(e);
        }

    }
}
