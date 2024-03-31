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
public enum SparkModel {
    /**
     * 聊天
     */
    GENERALV_3_5("generalv3.5", ""),
    /**
     * 图片生成
     */
    GENERAL("general", "");
    private String code;
    private String info;


    public static SparkModel get(String code) {
        SparkModel[] values = SparkModel.values();
        for (SparkModel value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

}
