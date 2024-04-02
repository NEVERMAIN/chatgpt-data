package com.myapp.chatgpt.data.domain.weixin.model.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 微信公众号的消息文本类型对象
 * @author: 云奇迹
 * @date: 2024/3/18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageTextEntity {
    @XStreamAlias("MsgId")
    private String msgId;
    /**
     * 微信公众号
     */
    @XStreamAlias("ToUserName")
    private String toUserName;
    /**
     * 用户的唯一标识
     */
    @XStreamAlias("FromUserName")
    private String fromUserName;
    @XStreamAlias("CreateTime")
    private String createTime;
    /**
     * 消息类型
     */
    @XStreamAlias("MsgType")
    private String msgType;
    @XStreamAlias("Content")
    private String content;
    /**
     * 事件类型
     */
    @XStreamAlias("Event")
    private String event;
    @XStreamAlias("EventKey")
    private String eventKey;
}
