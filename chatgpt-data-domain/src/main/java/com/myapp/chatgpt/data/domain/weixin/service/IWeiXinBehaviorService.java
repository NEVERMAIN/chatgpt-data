package com.myapp.chatgpt.data.domain.weixin.service;

import com.myapp.chatgpt.data.domain.weixin.model.UserBehaviorMessageEntity;

/**
 * @description: 处理用户行为服务
 * @author: 云奇迹
 * @date: 2024/3/18
 */
public interface IWeiXinBehaviorService {

    String post(UserBehaviorMessageEntity behaviorMessage);
}
