package com.myapp.chatgpt.data.trigger.http.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: 云奇迹
 * @date: 2024/3/16
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageEntity {

    private String role;
    private String content;
    private String name;
}
