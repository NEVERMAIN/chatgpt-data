package com.myapp.chatgpt.data.domain.order.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: 云奇迹
 * @date: 2024/4/16
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum LtzfSDKTradeTypeVO {
    /**
     * 扫描支付
     */
    NATIVE("NATIVE", "扫码支付"),
    H5("H5", "H5支付"),
    APP("APP", "APP支付"),
    JSAPI("JSAPI", "公众号支付"),
    MINIPROGRAM("MINIPROGRAM", "小程序支付"),
    ;
    private String code;
    private String info;


    /**
     * 获取枚举对象
     * @param code 值
     * @return 返回枚举对象
     */
    public static LtzfSDKTradeTypeVO get(String code) {
        LtzfSDKTradeTypeVO[] values = LtzfSDKTradeTypeVO.values();
        for (LtzfSDKTradeTypeVO value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
