package com.myapp.chatgpt.data.domain.order.service.pay.strategy;

import com.myapp.chatgpt.data.domain.order.model.entity.PrePayOrderEntity;
import com.myapp.chatgpt.data.domain.order.service.pay.IPayStrategy;
import com.openicu.ltzf.payments.nativepay.NativePayService;
import com.openicu.ltzf.payments.nativepay.model.PrepayRequest;
import com.openicu.ltzf.payments.nativepay.model.PrepayResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @description:
 * @author: 云奇迹
 * @date: 2024/4/16
 */
@Service("nativePay")
public class NativePayStrategy implements IPayStrategy {

    @Autowired(required = false)
    private NativePayService nativePayService;

    @Value("${ltzf.sdk.config.merchantId}")
    private String merchantId;

    @Value("${ltzf.sdk.config.notifyUrl}")
    private String notifyUrl;

    @Override
    public String doPrepayOrder(PrePayOrderEntity prePayOrderEntity) {

        PrepayRequest prepayRequest = new PrepayRequest();
        prepayRequest.setMchId(merchantId);
        prepayRequest.setOutTradeNo(prePayOrderEntity.getOrderId());
        prepayRequest.setTotalFee(prePayOrderEntity.getTotalAmount().toString());
        prepayRequest.setBody(prePayOrderEntity.getProductName());
        prepayRequest.setNotifyUrl(notifyUrl);

        String codeUrl = "";
        try {
            PrepayResponse prepayResponse = nativePayService.prepay(prepayRequest);
            if(prepayResponse.getCode() == 0){
                codeUrl = prepayResponse.getData().getQRcodeUrl();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return codeUrl;
    }
}
