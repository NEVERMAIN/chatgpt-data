package com.myapp.chatgpt.data.domain.atuth.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @description: 鉴权结果枚举类
 * @author: 云奇迹
 * @date: 2024/3/18
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum AuthTypeVo {

    SUCCESS("0000","验证成功"),
    NOT_EXIST("0001","验证码不存在"),
    NOT_VALID("0002","验证码失效");

    private String code;
    private String info;
}
