package com.myapp.chatgpt.data.domain.order.model.entity;

import com.myapp.chatgpt.data.domain.order.model.vo.OrderStatusVO;
import com.myapp.chatgpt.data.domain.order.model.vo.PayStatusVo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description: 订单对象
 * @author: 云奇迹
 * @date: 2024/3/22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEntity {

    /**
     * 订单Id
     */
    private String orderId;
    /**
     * 下单时间
     */
    private Date orderTime;
    /**
     * 订单状态
     */
    private OrderStatusVO orderStatus;
    /**
     * 总金额
     */
    private BigDecimal totalAmount;
    /**
     * 支付状态
     */
    private PayStatusVo payStatus;

}
