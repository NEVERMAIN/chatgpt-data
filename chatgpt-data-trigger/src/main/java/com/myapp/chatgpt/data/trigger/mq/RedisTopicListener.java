package com.myapp.chatgpt.data.trigger.mq;


import com.myapp.chatgpt.data.domain.order.service.IOrderService;
import com.myapp.chatgpt.data.types.annotation.RedisTopic;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.listener.MessageListener;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @description: Redis 消息订阅监听器
 * @author: 云奇迹
 * @date: 2024/4/1
 */
@Slf4j
@Service
@RedisTopic(topic = "payOrderSuccessTopic")
public class RedisTopicListener implements MessageListener<String> {

    @Resource
    private IOrderService orderService;

    @Override
    public void onMessage(CharSequence channel, String msg) {
        log.info("Redis 发布/订阅 收到消息:{}",msg);
        try{
            log.info("支付完成，发货并记录,开始.订单号:{}",msg);
            orderService.deliverGoods(msg);
            log.info("支付完成,发货并记录,结束,订单号:{}",msg);
        }catch (Exception e){
            log.error("支付完成,发货并记录，失败，订单:{}",msg,e);
        }


    }
}
