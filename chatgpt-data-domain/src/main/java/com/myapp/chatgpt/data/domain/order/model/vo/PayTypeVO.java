package com.myapp.chatgpt.data.domain.order.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @description: 支付类型枚举类
 * @author: 云奇迹
 * @date: 2024/3/22
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum PayTypeVO {

    /**
     * 微信支付
     */
    WEIXIN_NATIVE(0,"微信NATIVE支付"),
    ALIPAY(1,"支付宝支付-沙箱"),

    ;
    private Integer code;

    private String info;

    /**
     * 获取支付类型
     * @param code
     * @return
     */
    public static PayTypeVO get(Integer code){
        PayTypeVO[] values = PayTypeVO.values();
        for (PayTypeVO value : values) {
            if(value.getCode().equals(code)){
                return value;
            }
        }
        // 默认微信支付
        return WEIXIN_NATIVE;
    }
}
