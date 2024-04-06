package com.myapp.chatgpt.data.infrastructure.repository;

import com.myapp.chatgpt.data.domain.account.model.entity.UserAccountQuotaEntity;
import com.myapp.chatgpt.data.domain.atuth.repository.IAuthRepository;
import com.myapp.chatgpt.data.domain.openai.model.valobj.UserAccountStatusVO;
import com.myapp.chatgpt.data.infrastructure.dao.IUserAccountDao;
import com.myapp.chatgpt.data.infrastructure.po.UserAccountQuotaPo;
import com.myapp.chatgpt.data.infrastructure.redis.IRedisService;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

/**
 * @description:
 * @author: 云奇迹
 * @date: 2024/4/1
 */
@Repository
public class AuthRepository implements IAuthRepository {

    private static final String key = "wechat_code";

    @Resource
    private IRedisService redisService;

    @Resource
    private IUserAccountDao userAccountDao;


    @Override
    public String getCodeByOpenId(String code) {
        return redisService.getValue(key + "_" + code);
    }

    @Override
    public void removeCodeByOpenId(String code, String openId) {
        redisService.remove(key + "_" + code);
        redisService.remove(key + "_" + openId);
    }

    @Override
    public Integer queryUserAccountExist(String openid) {
        return userAccountDao.count(openid);
    }

    @Override
    public void createUserAccount(UserAccountQuotaEntity entity) {
        UserAccountQuotaPo userAccount = UserAccountQuotaPo.builder()
                .openid(entity.getOpenid())
                .status(UserAccountStatusVO.AVAILABLE.getCode())
                .modelTypes("glm-3-turbo,glm-4,glm-4v,cogview-3")
                .totalQuota(10)
                .surplusQuota(10)
                .build();

        userAccountDao.createAccount(userAccount);
    }
}
