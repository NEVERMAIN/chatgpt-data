package com.myapp.chatgpt.data.domain.account.service;

import com.myapp.chatgpt.data.domain.account.model.aggregates.UserAccountAggregate;
import com.myapp.chatgpt.data.domain.account.model.entity.UserAccountEntity;

/**
 * @description: 用户账户服务
 * @author: 云奇迹
 * @date: 2024/3/21
 */
public interface IUserAccountService {

    /**
     * 创建账户
     * @param accountAggregate
     * @return
     */
    UserAccountEntity createUserAccount(UserAccountAggregate accountAggregate);

    /**
     * 判断用户是否存在
     */
    Integer queryAccountIsExist(String openid);
}
