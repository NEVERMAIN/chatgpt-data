package com.myapp.chatgpt.data.domain.openai.service.rule.impl;

import com.myapp.chatgpt.data.domain.openai.annotation.LogicStrategy;
import com.myapp.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import com.myapp.chatgpt.data.domain.openai.model.entity.RuleLogicEntity;
import com.myapp.chatgpt.data.domain.openai.model.entity.UserAccountQuotaEntity;
import com.myapp.chatgpt.data.domain.openai.model.vo.LogicTypeVO;
import com.myapp.chatgpt.data.domain.openai.service.rule.ILogicFilter;
import com.myapp.chatgpt.data.domain.openai.service.rule.factory.DefaultLogicFilterFactory;
import com.myapp.chatgpt.data.domain.weixin.model.vo.MsgTypeVO;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @description: 可使用模型过滤
 * @author: 云奇迹
 * @date: 2024/3/21
 */
@Component
@LogicStrategy(logicModel = DefaultLogicFilterFactory.LogicModel.AVAILABLE_MODEL)
public class AvailableModelFilter implements ILogicFilter<UserAccountQuotaEntity> {

    @Override
    public RuleLogicEntity<ChatProcessAggregate> filter(ChatProcessAggregate process, UserAccountQuotaEntity data) {
        // 1.用户使用的模型
        String model = process.getModel();
        // 2.用户可使用的模型
        List<String> availModes = data.getAvailModes();
        if (!availModes.contains(model)) {
            return RuleLogicEntity.<ChatProcessAggregate>builder()
                    .type(LogicTypeVO.REFUCE)
                    .info("当前账户不支持 " + model + " 这种模型,请充值后再使用")
                    .data(process)
                    .build();
        }

        return RuleLogicEntity.<ChatProcessAggregate>builder()
                .data(process)
                .info(LogicTypeVO.SUCCESS.getInfo())
                .type(LogicTypeVO.SUCCESS).build();
    }
}
