package com.myapp.chatgpt.data.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @description: 微信事件对象枚举值
 * @author: 云奇迹
 * @date: 2024/4/9
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum WechatEventVO {

    /**
     * 事件类型
     */
    SUBSCRIBE("subscribe", "订阅"),
    UNSUBSCRIBE("unsubscribe", "取消订阅"),
    SCAN("SCAN", "扫描二维码"),
    LOCATION("location", "上报地理位置"),
    CLICK("click", "点击菜单"),
    VIEW("view", "点击菜单跳转链接"),
    ;

    private String code;
    private String info;

    /**
     * 根据 code 获取枚举值
     * @param code
     * @return
     */
    public static WechatEventVO getByCode(String code) {
        for (WechatEventVO value : WechatEventVO.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }



}
