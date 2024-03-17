package com.myapp.chatgpt.data.domain.openai.model.entity;

import lombok.Data;

/**
 * @description: 消息返回体的模型
 * @author: 云奇迹
 * @date: 2024/3/17
 */
@Data
public class ChoiceEntity {
    /** stream = true 请求参数里返回的属性是 delta */
    private MessageEntity delta;
    /** stream = false 请求参数里返回的属性是 delta */
    private MessageEntity message;


}
