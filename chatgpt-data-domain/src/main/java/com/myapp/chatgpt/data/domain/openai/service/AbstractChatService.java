package com.myapp.chatgpt.data.domain.openai.service;

import com.myapp.chatgpt.data.domain.openai.model.aggregates.ChatProcessAggregate;
import com.myapp.chatgpt.data.types.common.Constants;
import com.myapp.chatgpt.data.types.exception.ChatGPTException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

/**
 * @description: 抽象类，主要是编排方法的执行流程
 * @author: 云奇迹
 * @date: 2024/3/17
 */
@Slf4j
public abstract class AbstractChatService implements IChatService {

    @Override
    public ResponseBodyEmitter completions(ChatProcessAggregate chatProcess) {
        // 1. 校验权限
        if(!"openai".equals(chatProcess.getToken())){
            throw new ChatGPTException(Constants.ResponseCode.TOKEN_ERROR.getCode(), Constants.ResponseCode.TOKEN_ERROR.getInfo());
        }

        // 2. 请求应答
        ResponseBodyEmitter emitter = new ResponseBodyEmitter(3 * 6 * 1000L);
        emitter.onCompletion(()->{
            log.info("流式问答请求结束,使用的模型:{}",chatProcess.getModel());
        });

        emitter.onError((e)->{
            log.error("流式问答请求出现异常,使用的模型:{},异常信息",chatProcess.getModel(),e);
        });

        // 3. 应答处理
        try {
            this.doOnMessage(chatProcess,emitter);
        } catch (Exception e) {
            throw new ChatGPTException(Constants.ResponseCode.UN_ERROR.getCode(), Constants.ResponseCode.UN_ERROR.getInfo());
        }

        // 4. 返回结果
        return emitter;
    }

    /**
     * 调用大模型问答服务
     * @param emitter
     * @return
     */
    protected abstract void doOnMessage(ChatProcessAggregate chatProcess,ResponseBodyEmitter emitter);


}
