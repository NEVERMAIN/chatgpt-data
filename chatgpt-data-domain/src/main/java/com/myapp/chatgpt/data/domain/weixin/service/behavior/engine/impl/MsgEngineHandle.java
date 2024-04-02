package com.myapp.chatgpt.data.domain.weixin.service.behavior.engine.impl;

import com.myapp.chatgpt.data.domain.weixin.model.entity.BehaviorMatter;
import com.myapp.chatgpt.data.domain.weixin.model.entity.MessageTextEntity;
import com.myapp.chatgpt.data.domain.weixin.model.vo.MsgTypeVO;
import com.myapp.chatgpt.data.domain.weixin.service.behavior.engine.AbstractEngineBase;
import com.myapp.chatgpt.data.domain.weixin.service.behavior.logic.LogicFilter;
import com.myapp.chatgpt.data.types.sdk.weixin.XmlUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @description:
 * @author: 云奇迹
 * @date: 2024/3/20
 */
@Service("msgEngineHandle")
public class MsgEngineHandle extends AbstractEngineBase {

    @Value("${wx.config.originalid}")
    private String originalId;

    @Override
    public String process(BehaviorMatter behaviorMatter) {
        // 1. 获取对应的过滤器
        LogicFilter filter = super.router(behaviorMatter);

        // 2. 判断是否存在
        if(null == filter){
            return null;
        }

        // 3. 执行过滤器,获取处理后的结果
        String result = filter.filter(behaviorMatter);
        if(StringUtils.isBlank(result)){
            return "";
        }

        // 反馈文本
        // 反馈信息[文本]
        MessageTextEntity res = MessageTextEntity.builder()
                .toUserName(behaviorMatter.getOpenId())
                .fromUserName(originalId)
                .createTime(String.valueOf(System.currentTimeMillis() / 1000L))
                .msgType(MsgTypeVO.TEXT.getCode())
                .content(result)
                .build();

        return XmlUtil.beanToXml(res);
    }
}
