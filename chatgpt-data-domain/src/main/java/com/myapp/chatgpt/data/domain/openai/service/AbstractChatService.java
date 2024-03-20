package com.myapp.chatgpt.data.domain.openai.service;

import com.alibaba.fastjson.JSON;
import com.myapp.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import com.myapp.chatgpt.data.domain.openai.model.entity.RuleLogicEntity;
import com.myapp.chatgpt.data.domain.openai.model.entity.UserAccountQuotaEntity;
import com.myapp.chatgpt.data.domain.openai.model.vo.LogicTypeVO;
import com.myapp.chatgpt.data.domain.openai.repository.IOpenAiRepository;
import com.myapp.chatgpt.data.domain.openai.service.rule.factory.DefaultLogicFilterFactory;
import com.myapp.chatgpt.data.types.common.Constants;
import com.myapp.chatgpt.data.types.exception.ChatGPTException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import javax.annotation.Resource;

/**
 * @description: 抽象类，主要是编排方法的执行流程
 * @author: 云奇迹
 * @date: 2024/3/17
 */
@Slf4j
public abstract class AbstractChatService implements IChatService {


    @Resource
    private IOpenAiRepository openAiRepository;


    @Override
    public ResponseBodyEmitter completions(ChatProcessAggregate chatProcess, ResponseBodyEmitter emitter) {

        // 1.请求问答设置
        emitter.onCompletion(() -> {
            log.info("流式问答请求结束,使用的模型:{}", chatProcess.getModel());
        });

        emitter.onError((e) -> {
            log.error("流式问答请求出现异常,使用的模型:{},异常信息", chatProcess.getModel(), e);
        });

        try {
            // 2.查询用户账户相关信息
            UserAccountQuotaEntity userAccountQuotaEntity = openAiRepository.query(chatProcess.getOpenId());

            // 2.权限校验
            RuleLogicEntity<ChatProcessAggregate> ruleLogicEntity = this.doCheckLogic(chatProcess, userAccountQuotaEntity,
                    DefaultLogicFilterFactory.LogicModel.ACCESS_LIMIT.getCode(),
                    DefaultLogicFilterFactory.LogicModel.SENSITIVE_WORD.getCode(),
                    DefaultLogicFilterFactory.LogicModel.ACCESS_FREQUENCY.getCode(),
                    null != userAccountQuotaEntity ? DefaultLogicFilterFactory.LogicModel.USER_ACCOUNT_STATUS.getCode() : DefaultLogicFilterFactory.LogicModel.NULL.getCode(),
                    null != userAccountQuotaEntity ? DefaultLogicFilterFactory.LogicModel.AVAILABLE_MODEL.getCode() : DefaultLogicFilterFactory.LogicModel.NULL.getCode(),
                    null != userAccountQuotaEntity ? DefaultLogicFilterFactory.LogicModel.USER_ACCOUNT_QUOTA.getCode() : DefaultLogicFilterFactory.LogicModel.NULL.getCode()

            );

            // 没有通过校验
            if (!LogicTypeVO.SUCCESS.getCode().equals(ruleLogicEntity.getType().getCode())) {
                emitter.send(ruleLogicEntity.getInfo());
                emitter.complete();
                return emitter;
            }

            // 重新设置用户提问的文本信息
            ChatProcessAggregate process = ruleLogicEntity.getData();
            log.info("规则过滤后,用户的提问信息:{}", JSON.toJSONString(process.getMessages()));

            // 3. 应答处理
            this.doMessageResponse(process, emitter);
        } catch (Exception e) {
            throw new ChatGPTException(Constants.ResponseCode.UN_ERROR.getCode(), Constants.ResponseCode.UN_ERROR.getInfo());
        }

        // 4. 返回结果
        return emitter;
    }

    /**
     * 调用大模型问答服务
     *
     * @param emitter
     * @param chatProcess
     * @return
     */
    protected abstract void doMessageResponse(ChatProcessAggregate chatProcess, ResponseBodyEmitter emitter);


    protected abstract RuleLogicEntity<ChatProcessAggregate> doCheckLogic(ChatProcessAggregate process, UserAccountQuotaEntity accountQuotaEntity, String... logics);

}
