package com.myapp.chatgpt.data.domain.openai.repository;

import com.myapp.chatgpt.data.domain.openai.model.entity.UserAccountQuotaEntity;

/**
 * @description: openAI 仓储服务接口
 * @author: 云奇迹
 * @date: 2024/3/20
 */
public interface IOpenAiRepository {

    /**
     * 查询用户账户
     */
    UserAccountQuotaEntity query(String openid);

    /**
     * 扣除库存
     */
    Integer subAccountQuota(String openid);



}
