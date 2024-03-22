package com.myapp.chatgpt.data.infrastructure.repository;

import com.myapp.chatgpt.data.domain.openai.model.entity.UserAccountQuotaEntity;
import com.myapp.chatgpt.data.domain.openai.model.vo.UserAccountStatusVO;
import com.myapp.chatgpt.data.domain.openai.repository.IOpenAiRepository;
import com.myapp.chatgpt.data.infrastructure.dao.IUserAccountDao;
import com.myapp.chatgpt.data.infrastructure.po.UserAccountQuotaPo;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @description: OpenAI 领域的仓储服务
 * @author: 云奇迹
 * @date: 2024/3/20
 */
@Repository
public class OpenAiRepository implements IOpenAiRepository {

    @Resource
    private IUserAccountDao IUserAccountDao;


    @Override
    public UserAccountQuotaEntity query(String openid) {
        // 1. 从数据库中查询
        UserAccountQuotaPo accountQuotaPo = IUserAccountDao.query(openid);
        // 2.转成 entity 对象
        UserAccountQuotaEntity accountQuotaEntity = new UserAccountQuotaEntity();
        accountQuotaEntity.setOpenid(accountQuotaPo.getOpenid());
        accountQuotaEntity.setTotalQuota(accountQuotaPo.getTotalQuota());
        accountQuotaEntity.setSurplusQuota(accountQuotaPo.getSurplusQuota());
        accountQuotaEntity.genModelTypes(accountQuotaPo.getModelTypes());
        accountQuotaEntity.setStatus(UserAccountStatusVO.get(accountQuotaPo.getStatus()));

        return accountQuotaEntity;
    }

    @Override
    public Integer subAccountQuota(String openid) {
        return IUserAccountDao.subAccountQuota(openid);
    }


}
