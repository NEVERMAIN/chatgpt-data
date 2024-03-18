package com.myapp.chatgpt.data.domain.atuth.service;

import com.google.common.cache.Cache;
import com.myapp.chatgpt.data.domain.atuth.model.entity.AuthStateEntity;
import com.myapp.chatgpt.data.domain.atuth.model.vo.AuthTypeVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @description: 鉴权服务实现类
 * @author: 云奇迹
 * @date: 2024/3/18
 */
@Service
@Slf4j
public class AuthService  extends AbstractAuthService{

    @Resource
    private Cache<String,String> codeCache;

    @Override
    public AuthStateEntity checkIfExit(String code) {

        String openId = codeCache.getIfPresent(code);
        if(StringUtils.isBlank(openId)){
            return AuthStateEntity.builder()
                    .code(AuthTypeVo.NOT_EXIST.getCode())
                    .info(AuthTypeVo.NOT_EXIST.getInfo())
                    .build();
        }

        // 清楚缓存
        codeCache.invalidate(code);
        codeCache.invalidate(openId);

        return AuthStateEntity.builder()
                .openid(openId)
                .code(AuthTypeVo.SUCCESS.getCode())
                .info(AuthTypeVo.SUCCESS.getInfo())
                .build();
    }


    @Override
    public boolean checkToken(String token) {
        return isVerify(token);
    }
}
