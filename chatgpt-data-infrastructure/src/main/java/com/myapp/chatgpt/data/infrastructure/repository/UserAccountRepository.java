package com.myapp.chatgpt.data.infrastructure.repository;

import java.util.Date;

import com.myapp.chatgpt.data.domain.account.model.entity.UserAccountQuotaEntity;
import com.myapp.chatgpt.data.domain.account.repository.IUserAccountRepository;
import com.myapp.chatgpt.data.infrastructure.dao.UserAccountDao;
import com.myapp.chatgpt.data.infrastructure.po.UserAccountQuotaPo;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @description: 用户账户仓储服务
 * @author: 云奇迹
 * @date: 2024/3/21
 */
@Repository
public class UserAccountRepository implements IUserAccountRepository {

    @Resource
    private UserAccountDao userAccountDao;

    @Override
    public Integer createUserAccount(UserAccountQuotaEntity accountQuotaEntity) {
        UserAccountQuotaPo userAccountQuotaPo = new UserAccountQuotaPo();
        userAccountQuotaPo.setOpenid(accountQuotaEntity.getOpenid());
        userAccountQuotaPo.setTotalQuota(accountQuotaEntity.getTotalQuota());
        userAccountQuotaPo.setSurplusQuota(accountQuotaEntity.getSurplusQuota());
        userAccountQuotaPo.setModelTypes(String.join(",", accountQuotaEntity.getAvailModes()));
        userAccountQuotaPo.setStatus(0);
        return userAccountDao.createAccount(userAccountQuotaPo);
    }

    @Override
    public Integer queryAccountIsExist(String openid) {
        return userAccountDao.count(openid);
    }
}
