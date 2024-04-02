package com.myapp.chatgpt.data.domain.order.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 购物车对象
 * @author: 云奇迹
 * @date: 2024/3/22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopCarEntity {
    /**
     * 微信唯一ID
     */
    private String openid;
    /**
     * 商品Id
     */
    private Integer productId;
    /**
     * 支付类型
     */
    private Integer payType;


}
