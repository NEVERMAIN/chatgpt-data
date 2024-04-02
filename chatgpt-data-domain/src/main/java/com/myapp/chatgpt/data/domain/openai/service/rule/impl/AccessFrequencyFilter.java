package com.myapp.chatgpt.data.domain.openai.service.rule.impl;

import com.google.common.cache.Cache;
import com.myapp.chatgpt.data.domain.openai.annotation.LogicStrategy;
import com.myapp.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import com.myapp.chatgpt.data.domain.openai.model.entity.RuleLogicEntity;
import com.myapp.chatgpt.data.domain.openai.model.entity.UserAccountQuotaEntity;
import com.myapp.chatgpt.data.domain.openai.model.vo.LogicTypeVO;
import com.myapp.chatgpt.data.domain.openai.service.rule.ILogicFilter;
import com.myapp.chatgpt.data.domain.openai.service.rule.factory.DefaultLogicFilterFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.RegEx;
import javax.annotation.Resource;
import javax.security.auth.callback.CallbackHandler;
import java.util.concurrent.ExecutionException;

/**
 * @description: 用户频率过滤器, 一分钟最多访问3次
 * @author: 云奇迹
 * @date: 2024/3/20
 */
@Component
@LogicStrategy(logicModel = DefaultLogicFilterFactory.LogicModel.ACCESS_FREQUENCY)
public class AccessFrequencyFilter implements ILogicFilter<UserAccountQuotaEntity> {

    /**
     * 最大访问频率
     */
    @Value("${app.config.maximum-access-frequency}")
    public Integer maxAccessCount;

    @Value("${app.config.white-list}")
    private String whiteList;
    /**
     * 过期时间为 3 分钟的缓存
     */
    @Resource
    private Cache<String, Integer> frequencytCache;

    @Override
    public RuleLogicEntity<ChatProcessAggregate> filter(ChatProcessAggregate process,UserAccountQuotaEntity accountQuotaEntity) {

        try {
            // 1. 放行白名单
            if (process.isWhiteList(whiteList)) {
                return RuleLogicEntity.<ChatProcessAggregate>builder()
                        .type(LogicTypeVO.SUCCESS)
                        .info(LogicTypeVO.SUCCESS.getInfo())
                        .data(process)
                        .build();
            }

            // 2. 获取 openId
            String openId = process.getOpenId();
            // 3.增加访问次数
            Integer accessCount = frequencytCache.get(openId, () -> 0);
            if(accessCount < maxAccessCount){
                frequencytCache.put(openId,accessCount+1);
                return RuleLogicEntity.<ChatProcessAggregate>builder()
                        .type(LogicTypeVO.SUCCESS)
                        .info(LogicTypeVO.SUCCESS.getInfo())
                        .data(process)
                        .build();
            }

            // 4.一分钟访问超过3次,拦截
            return RuleLogicEntity.<ChatProcessAggregate>builder()
                    .type(LogicTypeVO.REFUCE)
                    .info("你访问的频率太快了,请您耐心等待一分钟")
                    .build();


        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
