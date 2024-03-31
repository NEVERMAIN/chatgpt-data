package com.myapp.chatgpt.data.domain.order.service;

import com.myapp.chatgpt.data.domain.order.model.aggregates.CreateOrderAggregate;
import com.myapp.chatgpt.data.domain.order.model.entity.OrderEntity;
import com.myapp.chatgpt.data.domain.order.model.entity.PayOrderEntity;
import com.myapp.chatgpt.data.domain.order.model.entity.PrePayOrderEntity;
import com.myapp.chatgpt.data.domain.order.model.entity.ProductEntity;
import com.myapp.chatgpt.data.domain.order.model.vo.OrderStatusVO;
import com.myapp.chatgpt.data.domain.order.model.vo.PayStatusVo;
import com.myapp.chatgpt.data.domain.order.model.vo.PayTypeVO;
import com.myapp.chatgpt.data.domain.order.repository.IOrderRepository;
import com.myapp.chatgpt.data.domain.order.service.AbstractOrderService;
import com.myapp.chatgpt.data.domain.order.service.pay.IPayStrategy;
import com.myapp.chatgpt.data.domain.order.service.pay.facde.PayFacade;
import com.myapp.chatgpt.data.domain.order.service.pay.factory.PayContextFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @description: 订单服务实现类
 * @author: 云奇迹
 * @date: 2024/3/22
 */
@Slf4j
@Service
public class OrderService extends AbstractOrderService {

    @Resource
    private IOrderRepository orderRepository;

    @Resource
    private PayFacade payFacade;


    @Override
    protected OrderEntity doSaveOrder(String openid, ProductEntity productEntity) {

        OrderEntity orderEntity = new OrderEntity();
        // 1. 生成订单唯一ID,保证数据库的幂等性
        orderEntity.setOrderId(RandomStringUtils.randomNumeric(12));
        orderEntity.setOrderTime(new Date());
        orderEntity.setOrderStatus(OrderStatusVO.CREATE);
        orderEntity.setPayType(PayTypeVO.ALIPAY);
        orderEntity.setTotalAmount(productEntity.getPrice());

        // 2. 聚合信息
        CreateOrderAggregate aggregate = CreateOrderAggregate.builder()
                .openid(openid)
                .product(productEntity)
                .order(orderEntity)
                .build();
        // 3. 保存订单
        orderRepository.saveOrder(aggregate);
        return orderEntity;
    }

    @Override
    protected PayOrderEntity doPrepayOrder(Integer payType, String openid, String orderId, String productName, BigDecimal totalAmount) {

        // 创建预支付的对象
        PrePayOrderEntity prePayOrder = PrePayOrderEntity.builder()
                .openid(openid)
                .orderId(orderId)
                .totalAmount(totalAmount)
                .productName(productName)
                .build();
        // 调用预支付服务,得到返回的结果
        String result = payFacade.pay(prePayOrder, payType);
        PayOrderEntity payOrderEntity = PayOrderEntity.builder()
                .openid(openid)
                .orderId(orderId)
                .payUrl(result)
                .payStatus(PayStatusVo.WAIT)
                .build();
        // 更新订单支付信息
        orderRepository.updateOrderPayInfo(payOrderEntity);
        return payOrderEntity;
    }

    @Override
    public boolean changeOrderPaySuccess(String orderId, String transactionId, BigDecimal totalAmount, Date payTime) {
        return orderRepository.changeOrderPaySuccess(orderId, transactionId, totalAmount, payTime);
    }

    @Override
    public CreateOrderAggregate queryOrder(String orderId) {
        return orderRepository.queryOrder(orderId);
    }

    @Override
    public void deliverGoods(String orderId) {
        orderRepository.deliverGoods(orderId);
    }

    @Override
    public List<String> queryReplenishmentOrder() {
        return orderRepository.queryReplenishmentOrder();
    }

    @Override
    public List<String> queryNoPayNotifyOrder() {
        return orderRepository.queryNoPayNotifyOrder();
    }

    @Override
    public List<String> queryTimeoutCloseOrderList() {
        return orderRepository.queryTimeOutCloseOrderList();
    }

    @Override
    public boolean changeOrderClose(String orderId) {
        return orderRepository.changeOrderClose(orderId);
    }

    @Override
    public List<ProductEntity> queryProductList() {
        return orderRepository.queryProductList();
    }
}
