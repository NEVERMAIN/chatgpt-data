package com.myapp.chatgpt.data.domain.weixin.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @description: 提供处理用户行为需要的数据
 * @author: 云奇迹
 * @date: 2024/3/18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BehaviorMatter {
    /**
     * 用户微信的唯一ID
     */
    private String openId;
    /**
     * 来自哪个微信用户的标识
     */
    private String fromUserName;
    /**
     * 消息的类型
     */
    private String msgType;
    /**
     * 消息文本
     */
    private String content;
    /**
     * 事件类型
     */
    private String event;
    private Date createTime;
}
