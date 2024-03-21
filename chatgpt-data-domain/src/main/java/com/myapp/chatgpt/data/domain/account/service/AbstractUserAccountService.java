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

    @Override
    public UserAccountEntity createUserAccount(UserAccountAggregate accountAggregate) {

        UserAccountQuotaEntity accountQuotaEntity = accountAggregate.getUserAccountQuotaEntity();
        // 1.先查询这个账户是否存在
        Integer i = userAccountRepository.queryAccountIsExist(accountQuotaEntity.getOpenid());
        if (i > 0) {
            return UserAccountEntity.builder()
                    .code(AccountStatusVO.ACCOUNT_EXIT.getCode())
                    .info(AccountStatusVO.ACCOUNT_EXIT.getInfo())
                    .build();
        }
        return this.doCreateAccount(accountQuotaEntity);
    }

    protected abstract UserAccountEntity doCreateAccount(UserAccountQuotaEntity accountQuotaEntity);
}
