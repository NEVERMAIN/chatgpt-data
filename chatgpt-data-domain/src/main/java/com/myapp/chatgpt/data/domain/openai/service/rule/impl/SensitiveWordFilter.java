package com.myapp.chatgpt.data.domain.openai.service.rule.impl;

import com.alibaba.fastjson2.JSON;
import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.myapp.chatgpt.data.domain.openai.annotation.LogicStrategy;
import com.myapp.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import com.myapp.chatgpt.data.domain.openai.model.entity.MessageEntity;
import com.myapp.chatgpt.data.domain.openai.model.entity.RuleLogicEntity;
import com.myapp.chatgpt.data.domain.openai.model.entity.UserAccountQuotaEntity;
import com.myapp.chatgpt.data.domain.openai.model.valobj.LogicTypeVO;
import com.myapp.chatgpt.data.domain.openai.service.rule.ILogicFilter;
import com.myapp.chatgpt.data.domain.openai.service.rule.factory.DefaultLogicFilterFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;


/**
 * @description: 敏感词的规则过滤
 * @author: 云奇迹
 * @date: 2024/3/19
 */
@Component
@LogicStrategy(logicModel = DefaultLogicFilterFactory.LogicModel.SENSITIVE_WORD)
public class SensitiveWordFilter implements ILogicFilter<UserAccountQuotaEntity> {

    @Value("${app.config.white-list}")
    private String whiteList;
    /**
     * 敏感词库
     */
    @Resource
    private SensitiveWordBs sensitiveWordBs;

    @Override
    public RuleLogicEntity<ChatProcessAggregate> filter(ChatProcessAggregate process,UserAccountQuotaEntity accountQuotaEntity) {

        try {
            // 1.判断是不是白名单上的用户
            if (process.isWhiteList(whiteList)) {
                return RuleLogicEntity.<ChatProcessAggregate>builder()
                        .data(process)
                        .type(LogicTypeVO.SUCCESS)
                        .build();
            }

            // 2.获取最后一条信息
            MessageEntity lastMessage = process.getMessages().get(process.getMessages().size() - 1);
            // 3.找到所有的敏感词
            List<String> words = sensitiveWordBs.findAll(lastMessage.getContent());
            if (words != null && !words.isEmpty()) {
                return RuleLogicEntity.<ChatProcessAggregate>builder()
                        .info("当前内容包含敏感词信息 " + JSON.toJSONString(words) + " 请重新输入")
                        .type(LogicTypeVO.REFUCE)
                        .build();
            }

            // 返回规则校验对象
            return RuleLogicEntity.<ChatProcessAggregate>builder()
                    .info(LogicTypeVO.SUCCESS.getInfo())
                    .data(process)
                    .type(LogicTypeVO.SUCCESS).build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
