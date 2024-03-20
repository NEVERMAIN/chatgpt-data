package com.myapp.chatgpt.data.domain.weixin.service;

import com.myapp.chatgpt.data.domain.weixin.model.entity.BehaviorMatter;

/**
 * @description: 处理用户行为服务
 * @author: 云奇迹
 * @date: 2024/3/18
 */
public interface IWeiXinBehaviorService {

    /**
     * 接收处理用户请求的行为
     * @param behaviorMessage
     * @return
     */
    String acceptUserBehavior(BehaviorMatter behaviorMessage);
}
