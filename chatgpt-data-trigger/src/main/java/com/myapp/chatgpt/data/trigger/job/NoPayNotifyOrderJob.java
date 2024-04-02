package com.myapp.chatgpt.data.trigger.job;

import com.google.common.eventbus.EventBus;
import com.myapp.chatgpt.data.domain.order.service.IOrderService;
import com.wechat.pay.java.service.payments.model.Transaction;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.payments.nativepay.model.QueryOrderByOutTradeNoRequest;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @description: 检测未接收或正确处理的支付回调通知  的任务
 * @author: 云奇迹
 * @date: 2024/3/23
 */

@Slf4j
@Component
public class NoPayNotifyOrderJob {

    @Resource
    private IOrderService orderService;

    @Autowired(required = false)
    private NativePayService nativePayService;

    @Resource
    private EventBus eventBus;

    @Resource
    private RTopic payOrderSuccessTopic;


    @Value("${wxpay.config.merchantId}")
    private String merchantId;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

    @Timed(value = "no_pay_notify_order_job", description = "定时任务，订单支付状态更新")
    @Scheduled(cron = "0 0/3 * * * ?")
    public void exec() {
        try {
            if (null == nativePayService) {
                log.info("定时任务，订单支付状态更新。应用未配置支付渠道，任务不执行。");
                return;
            }

            // 1.查询没有支付回调的订单
            List<String> orderIds = orderService.queryNoPayNotifyOrder();
            if(orderIds.isEmpty()){
                log.info("定时任务,订单支付状态更新,暂无未更新订单 orderId is null");
                return;
            }

            // todo: 如果是多支付渠道的情况下,应该根据返回的订单中的支付类型来选择补偿创建支付的方式
            for(String orderId : orderIds){
                // 查询结果
                QueryOrderByOutTradeNoRequest request = new QueryOrderByOutTradeNoRequest();
                request.setMchid(merchantId);
                request.setOutTradeNo(orderId);
                Transaction transaction = nativePayService.queryOrderByOutTradeNo(request);
                if (!Transaction.TradeStateEnum.SUCCESS.equals(transaction.getTradeState())) {
                    log.info("定时任务，订单支付状态更新，当前订单未支付 orderId is {}", orderId);
                    continue;
                }

                // 支付单号
                String transactionId = transaction.getTransactionId();
                Integer total = transaction.getAmount().getTotal();
                BigDecimal totalAmount = new BigDecimal(total).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
                String successTime = transaction.getSuccessTime();

                // 更新订单
                boolean success = orderService.changeOrderPaySuccess(orderId, transactionId, totalAmount, dateFormat.parse(successTime));
                if(success){
//                    eventBus.post(orderId);
                    payOrderSuccessTopic.publish(orderId);
                }
            }

        } catch (Exception e) {
            log.error("定时任务,订单支付状态更新失败");
        }

    }


}
