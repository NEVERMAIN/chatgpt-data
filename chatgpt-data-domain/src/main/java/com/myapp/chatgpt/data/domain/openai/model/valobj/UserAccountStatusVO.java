package com.myapp.chatgpt.data.domain.openai.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @description: 账户类型枚举类
 * @author: 云奇迹
 * @date: 2024/3/20
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum UserAccountStatusVO {

    /**
     * 可使用的账户
     */
    AVAILABLE(0,"可用"),
    /**
     * 被冻结的账户
     */
    FREEZE(1,"冻结"),
    ;
    private Integer code;
    private String info;


    /**
     * 获取枚举对象
     * @param status
     * @return
     */
    public static UserAccountStatusVO get(Integer status){
        UserAccountStatusVO[] values = UserAccountStatusVO.values();
        for (UserAccountStatusVO value : values) {
            if(value.getCode().equals(status)){
                return value;
            }
        }
        return null;
    }

}
