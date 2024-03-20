package com.myapp.chatgpt.data.domain.weixin.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @description:  微信公众号消息类型值对象，用于描述对象属性的值，为值对象。
 * @author: 云奇迹
 * @date: 2024/3/18
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum MsgTypeVO {

    EVENT("event","事件消息"),
    TEXT("text","文本消息")
    ;
    private String  code;
    private String desc;
}
