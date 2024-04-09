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
    /**
     * 消息 id
     */
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
    /**
     * 消息创建时间
     */
    @XStreamAlias("CreateTime")
    private String createTime;
    /**
     * 消息类型
     */
    @XStreamAlias("MsgType")
    private String msgType;
    /**
     * 文本消息内容
     */
    @XStreamAlias("Content")
    private String content;
    /**
     * 事件类型
     */
    @XStreamAlias("Event")
    private String event;
    /**
     * 事件 KEY 值
     */
    @XStreamAlias("EventKey")
    private String eventKey;
    /**
     * 二维码的ticket，可用来换取二维码图片
     */
    @XStreamAlias("Ticket")
    private String ticket;
}
