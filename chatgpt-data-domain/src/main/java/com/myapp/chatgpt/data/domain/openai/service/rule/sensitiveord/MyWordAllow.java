package com.myapp.chatgpt.data.domain.openai.service.rule.sensitiveord;

import com.github.houbb.sensitive.word.api.IWordAllow;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * @description: 动态加载 + 返回的内容不当做敏感词
 * @author: 云奇迹
 * @date: 2024/3/20
 */
@Component
public class MyWordAllow implements IWordAllow {
    @Override
    public List<String> allow() {
        return Arrays.asList("五星红旗","算法","编程");
    }
}
