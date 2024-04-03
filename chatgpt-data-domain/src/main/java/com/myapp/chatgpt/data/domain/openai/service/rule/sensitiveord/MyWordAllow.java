package com.myapp.chatgpt.data.domain.openai.service.rule.sensitiveord;

import com.github.houbb.sensitive.word.api.IWordAllow;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @description: 自定义某些词不是敏感词,可以动态配置
 * @author: 云奇迹
 * @date: 2024/3/20
 */
@Component
public class MyWordAllow implements IWordAllow {
    @Override
    public List<String> allow() {
        return Arrays.asList("法","2B","五星红旗","算法","编程","小说","口","用户","咨询","资格证","色","性","床","执业");
    }
}
