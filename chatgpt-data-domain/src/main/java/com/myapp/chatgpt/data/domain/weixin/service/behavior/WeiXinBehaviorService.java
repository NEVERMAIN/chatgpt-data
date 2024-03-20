package com.myapp.chatgpt.data.domain.weixin.service.behavior;

import com.google.common.cache.Cache;
import com.myapp.chatgpt.data.domain.weixin.model.entity.MessageTextEntity;
import com.myapp.chatgpt.data.domain.weixin.model.entity.UserBehaviorMessageEntity;
import com.myapp.chatgpt.data.domain.weixin.model.vo.MsgTypeVO;
import com.myapp.chatgpt.data.domain.weixin.service.IWeiXinBehaviorService;
import com.myapp.chatgpt.data.types.exception.ChatGPTException;
import com.myapp.chatgpt.data.types.sdk.weixin.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @description:
 * @author: 云奇迹
 * @date: 2024/3/18
 */
@Slf4j
@Service
public class WeiXinBehaviorService implements IWeiXinBehaviorService {

    @Value("${wx.config.originalid}")
    private String originalid;

    @Resource
    private Cache<String,String> codeCache;

    @Override
    public String acceptUserBehavior(UserBehaviorMessageEntity behaviorMessage) {

        if(MsgTypeVO.EVENT.getCode().equals(behaviorMessage.getMsgType())){
            return "";
        }

        // Text 文本类型
        if(MsgTypeVO.TEXT.getCode().equals(behaviorMessage.getMsgType())){
            String openId = behaviorMessage.getOpenId();
            // 从缓存中获取验证码
            String isExistCode = codeCache.getIfPresent(openId);
            // 判断验证码是否存在 - 不考虑验证码重复问题
            if(StringUtils.isBlank(isExistCode)){
                // 生成 4 位随机数
                String code = RandomStringUtils.randomNumeric(4);
                // 放入缓存中
                codeCache.put(openId,code);
                codeCache.put(code,openId);
                isExistCode = code;
            }

            // 反馈信息[文本]
            MessageTextEntity res = MessageTextEntity.builder()
                    .toUserName(behaviorMessage.getOpenId())
                    .fromUserName(originalid)
                    .createTime(String.valueOf(System.currentTimeMillis() / 1000L))
                    .msgType(MsgTypeVO.TEXT.getCode())
                    .content(String.format("您的验证码是: %s ,有效期为%d分钟",isExistCode,3))
                    .build();

            // 返回结果
            return XmlUtil.beanToXml(res);
        }
        throw new ChatGPTException(behaviorMessage.getMsgType() + " 未被处理的行为类型 Err！");

    }
}
