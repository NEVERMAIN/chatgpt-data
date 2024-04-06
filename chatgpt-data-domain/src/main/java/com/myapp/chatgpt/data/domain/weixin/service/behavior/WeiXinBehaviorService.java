package com.myapp.chatgpt.data.domain.weixin.service.behavior;

import com.google.common.cache.Cache;
import com.myapp.chatgpt.data.domain.weixin.model.entity.MessageTextEntity;
import com.myapp.chatgpt.data.domain.weixin.model.entity.BehaviorMatter;
import com.myapp.chatgpt.data.domain.weixin.model.vo.MsgTypeVO;
import com.myapp.chatgpt.data.domain.weixin.service.IWeiXinBehaviorService;
import com.myapp.chatgpt.data.domain.weixin.service.behavior.engine.Engine;
import com.myapp.chatgpt.data.types.exception.ChatGPTException;
import com.myapp.chatgpt.data.types.sdk.weixin.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @description: 微信行为服务类
 * @author: 云奇迹
 * @date: 2024/3/18
 */
@Slf4j
@Service
public class WeiXinBehaviorService implements IWeiXinBehaviorService {

    @Resource(name = "msgEngineHandle")
    private Engine msgEngineHandle;

    @Override
    public String acceptUserBehavior(BehaviorMatter behaviorMessage) {
        return msgEngineHandle.process(behaviorMessage);
    }
}
