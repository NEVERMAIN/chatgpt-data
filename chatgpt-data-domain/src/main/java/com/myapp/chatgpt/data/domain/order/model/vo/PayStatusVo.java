package com.myapp.chatgpt.data.domain.order.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @description: 支付状态枚举类
 * @author: 云奇迹
 * @date: 2024/3/22
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum PayStatusVo {

    /**
     * 等待支付
     */
    WAIT(0,"等待支付"),
    SUCCESS(1,"支付成功"),
    FAILED(2,"支付失败"),
    QUIT(3,"放弃支付"),

    ;

    private Integer code;
    private String info;

    public static PayStatusVo get(Integer code){
        PayStatusVo[] values = PayStatusVo.values();
        for (PayStatusVo value : values) {
            if(value.code.equals(code)){
                return value;
            }
        }
        return null;
    }


}
