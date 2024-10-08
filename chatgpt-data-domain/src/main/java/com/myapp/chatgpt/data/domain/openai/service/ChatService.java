package com.myapp.chatgpt.data.domain.openai.service;

import com.myapp.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import com.myapp.chatgpt.data.domain.openai.model.entity.RuleLogicEntity;
import com.myapp.chatgpt.data.domain.openai.model.entity.UserAccountQuotaEntity;
import com.myapp.chatgpt.data.domain.openai.model.valobj.LogicTypeVO;
import com.myapp.chatgpt.data.domain.openai.service.rule.ILogicFilter;
import com.myapp.chatgpt.data.domain.openai.service.rule.factory.DefaultLogicFilterFactory;
import com.myapp.chatgpt.data.types.enums.OpenAiModel;
import com.myapp.chatgpt.data.types.enums.OpenAiRole;
import com.myapp.openai.executor.parameter.Message;
import com.myapp.openai.executor.parameter.request.CompletionRequest;
import com.myapp.openai.session.OpenAiSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @description: chat 服务实现类
 * @author: 云奇迹
 * @date: 2024/3/17
 */
@Service
@Slf4j
public class ChatService extends AbstractChatService {


    @Resource
    private OpenAiSession openAiSession;

    @Resource
    private DefaultLogicFilterFactory defaultLogicFilterFactory;


    @Override
    protected RuleLogicEntity<ChatProcessAggregate> doCheckLogic(ChatProcessAggregate process, UserAccountQuotaEntity accountQuotaEntity, String... logics) {

        // 1.获得所有的权限策略
        Map<String, ILogicFilter> groups = defaultLogicFilterFactory.getLogicFilterGroups();

        RuleLogicEntity<ChatProcessAggregate> entity = null;
        for (String logic : logics) {
            // 2. 如果权限类型为 null ,枚举下一个
            if (DefaultLogicFilterFactory.LogicModel.NULL.getCode().equals(logic)) continue;
            // 3. logic 不为 null , 获取权限校验的策略
            ILogicFilter logicFilter = groups.get(logic);
            // 4. 进行权限的校验
            entity = logicFilter.filter(process, accountQuotaEntity);
            if (!LogicTypeVO.SUCCESS.getCode().equals(entity.getType().getCode())) return entity;
        }

        return entity != null ? entity :
                RuleLogicEntity.<ChatProcessAggregate>builder()
                        .type(LogicTypeVO.SUCCESS)
                        .data(process)
                        .info(LogicTypeVO.SUCCESS.getInfo()).build();
    }

}
