package com.myapp.chatgpt.data.infrastructure.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 获取 Access token DTO 对象
 * @author: 云奇迹
 * @date: 2024/4/9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeChatTokenResponseDTO {

    /**
     * 获取到的凭证
     */
    private String access_token;
    /**
     * 凭证有效时间，单位：秒
     */
    private Integer expires_in;
    /**
     * 错误码
     */
    private String errcode;
    /**
     * 错误信息
     */
    private String errmsg;
}
