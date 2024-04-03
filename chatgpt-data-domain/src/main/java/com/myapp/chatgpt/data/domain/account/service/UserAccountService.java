package com.myapp.chatgpt.data.domain.account.service;

import com.myapp.chatgpt.data.domain.account.model.entity.UserAccountEntity;
import com.myapp.chatgpt.data.domain.account.model.entity.UserAccountQuotaEntity;
import com.myapp.chatgpt.data.domain.account.model.vo.AccountStatusVO;
import com.myapp.chatgpt.data.domain.account.repository.IUserAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * @description: 用户账单领域服务
 * @author: 云奇迹
 * @date: 2024/3/21
 */
@Service
public class UserAccountService extends AbstractUserAccountService {

    @Resource
    private IUserAccountRepository userAccountRepository;

    @Override
    protected UserAccountEntity doCreateAccount(UserAccountQuotaEntity accountQuotaEntity) {
        // 2.如果不存在,再创建账户
        userAccountRepository.createUserAccount(accountQuotaEntity);
        // 3.返回正确结果
        return UserAccountEntity.builder()
                .code(AccountStatusVO.SUCCESS.getCode())
                .info(AccountStatusVO.SUCCESS.getInfo())
                .build();
    }

    @Override
    public Integer queryAccountIsExist(String openid) {
        return userAccountRepository.queryAccountIsExist(openid);
    }
}
