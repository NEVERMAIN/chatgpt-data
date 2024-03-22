package com.myapp.chatgpt.data.domain.order.model.entity;

import com.myapp.chatgpt.data.types.enums.OpenAIProductEnableModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @description: 产品对象
 * @author: 云奇迹
 * @date: 2024/3/22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductEntity {

    /**
     * 产品Id
     */
    private Integer productId;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 产品描述
     */
    private String productDesc;
    /**
     * 额度
     */
    private Integer quota;
    /**
     * 价格
     */
    private BigDecimal price;
    /**
     * 是否可用
     */
    private OpenAIProductEnableModel enable;

    public boolean isEnable(){
        return OpenAIProductEnableModel.OPEN.equals(enable);
    }


}
