package com.myapp.chatgpt.data.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @description: 产品是否可用的枚举类
 * @author: 云奇迹
 * @date: 2024/3/22
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum OpenAIProductEnableModel {

    /**
     * 无效
     */
    CLOSE(0,"无效，关闭"),
    /**
     * 有效
     */
    OPEN(1,"有效,使用中"),
    ;
    private Integer code;

    private String info;

    /**
     * 获取枚举对象
     * @param code
     * @return
     */
    public static OpenAIProductEnableModel get(Integer code){
        OpenAIProductEnableModel[] values = OpenAIProductEnableModel.values();
        for (OpenAIProductEnableModel value : values) {
            if(value.code.equals(code)){
                return value;
            }
        }
        return null;
    }



}
