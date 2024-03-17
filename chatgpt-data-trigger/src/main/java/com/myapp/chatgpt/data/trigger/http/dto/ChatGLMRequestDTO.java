package com.myapp.chatgpt.data.trigger.http.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description:
 * @author: 云奇迹
 * @date: 2024/3/16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatGLMRequestDTO {

    private String model;
    private List<MessageEntity> messages;

}
