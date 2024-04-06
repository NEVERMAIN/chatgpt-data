package com.myapp.chatgpt.data.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @description: chatGLM 的角色枚举类
 * @author: 云奇迹
 * @date: 2024/4/3
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum Role {

    USER("user"),
    ASSISTANT("assistant"),
    SYSTEM("system");

    private String code;

    public static Role get(String code) {
        Role[] values = Role.values();
        for (Role value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}
