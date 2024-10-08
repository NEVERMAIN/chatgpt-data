package com.myapp.chatgpt.data.infrastructure.gateway.dto;

import lombok.*;

/**
 * @description: 获取微信登录二维码请求对象
 * @author: 云奇迹
 * @date: 2024/4/9
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeChatQrCodeRequestDTO {

    /**
     * 该二维码有效时间，以秒为单位。 最大不超过 2592000（即30天），此字段如果不填，则默认有效期为60秒。
     */
    private Integer expire_seconds;
    /**
     * 二维码类型:
     * QR_SCENE 为临时的整型参数值，
     * QR_STR_SCENE 为临时的字符串参数值，
     * QR_LIMIT_SCENE 为永久的整型参数值，
     * QR_LIMIT_STR_SCENE 为永久的字符串参数值
     */
    private  String action_name;
    /**
     * 二维码详细信息
     */
    private  ActionInfo action_info;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class  ActionInfo{

        private Scene scene;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class Scene{
            /**
             * 场景值ID，临时二维码时为32位非0整型，永久二维码时最大值为100000（目前参数只支持1--100000)
             */
            private Integer scene_id;
            /**
             * 场景值ID（字符串形式的ID），字符串类型，长度限制为1到64
             */
            private String scene_str;
        }

    }

    /**
     * ActionNameTypeVO 枚举类，用于定义二维码场景类型
     * @@Getter 提供自动生成功能的get方法
     * @NoArgsConstructor 提供无参构造方法
     * @AllArgsConstructor 提供全参数构造方法
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public enum ActionNameTypeVO{
        // 临时二维码，整型参数
        QR_SCENE("QR_SCENE","临时的整型参数值"),
        // 临时二维码，字符串参数
        QR_STR_SCENE("QR_STR_SCENE","临时的字符串参数值"),
        // 永久二维码，整型参数
        QR_LIMIT_SCENE("QR_LIMIT_SCENE","永久的整型参数值"),
        // 永久二维码，字符串参数
        QR_LIMIT_STR_SCENE("QR_LIMIT_STR_SCENE","永久的字符串参数值"),
        ;

        // 枚举类型的属性：代码和信息
        private String code;
        private String info;


    }




}
