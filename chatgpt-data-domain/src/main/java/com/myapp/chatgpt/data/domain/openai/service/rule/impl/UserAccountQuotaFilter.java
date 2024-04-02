package com.myapp.chatgpt.data.domain.openai.service.rule.impl;

import com.myapp.chatgpt.data.domain.openai.annotation.LogicStrategy;
import com.myapp.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import com.myapp.chatgpt.data.domain.openai.model.entity.RuleLogicEntity;
import com.myapp.chatgpt.data.domain.openai.model.entity.UserAccountQuotaEntity;
import com.myapp.chatgpt.data.domain.openai.model.vo.LogicTypeVO;
import com.myapp.chatgpt.data.domain.openai.repository.IOpenAiRepository;
import com.myapp.chatgpt.data.domain.openai.service.rule.ILogicFilter;
import com.myapp.chatgpt.data.domain.openai.service.rule.factory.DefaultLogicFilterFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @description: 用户账户余额过滤
 * @author: 云奇迹
 * @date: 2024/3/21
 */
@Component
@LogicStrategy(logicModel = DefaultLogicFilterFactory.LogicModel.USER_ACCOUNT_QUOTA)
public class UserAccountQuotaFilter implements ILogicFilter<UserAccountQuotaEntity> {

    @Resource
    private IOpenAiRepository openAiRepository;

    @Override
    public RuleLogicEntity<ChatProcessAggregate> filter(ChatProcessAggregate process, UserAccountQuotaEntity data) {

        String openid = data.getOpenid();
        Integer surplusQuota = data.getSurplusQuota();
        // 剩余额度大于 0
        if (surplusQuota > 0) {
            // 扣减额度
            Integer res = openAiRepository.subAccountQuota(openid);
            if (res != 0) {
                return RuleLogicEntity.<ChatProcessAggregate>builder()
                        .type(LogicTypeVO.SUCCESS)
                        .info(LogicTypeVO.SUCCESS.getInfo())
                        .data(process)
                        .build();
            }

            return RuleLogicEntity.<ChatProcessAggregate>builder()
                    .type(LogicTypeVO.REFUCE)
                    .info("当前账户可使用额度为 " + surplusQuota + " 请即时充值")
                    .data(process)
                    .build();
        }

        // 返回结果
        return RuleLogicEntity.<ChatProcessAggregate>builder()
                .type(LogicTypeVO.REFUCE)
                .info("当前账户可使用额度为 " + surplusQuota + " 请即时充值")
                .data(process)
                .build();
    }
}
