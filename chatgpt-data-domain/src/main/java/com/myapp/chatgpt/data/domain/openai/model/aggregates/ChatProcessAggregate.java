package com.myapp.chatgpt.data.domain.openai.model.aggregates;

import com.myapp.chatglm.model.Model;
import com.myapp.chatgpt.data.domain.openai.model.entity.MessageEntity;
import com.myapp.chatgpt.data.types.enums.OpenAiChannel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description: openAi 服务的聚合对象
 * @author: 云奇迹
 * @date: 2024/3/17
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatProcessAggregate {
    /**
     * 微信唯一ID
     */
    private String openId;
    /**
     * 模型类型
     */
    private String Model = "glm-3-turbo";
    /**
     * 消息体
     */
    private List<MessageEntity> messages;

    /**
     * 属不属于白名单
     * @param whiteList
     * @return
     */
    public boolean isWhiteList(String whiteList) {
        String[] list = whiteList.split(",");
        for (String userId : list) {
            if (userId.equals(openId)) return true;
        }
        return false;
    }

    /**
     * 根据模型类型调用模型对应的服务
     * @return
     */
    public OpenAiChannel getChannel() {
        return OpenAiChannel.getChannel(this.getModel());
    }
}
