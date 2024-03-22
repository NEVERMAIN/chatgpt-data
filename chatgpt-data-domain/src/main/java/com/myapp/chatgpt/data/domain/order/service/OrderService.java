package com.myapp.chatgpt.data.domain.order.service;

import com.myapp.chatgpt.data.domain.order.model.aggregates.CreateOrderAggregate;
import com.myapp.chatgpt.data.domain.order.model.entity.OrderEntity;
import com.myapp.chatgpt.data.domain.order.model.entity.PayOrderEntity;
import com.myapp.chatgpt.data.domain.order.model.entity.ProductEntity;
import com.myapp.chatgpt.data.domain.order.model.vo.OrderStatusVO;
import com.myapp.chatgpt.data.domain.order.model.vo.PayStatusVo;
import com.myapp.chatgpt.data.domain.order.model.vo.PayTypeVO;
import com.myapp.chatgpt.data.domain.order.repository.IOrderRepository;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.payments.nativepay.model.Amount;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayRequest;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @description: 订单服务实现类
 * @author: 云奇迹
 * @date: 2024/3/22
 */
@Slf4j
@Service
public class OrderService extends AbstractOrderService {

    @Value("${wxpay.config.appid}")
    private String appid;

    @Value("${wxpay.config.merchantId}")
    private String merchantId;

    @Value("${wxpay.config.notify-url}")
    private String notifyUrl;

    @Resource
    private IOrderRepository orderRepository;

    @Resource
    private NativePayService nativePayService;


    @Override
    protected OrderEntity doSaveOrder(String openid, ProductEntity productEntity) {
        OrderEntity orderEntity = new OrderEntity();
        // 生成订单ID,保证数据库的幂等性
        orderEntity.setOrderId(RandomStringUtils.randomNumeric(12));
        orderEntity.setOrderTime(new Date());
        orderEntity.setOrderStatus(OrderStatusVO.CREATE);
        orderEntity.setPayType(PayTypeVO.WEIXIN_NATIVE);
        orderEntity.setTotalAmount(productEntity.getPrice());

        // 聚合信息
        CreateOrderAggregate aggregate = CreateOrderAggregate.builder()
                .openid(openid)
                .product(productEntity)
                .order(orderEntity)
                .build();

        orderRepository.saveOrder(aggregate);
        return orderEntity;
    }

    @Override
    protected PayOrderEntity doPrepayOrder(String openid, String orderId, String productName, BigDecimal totalAmount) {

        // 1. 创建请求
        PrepayRequest request = new PrepayRequest();
        Amount amount = new Amount();
        amount.setTotal(totalAmount.multiply(new BigDecimal(100)).intValue());
        request.setAmount(amount);
        request.setAppid(appid);
        request.setMchid(merchantId);
        request.setNotifyUrl(notifyUrl);
        request.setOutTradeNo(orderId);

        // 创建微信支付单，如果你有多种支付方式，则可以根据支付类型的策略模式进行创建支付单
        PrepayResponse prepay = nativePayService.prepay(request);
        PayOrderEntity payOrderEntity = PayOrderEntity.builder()
                .openid(openid)
                .orderId(orderId)
                .payUrl(prepay.getCodeUrl())
                .payStatus(PayStatusVo.WAIT)
                .build();

        // 更新订单支付信息
        orderRepository.updateOrderPayInfo(payOrderEntity);

        return payOrderEntity;


    }
}
