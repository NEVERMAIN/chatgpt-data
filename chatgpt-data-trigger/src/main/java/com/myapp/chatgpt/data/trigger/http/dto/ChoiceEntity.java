package com.myapp.chatgpt.data.trigger.http.dto;

import lombok.Data;

/**
 * @description:
 * @author: 云奇迹
 * @date: 2024/3/16
 */
@Data
public class ChoiceEntity {

    /** stream = true 请求参数里返回的属性是 message */
    private MessageEntity delta;
    /** stream = false 请求参数里返回的属性是 message */
    private MessageEntity message;
}
