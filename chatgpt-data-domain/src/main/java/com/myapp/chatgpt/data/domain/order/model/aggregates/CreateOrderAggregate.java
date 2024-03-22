package com.myapp.chatgpt.data.domain.order.model.aggregates;

import com.myapp.chatgpt.data.domain.order.model.entity.OrderEntity;
import com.myapp.chatgpt.data.domain.order.model.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 创建订单的聚合对象
 * @author: 云奇迹
 * @date: 2024/3/22
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CreateOrderAggregate {
    /**
     * 订单Id
     */
    private String openid;
    /**
     * 产品对象
     */
    private ProductEntity product;
    /**
     * 订单对象
     */
    private OrderEntity order;


}
