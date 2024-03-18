package com.myapp.chatgpt.data.domain.atuth.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 权限验证返回对象
 * @author: 云奇迹
 * @date: 2024/3/18
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthStateEntity {

    /**
     * jwt 生成的 token
     */
    private String token;
    /**
     * 错误码
     */
    private String code;
    /**
     * 错误信息
     */
    private String info;
    /**
     * 微信用户个人凭证
     */
    private String openid;

}
