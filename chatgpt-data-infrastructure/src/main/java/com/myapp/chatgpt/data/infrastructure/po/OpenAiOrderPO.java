package com.myapp.chatgpt.data.infrastructure.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description: 订单持久化对象
 * @author: 云奇迹
 * @date: 2024/3/22
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpenAiOrderPO {

    /**
     * 自增ID
     */
    private Integer id;
    /**
     * 用户ID:微信分配的唯一ID
     */
    private String openid;
    /**
     * 商品ID
     */
    private Integer productId;
    /**
     * 商品名称
     */
    private String productName;
    /**
     * 商品额度
     */
    private Integer productQuota;
    /**
     * 可用模型:glm-3-turbo,glm-4,glm-4v,cogview-3
     */
    private String productModelTypes;
    /**
     * 订单编号
     */
    private String orderId;
    /**
     * 下单时间
     */
    private Date orderTime;
    /**
     * 订单状态:0-创建完成 1-等待发货 2-发货完成 3-系统关单
     */
    private Integer orderStatus;
    /**
     * 订单金额
     */
    private BigDecimal totalAmount;
    /**
     * 支付方式:0-微信支付
     */
    private Integer payType;
    /**
     * 支付地址:创建支付后，获取 URL 地址
     */
    private String payUrl;
    /**
     * 支付金额:支付成功后,以回调信息更新金额
     */
    private BigDecimal payAmount;
    /**
     * 交易单号:支付成功后，回调信息的交易单号
     */
    private String transactionId;
    /**
     * 支付状态：0-等待支付、1-支付成功、2-支付失败、3-放弃支付
     */
    private Integer payStatus;
    /**
     * 支付时间
     */
    private Date payTime;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;


}
