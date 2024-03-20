package com.myapp.chatgpt.data.domain.openai.annotation;

import com.myapp.chatgpt.data.domain.openai.service.rule.factory.DefaultLogicFilterFactory;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description: 自定义注解,标注在规则过滤器上
 * @author: 云奇迹
 * @date: 2024/3/19
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogicStrategy {
    DefaultLogicFilterFactory.LogicModel logicModel();

}
