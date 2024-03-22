package com.myapp.chatgpt.data.domain.order.model.entity;

import com.myapp.chatgpt.data.domain.order.model.vo.PayStatusVo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Stack;

/**
 * @description: 支付账单对象
 * @author: 云奇迹
 * @date: 2024/3/22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayOrderEntity {
    /**
     * 微信唯一ID
     */
    private String openid;
    /**
     * 订单ID
     */
    private String orderId;
    /**
     * 支付二维码
     */
    private String payUrl;
    /**
     * 支付状态
     */
    private PayStatusVo payStatus;


}
