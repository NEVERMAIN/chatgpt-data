package com.myapp.chatgpt.data.infrastructure.dao;

import com.myapp.chatgpt.data.infrastructure.po.OpenAiOrderPO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @description: 操作 openai_order 表的 dao 对象
 * @author: 云奇迹
 * @date: 2024/3/22
 */
@Mapper
public interface IOpenAiOrderDao {
    /**
     * 查询未支付的订单
     * @param openAiOrderPO
     * @return
     */
    OpenAiOrderPO queryUnpaidOrder(OpenAiOrderPO openAiOrderPO);

    /**
     * 保存订单
     */
    void insert(OpenAiOrderPO order);

    /**
     * 修改订单的状态
     * @param openAiOrderPOReq
     */
    void updateOrderPayInfo(OpenAiOrderPO openAiOrderPOReq);

    /**
     * 修改订单的状态 - 修改为支付成功
     * @param req
     * @return
     */
    Integer changeOrderPaySuccess(OpenAiOrderPO req);

    /**
     * 查询订单
     * @param orderId
     * @return
     */
    OpenAiOrderPO queryOrder(String orderId);

    /**
     * 修改订单的状态-发货状态
     * @param orderId
     * @return
     */
    Integer updateOrderStatusDeliverGoods(String orderId);
}
