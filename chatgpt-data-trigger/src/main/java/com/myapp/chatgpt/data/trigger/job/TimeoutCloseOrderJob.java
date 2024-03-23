package com.myapp.chatgpt.data.trigger.job;


import com.myapp.chatgpt.data.domain.order.service.IOrderService;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.payments.nativepay.model.CloseOrderRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description: 超时关单任务
 * @author: 云奇迹
 * @date: 2024/3/23
 */
@Component
@Slf4j
public class TimeoutCloseOrderJob {

    @Resource
    private IOrderService orderService;

    @Autowired(required = false)
    private NativePayService nativePayService;

    @Value("${wxpay.config.merchantId}")
    private String merchantId;

    @Scheduled(cron = "0 0/10 * * * ?")
    public void exec() {
        try {
            if (null == nativePayService) {
                log.info("定时任务，订单支付状态更新。应用未配置支付渠道，任务不执行。");
                return;
            }

            List<String> orderIds = orderService.queryTimeoutCloseOrderList();
            if (orderIds.isEmpty()) {
                log.info("定时任务,超时30分钟订单关闭,暂无超时支付订单 orderIds is null");
                return;
            }
            for (String orderId : orderIds) {
                boolean status = orderService.changeOrderClose(orderId);
                // 微信关单,暂时不需要主动关单
                CloseOrderRequest request = new CloseOrderRequest();
                request.setMchid(merchantId);
                request.setOutTradeNo(orderId);
                nativePayService.closeOrder(request);

                log.info("定时任务，超时30分钟订单关闭 orderId: {} status：{}", orderId, status);
            }
        } catch (Exception e) {
            log.error("定时任务，超时30分钟订单关闭失败", e);
        }
    }
}
