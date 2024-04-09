package com.myapp.chatgpt.data.infrastructure.gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 获取微信登录二维码响应对象
 * @author: 云奇迹
 * @date: 2024/4/9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeChatQrCodeResponseDTO {

    /**
     * 获取的二维码 ticket ，凭借此ticket可以在有效时间内换取二维码。
     */
    private String ticket;
    /**
     * 二维码的有效时间，以秒为单位。最大不超过2592000（即30天）。
     */
    private Integer expire_seconds;
    /**
     * 二维码图片解析后的地址，开发者可根据该地址自行生成需要的二维码图片
     */
    private String url;

    private String errcode;

    private String errmsg;


}
