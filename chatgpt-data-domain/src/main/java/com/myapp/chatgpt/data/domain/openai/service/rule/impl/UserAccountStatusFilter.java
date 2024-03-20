package com.myapp.chatgpt.data.domain.openai.service.rule.impl;

import com.myapp.chatgpt.data.domain.openai.annotation.LogicStrategy;
import com.myapp.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import com.myapp.chatgpt.data.domain.openai.model.entity.RuleLogicEntity;
import com.myapp.chatgpt.data.domain.openai.model.entity.UserAccountQuotaEntity;
import com.myapp.chatgpt.data.domain.openai.model.vo.LogicTypeVO;
import com.myapp.chatgpt.data.domain.openai.model.vo.UserAccountStatusVO;
import com.myapp.chatgpt.data.domain.openai.service.rule.ILogicFilter;
import com.myapp.chatgpt.data.domain.openai.service.rule.factory.DefaultLogicFilterFactory;
import org.springframework.stereotype.Component;

/**
 * @description: 处理用户账户状态
 * @author: 云奇迹
 * @date: 2024/3/21
 */
@Component
@LogicStrategy(logicModel = DefaultLogicFilterFactory.LogicModel.USER_ACCOUNT_STATUS)
public class UserAccountStatusFilter implements ILogicFilter<UserAccountQuotaEntity> {
    @Override
    public RuleLogicEntity<ChatProcessAggregate> filter(ChatProcessAggregate process, UserAccountQuotaEntity data) {

        UserAccountStatusVO status = data.getStatus();
        if (UserAccountStatusVO.AVAILABLE.getCode().equals(status.getCode())) {
            return RuleLogicEntity.<ChatProcessAggregate>builder()
                    .type(LogicTypeVO.SUCCESS)
                    .data(process)
                    .info(LogicTypeVO.SUCCESS.getInfo())
                    .build();
        }

        return RuleLogicEntity.<ChatProcessAggregate>builder()
                .type(LogicTypeVO.REFUCE)
                .data(process)
                .info("当前账户已被冻结,请联系管理员解封")
                .build();
    }
}
