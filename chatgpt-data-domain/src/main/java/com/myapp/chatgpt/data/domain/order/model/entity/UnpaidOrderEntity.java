package com.myapp.chatgpt.data.domain.order.model.entity;

import com.myapp.chatgpt.data.domain.order.model.vo.PayStatusVo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @description: 未支付的订单对象
 * @author: 云奇迹
 * @date: 2024/3/22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnpaidOrderEntity {
    /**
     * 微信唯一Id
     */
    private String openid;
    /**
     * 订单状态
     */
    private String orderId;
    /**
     * 总金额
     */
    private BigDecimal totalAmount;
    /**
     * 支付二维码
     */
    private String payUrl;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 支付状态
     */
    private PayStatusVo payStatus;


}
