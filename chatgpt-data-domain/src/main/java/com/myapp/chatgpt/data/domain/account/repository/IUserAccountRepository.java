package com.myapp.chatgpt.data.domain.account.repository;

import com.myapp.chatgpt.data.domain.account.model.entity.UserAccountQuotaEntity;

/**
 * @description: 用户账户仓储服务
 * @author: 云奇迹
 * @date: 2024/3/21
 */
public interface IUserAccountRepository {

    /**
     * 创建账户
     * @param accountQuotaEntity
     * @return
     */
    Integer createUserAccount(UserAccountQuotaEntity accountQuotaEntity);

    /**
     * 查询账户是否存在
     */
    Integer queryAccountIsExist(String openid);
}
