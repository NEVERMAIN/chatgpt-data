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
import com.myapp.chatgpt.data.domain.order.service.pay.facde.PayFacade;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
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


    /**
     * 保存订单信息
     * @param openid 用户的唯一标识
     * @param payType 支付类型（当前未使用，后期接入支付功能时使用）
     * @param productEntity 商品实体，包含商品相关信息
     * @return 返回创建的订单实体
     */
    @Override
    protected OrderEntity doSaveOrder(String openid, Integer payType, ProductEntity productEntity) {

        OrderEntity orderEntity = new OrderEntity();
        // 生成订单唯一ID,保证数据库的幂等性
        orderEntity.setOrderId(RandomStringUtils.randomNumeric(12));
        orderEntity.setOrderTime(new Date());
        orderEntity.setOrderStatus(OrderStatusVO.CREATE);
        // 根据支付类型设置支付方式，当前默认为 扫码支付
        orderEntity.setPayType(PayTypeVO.get(payType));
        // 设置订单总金额
        orderEntity.setTotalAmount(productEntity.getPrice());

        // 聚合订单相关信息
        CreateOrderAggregate aggregate = CreateOrderAggregate.builder()
                .openid(openid)
                .product(productEntity)
                .order(orderEntity)
                .build();
        // 保存订单到数据库
        orderRepository.saveOrder(aggregate);
        return orderEntity;
    }


    /**
     * 创建预支付订单并进行预支付处理。
     *
     * @param payType 支付类型，例如：1代表支付宝，2代表微信支付。
     * @param openid 用户的支付账号标识。
     * @param orderId 订单的唯一标识。
     * @param productName 订单产品名称。
     * @param totalAmount 订单总金额。
     * @return PayOrderEntity 支付订单实体，包含了支付的详细信息。
     */
    @Override
    protected PayOrderEntity doPrepayOrder(Integer payType, String openid, String orderId, String productName, BigDecimal totalAmount) {
        log.info("创建预支付订单，支付类型：{}，订单ID：{}，订单金额：{}", payType, orderId, totalAmount);
        // 创建预支付订单对象，设置订单基本信息
        PrePayOrderEntity prePayOrder = PrePayOrderEntity.builder()
                .openid(openid)
                .orderId(orderId)
                .totalAmount(totalAmount)
                .productName(productName)
                .build();

        // 调用预支付服务，根据支付类型生成支付二维码
        String result = payFacade.pay(prePayOrder, payType);

        // 构建支付订单实体，设置支付URL和支付状态
        PayOrderEntity payOrderEntity = PayOrderEntity.builder()
                .openid(openid)
                .orderId(orderId)
                .payUrl(result)
                .payStatus(PayStatusVo.WAIT)
                .build();

        // 更新订单的支付信息
        orderRepository.updateOrderPayInfo(payOrderEntity);

        return payOrderEntity;
    }


    @Override
    public boolean changeOrderPaySuccess(String orderId, String transactionId, BigDecimal payAmount, Date payTime) {
        return orderRepository.changeOrderPaySuccess(orderId, transactionId, payAmount, payTime);
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
