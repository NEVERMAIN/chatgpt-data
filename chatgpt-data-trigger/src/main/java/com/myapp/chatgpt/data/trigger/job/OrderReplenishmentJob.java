package com.myapp.chatgpt.data.trigger.job;

import com.google.common.eventbus.EventBus;
import com.myapp.chatgpt.data.domain.order.service.IOrderService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RTopic;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description: 订单补货任务
 * @author: 云奇迹
 * @date: 2024/3/23
 */
@Slf4j
@Component
public class OrderReplenishmentJob {

    @Resource
    private IOrderService orderService;

    @Resource
    private EventBus eventBus;

    @Resource
    private RTopic payOrderSuccessTopic;

    /**
     * 执行订单补货，超时 1 分钟，已支付，待发货未发货的订单
     */
    @Scheduled(cron = "0 0/10 * * * ?")
    public void exec() {
        try {
            List<String> orderIds = orderService.queryReplenishmentOrder();
            if (orderIds.isEmpty()) {
                log.info("定时任务,订单补货不存在,查询 orderIds is null");
            }
            for (String orderId : orderIds) {
                log.info("定时任务,订单补货开始，orderId:{}", orderId);
                payOrderSuccessTopic.publish(orderId);
            }

        } catch (Exception e) {
            log.error("定时任务,订单补货失败", e);
        }
    }

}
