package com.myapp.chatgpt.data.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @description: openAi 角色
 * @author: 云奇迹
 * @date: 2024/4/7
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum OpenAiRole {
    /**
     * 用户
     */
    USER("user"),
    /**
     * 模型回答
     */
    ASSISTANT("assistant"),
    /**
     * 系统预设
     */
    SYSTEM("system");

    private String code;

    /**
     * 获取枚举值
     * @param code
     * @return
     */
    public static OpenAiRole get(String code) {
        OpenAiRole[] values = OpenAiRole.values();
        for (OpenAiRole value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
