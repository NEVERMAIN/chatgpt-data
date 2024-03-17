package com.myapp.chatgpt.data.domain.openai.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 请求消息的格式
 * @author: 云奇迹
 * @date: 2024/3/17
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class MessageEntity {

    /**
     * 角色类型
     */
    private String role;
    /**
     * 消息
     */
    private String content;

}
