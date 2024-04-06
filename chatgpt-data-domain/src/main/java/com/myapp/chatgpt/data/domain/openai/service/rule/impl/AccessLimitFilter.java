package com.myapp.chatgpt.data.domain.openai.service.rule.impl;

import com.google.common.cache.Cache;
import com.myapp.chatgpt.data.domain.openai.annotation.LogicStrategy;
import com.myapp.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import com.myapp.chatgpt.data.domain.openai.model.entity.RuleLogicEntity;
import com.myapp.chatgpt.data.domain.openai.model.entity.UserAccountQuotaEntity;
import com.myapp.chatgpt.data.domain.openai.model.valobj.LogicTypeVO;
import com.myapp.chatgpt.data.domain.openai.service.rule.ILogicFilter;
import com.myapp.chatgpt.data.domain.openai.service.rule.factory.DefaultLogicFilterFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.ExecutionException;

/**
 * @description: 控制访问次数的规则
 * @author: 云奇迹
 * @date: 2024/3/19
 */
@Component
@LogicStrategy(logicModel = DefaultLogicFilterFactory.LogicModel.ACCESS_LIMIT)
public class AccessLimitFilter implements ILogicFilter<UserAccountQuotaEntity> {

    @Value("${app.config.white-list}")
    private String  whiteList;

    @Value("${app.config.limit-count}")
    private Integer limitCount;

    /**
     * 12 小时内,只能访问 10 次
     */
    @Resource
    private Cache<String,Integer> visitCache;

    @Override
    public RuleLogicEntity<ChatProcessAggregate> filter(ChatProcessAggregate process,UserAccountQuotaEntity accountQuotaEntity)  {

        try {
            // 1. 判断属不属于白名单
            if(process.isWhiteList(whiteList)){
                return RuleLogicEntity.<ChatProcessAggregate>builder().data(process)
                        .type(LogicTypeVO.SUCCESS)
                        .info(LogicTypeVO.SUCCESS.getInfo())
                        .data(process)
                        .build();
            }

            String openId = process.getOpenId();
            Integer visitCount = visitCache.get(openId, () -> 0);
            if(visitCount < limitCount){
                // 增加访问次数
                visitCache.put(openId,visitCount + 1);
                return RuleLogicEntity.<ChatProcessAggregate>builder()
                        .type(LogicTypeVO.SUCCESS)
                        .info(LogicTypeVO.SUCCESS.getInfo())
                        .data(process)
                        .build();
            }

            // 访问次数超过 10 次
            return RuleLogicEntity.<ChatProcessAggregate>builder()
                    .type(LogicTypeVO.REFUCE)
                    .info("你今天已经使用 "+limitCount +"次,超过最大访问次数")
                    .data(process)
                    .build();
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
