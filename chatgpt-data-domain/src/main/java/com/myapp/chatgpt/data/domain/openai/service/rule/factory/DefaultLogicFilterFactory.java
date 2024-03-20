package com.myapp.chatgpt.data.domain.openai.service.rule.factory;

import com.myapp.chatgpt.data.domain.openai.annotation.LogicStrategy;
import com.myapp.chatgpt.data.domain.openai.service.rule.ILogicFilter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: 工厂类
 * @author: 云奇迹
 * @date: 2024/3/19
 */
@Component
public class DefaultLogicFilterFactory {

    public ConcurrentHashMap<String,ILogicFilter> logicFilterGroups = new ConcurrentHashMap<>();

    public DefaultLogicFilterFactory(List<ILogicFilter> logicFilters){
        for (ILogicFilter filter : logicFilters) {
            LogicStrategy strategy = AnnotationUtils.findAnnotation(filter.getClass(), LogicStrategy.class);
            if(strategy != null){
                logicFilterGroups.put(strategy.logicModel().getCode(),filter);
            }
        }
    }

    public Map<String,ILogicFilter> getLogicFilterGroups(){
        return logicFilterGroups;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public enum  LogicModel{
        /**
         * 访问次数
         */
        ACCESS_LIMIT("ACCESS_LIMIT","访问次数过滤"),
        /**
         * 敏感词
         */
        SENSITIVE_WORD("SENSITIVE_WORD","敏感词过滤"),
        /**
         * 访问频率
         */
        ACCESS_FREQUENCY("ACCESS_FREQUENCY","敏感词过滤"),
        ;
        private String code;
        private String info;
    }
}
