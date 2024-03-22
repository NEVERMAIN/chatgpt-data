package com.myapp.chatgpt.data.domain.order.service;

import com.myapp.chatgpt.data.domain.order.model.entity.*;
import com.myapp.chatgpt.data.domain.order.model.vo.OrderStatusVO;
import com.myapp.chatgpt.data.domain.order.model.vo.PayStatusVo;
import com.myapp.chatgpt.data.domain.order.repository.IOrderRepository;
import com.myapp.chatgpt.data.types.common.Constants;
import com.myapp.chatgpt.data.types.exception.ChatGPTException;
import com.mysql.cj.x.protobuf.MysqlxCrud;
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


    @Resource
    private IOrderRepository orderRepository;

    @Override
    public PayOrderEntity createOrder(ShopCarEntity shopCarEntity) {
        // 0-基础信息
        String openid = shopCarEntity.getOpenid();
        Integer productId = shopCarEntity.getProductId();

        // 1. 查询是否有没有支付的订单
        UnpaidOrderEntity unpaidOrderEntity = orderRepository.queryUnpaidOrder(shopCarEntity);
        if (null != unpaidOrderEntity && PayStatusVo.WAIT.equals(unpaidOrderEntity.getPayStatus()) && null != unpaidOrderEntity.getPayUrl()) {
            log.info("创建订单-存在未支付的,已生成微信支付,返回 openid:{} orderId:{} payUrl:{}", openid, unpaidOrderEntity.getOrderId(), unpaidOrderEntity.getPayUrl());
            return PayOrderEntity.builder()
                    .openid(openid)
                    .payUrl(unpaidOrderEntity.getPayUrl())
                    .orderId(unpaidOrderEntity.getOrderId())
                    .payStatus(unpaidOrderEntity.getPayStatus())
                    .build();
        } else if (null != unpaidOrderEntity && null == unpaidOrderEntity.getPayUrl()) {
            log.info("创建订单-存在未支付，未生成支付的订单，返回 openid:{},orderId:{}", openid, unpaidOrderEntity.getOrderId());
            PayOrderEntity payOrderEntity = this.doPrepayOrder(openid, unpaidOrderEntity.getOrderId(), unpaidOrderEntity.getProductName(), unpaidOrderEntity.getTotalAmount());
            log.info("创建订单-完成，生成支付单，返回 openid:{},orderId:{} payUrl:{}", openid, unpaidOrderEntity.getOrderId(), payOrderEntity.getPayUrl());
            return payOrderEntity;
        }

        // 2. 创建订单
        // 2.1. 查询商品详细信息
        ProductEntity productEntity = orderRepository.queryProduct(productId);
        if (!productEntity.isEnable()) {
            throw new ChatGPTException(Constants.ResponseCode.ORDER_PRODUCT_ERR.getCode(), Constants.ResponseCode.ORDER_PRODUCT_ERR.getInfo());
        }

        // 3. 保存订单
        OrderEntity orderEntity = this.doSaveOrder(openid, productEntity);

        // 4. 创建支付
        PayOrderEntity payOrderEntity = this.doPrepayOrder(openid, orderEntity.getOrderId(), productEntity.getProductName(), orderEntity.getTotalAmount());
        log.info("创建订单-完成,生成支付单. openid:{},orderId:{},payUrl:{}", openid, orderEntity.getOrderId(), payOrderEntity.getPayUrl());
        return payOrderEntity;
    }

    /**
     * 保存订单
     *
     * @param openid
     * @param productEntity
     * @return
     */
    protected abstract OrderEntity doSaveOrder(String openid, ProductEntity productEntity);

    /**
     * 创建支付
     *
     * @param openid
     * @param orderId
     * @param productName
     * @param totalAmount
     * @return
     */
    protected abstract PayOrderEntity doPrepayOrder(String openid, String orderId, String productName, BigDecimal totalAmount);

}
