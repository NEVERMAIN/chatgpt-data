package com.myapp.chatgpt.data.trigger.http.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @description: 商品对象 DTO
 * @author: 云奇迹
 * @date: 2024/3/23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleProductDTO {

    /**
     * 商品ID
     */
    private Integer productId;
    /**
     * 商品名称
     */
    private String productName;
    /**
     * 商品描述
     */
    private String productDesc;
    /**
     * 额度次数
     */
    private Integer quota;
    /**
     * 商品金额
     */
    private BigDecimal price;

}
