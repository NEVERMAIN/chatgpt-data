package com.myapp.chatgpt.data.domain.openai.model.aggregates;

import com.myapp.chatgpt.data.domain.openai.model.entity.MessageEntity;
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
     * 鉴权 token
     */
    private String token;
    /**
     * 模型类型
     */
    private String Model;
    /**
     * 消息体
     */
    private List<MessageEntity> messages;


}
