package com.myapp.chatgpt.data.domain.openai.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: 云奇迹
 * @date: 2024/3/19
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum LogicTypeVO {

    SUCCESS("0000","校验通过"),
    REFUCE("0001","校验失败")
    ;

    private String code;
    private String info;

}
