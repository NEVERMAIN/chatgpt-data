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
     * 创建用户账户。
     * 该方法用于根据传入的账户聚合体创建一个新的用户账户实体。
     *
     * @param accountAggregate 用户账户聚合体，包含创建账户所需的所有信息。
     * @return 返回创建成功的用户账户实体。
     */
    UserAccountEntity createUserAccount(UserAccountAggregate accountAggregate);


    /**
     * 判断用户是否存在
     *
     * @param openid 用户的唯一标识符
     * @return 如果用户存在返回非零整数，否则返回零
     */
    Integer queryAccountIsExist(String openid);

}
