package com.myapp.chatgpt.data.domain.openai.model.entity;

import com.myapp.chatgpt.data.domain.openai.model.valobj.LogicTypeVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 规则过滤的结果对象
 * @author: 云奇迹
 * @date: 2024/3/19
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RuleLogicEntity<T> {

    /**
     * 规则过滤的结果类型
     */
    private LogicTypeVO type;
    /**
     * 规则过滤的结果信息
     */
    private String info;
    /**
     * 过滤的内容
     * 这里使用泛型,没有直接指定类型,能更灵活指定过滤的内容
     */
    private T data;

}
