package com.myapp.chatgpt.data.domain.account.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @description: 用户账单枚举类
 * @author: 云奇迹
 * @date: 2024/3/21
 */

@NoArgsConstructor
@AllArgsConstructor
@Getter
public enum AccountStatusVO {

    SUCCESS("0000","成功"),
    ACCOUNT_EXIT("0001","账户已经存在"),
    ;
    private String code;
    private String Info;


}
