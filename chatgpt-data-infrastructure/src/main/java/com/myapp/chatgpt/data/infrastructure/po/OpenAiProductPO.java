package com.myapp.chatgpt.data.infrastructure.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description: 产品持久化对象
 * @author: 云奇迹
 * @date: 2024/3/22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpenAiProductPO {
    /**
     * 自增ID
     */
    private Integer id;
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
     * 可用模型:glm-3-turbo,glm-4,glm-4v,cogview-3
     */
    private String productModelTypes;
    /**
     * 额度次数
     */
    private Integer quota;
    /**
     * 商品价格
     */
    private BigDecimal price;
    /**
     * 商品排序
     */
    private Integer sort;
    /**
     * 是否有效：0-无效 1-有效
     */
    private Integer isEnabled;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private  Date updateTime;

}
