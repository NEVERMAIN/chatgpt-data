package com.myapp.chatgpt.data.domain.order.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @description: 订单状态枚举类
 * @author: 云奇迹
 * @date: 2024/3/22
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum OrderStatusVO {

    /**
     * 创建完成
     */
    CREATE(0,"创建完成"),
    WAIT(1,"等待发货"),
    COMPLETED(2,"发货完成"),
    CLOSE(3,"系统关单"),

    ;
    private Integer code;
    private String info;

    /**
     * 获取订单状态的枚举
     * @param code
     * @return
     */
    public static OrderStatusVO get(Integer code){
        OrderStatusVO[] values = OrderStatusVO.values();
        for (OrderStatusVO value : values) {
            if(value.code.equals(code)){
                return value;
            }
        }
        return null;
    }
}
