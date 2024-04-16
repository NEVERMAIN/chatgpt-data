package com.myapp.chatgpt.data;

import com.alibaba.fastjson.JSON;
import com.openicu.ltzf.payments.nativepay.NativePayService;
import com.openicu.ltzf.payments.nativepay.model.PrepayRequest;
import com.openicu.ltzf.payments.nativepay.model.PrepayResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @description:
 * @author: 云奇迹
 * @date: 2024/4/16
 */
@SpringBootTest
public class NativePayServiceTest {

    @Resource
    private NativePayService nativePayService;

    @Test
    public void test() throws IOException {

        PrepayRequest prepayRequest = new PrepayRequest();
        prepayRequest.setMchId("1674036351");
        prepayRequest.setOutTradeNo(RandomStringUtils.randomNumeric(8));
        prepayRequest.setTotalFee("0.01");
        prepayRequest.setBody("QQ公仔");
        prepayRequest.setNotifyUrl("http://lotterycore.nat300.top/api/v1/sale/ltzf/pay_notify");

        PrepayResponse prepayResponse = nativePayService.prepay(prepayRequest);

        System.out.println("请求参数: "+ JSON.toJSONString(prepayRequest));
        System.out.println("返回结果: "+JSON.toJSONString(prepayResponse));


    }
}
