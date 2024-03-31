package com.myapp.chatgpt.data.types.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.ui.Model;

/**
 * @description:
 * @author: 云奇迹
 * @date: 2024/3/29
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ChatGLMModel {

    /**
     * glm-3-turbo 大模型
     * 推荐使用 SSE 或异步调用方式请求接口
     */
    GLM_3_TURBO("glm-3-turbo", "适用于对知识量、推理能力、创造力要求较高的场景"),
    /**
     * GLM-4
     * 推荐使用 SSE 或异步调用方式请求接口
     */
    GLM_4("glm-4", "根据输入的自然语言指令完成多种语言类任务，推荐使用 SSE 或异步调用方式请求接口"),
    /**
     * GLM-4V
     * 推荐使用 SSE 或同步调用方式请求接口
     */
    GLM_4V("glm-4v", "根据输入的自然语言指令完成多种语言类任务，推荐使用 SSE 或异步调用方式请求接口"),
    CogView("cogview-3", "根据用户的文字描述生成图像,使用同步调用方式请求接口"),
    CharacterGLM("charglm-3", "支持基于人设的角色扮演、超长多轮的记忆、千人千面的角色对话，广泛应用于情感陪伴、游戏智能NPC、网红/明星/影视剧IP分身、数字人/虚拟主播、文字冒险游戏等拟人对话或游戏场景。"),
    ;

    private String code;
    private String info;

    public static ChatGLMModel get(String code) {
        ChatGLMModel[] values = ChatGLMModel.values();
        for (ChatGLMModel value : values) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }


}
