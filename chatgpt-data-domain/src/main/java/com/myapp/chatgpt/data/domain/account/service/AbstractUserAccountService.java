package com.myapp.chatgpt.data.domain.account.service;

import com.myapp.chatgpt.data.domain.account.model.aggregates.UserAccountAggregate;
import com.myapp.chatgpt.data.domain.account.model.entity.UserAccountEntity;
import com.myapp.chatgpt.data.domain.account.model.entity.UserAccountQuotaEntity;
import com.myapp.chatgpt.data.domain.account.model.vo.AccountStatusVO;
import com.myapp.chatgpt.data.domain.account.repository.IUserAccountRepository;

import javax.annotation.Resource;

/**
 * @description: 用户账户抽象类
 * @author: 云奇迹
 * @date: 2024/3/21
 */
public abstract class AbstractUserAccountService implements IUserAccountService {

    @Resource
    private IUserAccountRepository userAccountRepository;

    /**
     * 创建用户账户。
     *
     * @param accountAggregate 账户聚合根，包含账户的全部信息。
     * @return 返回创建的用户账户实体。如果账户已存在，则返回一个表示账户已存在的错误信息实体。
     */
    @Override
    public UserAccountEntity createUserAccount(UserAccountAggregate accountAggregate) {

        UserAccountQuotaEntity accountQuotaEntity = accountAggregate.getUserAccountQuotaEntity();
        // 1. 查询该账户是否已存在
        Integer i = userAccountRepository.queryAccountIsExist(accountQuotaEntity.getOpenid());
        if (i > 0) {
            // 如果账户已存在，则返回账户已存在的错误信息
            return UserAccountEntity.builder()
                    .code(AccountStatusVO.ACCOUNT_EXIT.getCode())
                    .info(AccountStatusVO.ACCOUNT_EXIT.getInfo())
                    .build();
        }

        // 账户不存在时，执行账户创建逻辑
        return this.doCreateAccount(accountQuotaEntity);
    }


    /**
     * 创建账户
     * @param accountQuotaEntity 账户额度实体对象
     * @return
     */
    protected abstract UserAccountEntity doCreateAccount(UserAccountQuotaEntity accountQuotaEntity);
}
