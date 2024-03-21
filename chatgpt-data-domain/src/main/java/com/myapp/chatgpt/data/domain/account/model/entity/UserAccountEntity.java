package com.myapp.chatgpt.data.domain.account.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.A;

/**
 * @description: 用户账单结果返回对象
 * @author: 云奇迹
 * @date: 2024/3/21
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAccountEntity {

    private String code;
    private String info;

}
