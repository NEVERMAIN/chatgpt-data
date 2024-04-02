package com.myapp.chatgpt.data.domain.order.service;

import com.myapp.chatgpt.data.domain.order.model.entity.*;
import com.myapp.chatgpt.data.domain.order.model.vo.PayStatusVo;
import com.myapp.chatgpt.data.domain.order.repository.IOrderRepository;
import com.myapp.chatgpt.data.types.common.Constants;
import com.myapp.chatgpt.data.types.exception.ChatGPTException;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @description: 订单服务抽象类
 * @author: 云奇迹
 * @date: 2024/3/22
 */
@Slf4j
public abstract class AbstractOrderService implements IOrderService {

    /**
     * 订单仓储服务
     */
    @Resource
    private IOrderRepository orderRepository;

    @Override
    public PayOrderEntity createOrder(ShopCarEntity shopCarEntity) {
        // 0 - 基础信息
        String openid = shopCarEntity.getOpenid();
        Integer productId = shopCarEntity.getProductId();

        // 1. 查询是否有没有支付的订单
        UnpaidOrderEntity unpaidOrderEntity = orderRepository.queryUnpaidOrder(shopCarEntity);

        if (null != unpaidOrderEntity && PayStatusVo.WAIT.equals(unpaidOrderEntity.getPayStatus()) && null != unpaidOrderEntity.getPayUrl()) {
            log.info("创建订单-存在未支付的,但已生成微信支付的订单,返回 openid:{} orderId:{} payUrl:{}", openid, unpaidOrderEntity.getOrderId(), unpaidOrderEntity.getPayUrl());
            return PayOrderEntity.builder()
                    .openid(openid)
                    .payUrl(unpaidOrderEntity.getPayUrl())
                    .orderId(unpaidOrderEntity.getOrderId())
                    .payStatus(unpaidOrderEntity.getPayStatus())
                    .build();

        } else if (null != unpaidOrderEntity && null == unpaidOrderEntity.getPayUrl()) {
            log.info("创建订单-存在未生成支付的订单，返回 openid:{},orderId:{}", openid, unpaidOrderEntity.getOrderId());
            PayOrderEntity payOrderEntity = this.doPrepayOrder(unpaidOrderEntity.getPayType().getCode(), openid, unpaidOrderEntity.getOrderId(), unpaidOrderEntity.getProductName(), unpaidOrderEntity.getTotalAmount());
            log.info("创建订单-完成,生成支付单，返回 openid:{},orderId:{} payUrl:{}", openid, unpaidOrderEntity.getOrderId(), payOrderEntity.getPayUrl());
            return payOrderEntity;

        }

        // 2. 创建订单
        // 2.1. 查询商品详细信息
        ProductEntity productEntity = orderRepository.queryProduct(productId);
        if (!productEntity.isEnable()) {
            throw new ChatGPTException(Constants.ResponseCode.ORDER_PRODUCT_ERR.getCode(), Constants.ResponseCode.ORDER_PRODUCT_ERR.getInfo());
        }

        // 3. 保存订单
        // todo: 这里缺少一个支付类型字段,考虑用户选择不同支付类型支付
        OrderEntity orderEntity = this.doSaveOrder(openid, shopCarEntity.getPayType(), productEntity);

        // 4. 创建支付
        PayOrderEntity payOrderEntity = this.doPrepayOrder(orderEntity.getPayType().getCode(), openid, orderEntity.getOrderId(), productEntity.getProductName(), orderEntity.getTotalAmount());
        log.info("创建订单-完成,生成支付单. openid:{},orderId:{},payUrl:{}", openid, orderEntity.getOrderId(), payOrderEntity.getPayUrl());
        return payOrderEntity;
    }

    /**
     * 保存订单
     *
     * @param openid        微信ID
     * @param productEntity 商品对象
     * @return 订单对象
     */
    protected abstract OrderEntity doSaveOrder(String openid, Integer payType, ProductEntity productEntity);

    /**
     * 处理微信支付预中的预支付订单流程
     *
     * @param type        支付类型
     * @param openid      微信ID
     * @param orderId     订单ID
     * @param productName 产品名称
     * @param totalAmount 总金额
     * @return 支付订单对象
     */
    protected abstract PayOrderEntity doPrepayOrder(Integer type, String openid, String orderId, String productName, BigDecimal totalAmount);

}
