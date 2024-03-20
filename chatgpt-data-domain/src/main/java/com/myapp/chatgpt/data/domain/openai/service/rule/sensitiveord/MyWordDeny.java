package com.myapp.chatgpt.data.domain.openai.service.rule.sensitiveord;

import com.github.houbb.sensitive.word.api.IWordDeny;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @description: 自定义某些词是敏感词 + 动态加载
 * @author: 云奇迹
 * @date: 2024/3/20
 */
@Component
public class MyWordDeny implements IWordDeny {

    @Override
    public List<String> deny() {
        // 这里可以从数据库中查询出配置的敏感词
        return Arrays.asList("习近平","天安门");
    }
}
