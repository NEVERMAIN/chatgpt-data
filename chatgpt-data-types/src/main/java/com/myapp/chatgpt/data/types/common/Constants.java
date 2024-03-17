package com.myapp.chatgpt.data.types.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @description: 常量值
 * @author: 云奇迹
 * @date: 2024/3/17
 */
public class Constants {

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public enum ResponseCode{
        /**
         * 成功
         */
        SUCEESS("0000","成功"),
        UN_ERROR("0001","未知失败"),
        ILLEGAL_PARAMETER("0002","非法参数"),
        TOKEN_ERROR("0003","权限拦截"),
        ;
        private String code;
        private String info;
    }

}
