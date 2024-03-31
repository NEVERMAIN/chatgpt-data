package com.myapp.chatgpt.data.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: 云奇迹
 * @date: 2024/3/29
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum OpenAiChannel {

    ChatGLM("ChatGLM"),
    Spark("general"),

    ;


    private String code;

    public static OpenAiChannel getChannel(String model) {
        if (model.toLowerCase().contains("glm")) return OpenAiChannel.ChatGLM;
        if (model.toLowerCase().contains("general")) return OpenAiChannel.Spark;
        return null;
    }
}
