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

    /**
     * 创建订单并生成支付单。
     *
     * @param shopCarEntity 购物车实体，包含商品信息和用户信息
     * @return PayOrderEntity 支付订单实体，包含支付链接、订单号等支付所需信息
     */
    @Override
    public PayOrderEntity createOrder(ShopCarEntity shopCarEntity) {

        // 1. 根据用户openid和商品ID，尝试查询是否存在未支付的订单
        String openid = shopCarEntity.getOpenid();
        Integer productId = shopCarEntity.getProductId();

        UnpaidOrderEntity unpaidOrderEntity = orderRepository.queryUnpaidOrder(shopCarEntity);

        // 2. 如果存在未支付的订单，并且该订单已经生成了支付链接，则直接返回这个订单的信息
        if (null != unpaidOrderEntity && PayStatusVo.WAIT.equals(unpaidOrderEntity.getPayStatus()) && null != unpaidOrderEntity.getPayUrl()) {
            log.info("创建订单-存在未支付的,但已生成微信支付的订单,返回 openid:{} orderId:{} payUrl:{}", openid, unpaidOrderEntity.getOrderId(), unpaidOrderEntity.getPayUrl());
            return PayOrderEntity.builder()
                    .openid(openid)
                    .payUrl(unpaidOrderEntity.getPayUrl())
                    .orderId(unpaidOrderEntity.getOrderId())
                    .payStatus(unpaidOrderEntity.getPayStatus())
                    .build();

        // 3. 如果存在未支付的订单，但还未生成支付链接，则为其生成支付链接
        } else if (null != unpaidOrderEntity && null == unpaidOrderEntity.getPayUrl()) {
            log.info("创建订单-存在未生成支付的订单，返回 openid:{},orderId:{}", openid, unpaidOrderEntity.getOrderId());
            PayOrderEntity payOrderEntity = this.doPrepayOrder(unpaidOrderEntity.getPayType().getCode(), openid, unpaidOrderEntity.getOrderId(), unpaidOrderEntity.getProductName(), unpaidOrderEntity.getTotalAmount());
            log.info("创建订单-完成,生成支付单，返回 openid:{},orderId:{} payUrl:{}", openid, unpaidOrderEntity.getOrderId(), payOrderEntity.getPayUrl());
            return payOrderEntity;

        }

        // 4. 如果不存在未支付的订单，则开始创建新的订单
        // 查询商品详细信息，确保商品有效
        ProductEntity productEntity = orderRepository.queryProduct(productId);
        if (!productEntity.isEnable()) {
            throw new ChatGPTException(Constants.ResponseCode.ORDER_PRODUCT_ERR.getCode(), Constants.ResponseCode.ORDER_PRODUCT_ERR.getInfo());
        }

        // 5. 保存新订单信息
        OrderEntity orderEntity = this.doSaveOrder(openid, shopCarEntity.getPayType(), productEntity);

        // 6. 为新订单生成支付单
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
