package com.myapp.chatgpt.data.domain.openai.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: 云奇迹
 * @date: 2024/4/6
 */
@Getter
@AllArgsConstructor
public enum GenerativeModelVO {

    TEXT("text", "文本"),
    IMAGES("images", "图片"),

    ;
    private final String code;
    private final String info;
}
