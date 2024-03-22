package com.myapp.chatgpt.data.trigger.http;

import com.google.common.eventbus.EventBus;
import com.myapp.chatgpt.data.domain.atuth.service.IAuthService;
import com.myapp.chatgpt.data.domain.order.service.IOrderService;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.core.notification.RequestParam;
import com.wechat.pay.java.service.partnerpayments.nativepay.model.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;

/**
 * @description: 销售服务
 * @author: 云奇迹
 * @date: 2024/3/22
 */
@RestController
@Slf4j
@RequestMapping("/api/${app.config.api-version}/sale/")
@CrossOrigin("*")
public class SaleController {

    @Autowired(required = false)
    private NotificationParser notificationParser;

    @Resource
    private IOrderService orderService;

    @Resource
    private IAuthService authService;

    @Resource
    private EventBus eventBus;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");


    /**
     * 支付回调
     *
     * @param requestBody
     * @param request
     * @param response
     */
    @PostMapping("pay_notify")
    public void payNotify(@RequestBody String requestBody, HttpServletRequest request, HttpServletResponse response) throws IOException {

        try {
            // 随机串
            String nonceStr = request.getHeader("Wechatpay-Nonce");
            // 微信传递过来的签名
            String signature = request.getHeader("Wechatpay-Signature");
            // 微信支付平台证书的序列号，验签必须使用序列号对应的微信支付平台证书。
            String serialNo = request.getHeader("Wechatpay-Serial");
            // 时间戳
            String timestamp = request.getHeader("Wechatpay-Timestamp");

            // 1. 构造 RequestParam
            RequestParam requestParam = new RequestParam.Builder()
                    .serialNumber(serialNo)
                    .nonce(nonceStr)
                    .signature(signature)
                    .timestamp(timestamp)
                    .body(requestBody)
                    .build();

            // 以支付通知回调为例,验签,解密,并转成 Transaction
            Transaction transaction = notificationParser.parse(requestParam, Transaction.class);

            Transaction.TradeStateEnum tradeState = transaction.getTradeState();
            if (Transaction.TradeStateEnum.SUCCESS.equals(tradeState)) {
                // 支付单号
                String orderId = transaction.getOutTradeNo();
                String transactionId = transaction.getTransactionId();
                Integer total = transaction.getAmount().getTotal();
                String successTime = transaction.getSuccessTime();
                log.info("支付成功 orderId:{} total:{} successTime:{}", orderId, total, successTime);
                // 更新订单
                boolean success = orderService.changeOrderPaySuccess(orderId, transactionId, new BigDecimal(total).divide(new BigDecimal(100), RoundingMode.HALF_UP), dateFormat.parse(successTime));
                if (success) {
                    // 发布消息
                    eventBus.post(orderId);
                }
                response.getWriter().write("<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>");
            } else {
                response.getWriter().write("<xml><return_code><![CDATA[FAIL]]></return_code></xml>");
            }
        } catch (Exception e) {
            log.error("支付失败", e);
            response.getWriter().write("<xml><return_code><![CDATA[FAIL]]></return_code></xml>");

        }


    }


}
