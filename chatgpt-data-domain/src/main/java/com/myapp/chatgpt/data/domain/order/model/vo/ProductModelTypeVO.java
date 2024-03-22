package com.myapp.chatgpt.data.domain.order.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @description: 产品模型枚举类
 * @author: 云奇迹
 * @date: 2024/3/22
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum ProductModelTypeVO {

    /**
     * 默认模型
     */
    DEFAULT_MODEL("glm-3-turbo,glm-4,glm-4v,cogview-3","默认"),

    ;
    private String code;
    private String info;



}
