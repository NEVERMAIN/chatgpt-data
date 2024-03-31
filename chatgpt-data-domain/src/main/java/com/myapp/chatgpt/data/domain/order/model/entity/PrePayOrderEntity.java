package com.myapp.chatgpt.data.domain.order.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @description: 创建支付的参数对象
 * @author: 云奇迹
 * @date: 2024/3/25
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrePayOrderEntity {
    /** 微信唯一ID */
    private String openid;
    /** 订单ID  */
    private String orderId;
    /** 产品名称 */
    private String productName;
    /** 总金额 */
    private BigDecimal totalAmount;
}
