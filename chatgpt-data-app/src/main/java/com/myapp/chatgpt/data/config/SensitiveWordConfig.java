package com.myapp.chatgpt.data.config;

import com.github.houbb.sensitive.word.api.IWordContext;
import com.github.houbb.sensitive.word.api.IWordResult;
import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.github.houbb.sensitive.word.utils.InnerWordCharUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description: 敏感词库
 * @author: 云奇迹
 * @date: 2024/3/19
 */
@Configuration
@Slf4j
public class SensitiveWordConfig {

    @Bean
    public SensitiveWordBs sensitiveWordBs() {
        return SensitiveWordBs.newInstance()
                /**
                 * stringBuilder – 字符串连接器
                 * rawChars – 原始字符串
                 * wordResult – 当前的敏感词结果
                 * wordContext – 上下文
                 */
                .wordReplace((StringBuilder stringBuilder, char[] rawChars, IWordResult wordResult, IWordContext wordContext) -> {
                    String sensitiveWord = InnerWordCharUtils.getString(rawChars, wordResult);
                    log.info("检测到敏感词:{}",sensitiveWord);
                    // 自定义不同的敏感词替换策略，可以从数据库等地方读取
                    if("五星红旗".equals(sensitiveWord)) {
                        stringBuilder.append("国家旗帜");
                    } else if("毛主席".equals(sensitiveWord)) {
                        stringBuilder.append("教员");
                    } else {
                        // 其他默认使用 * 代替
                        int wordLength = wordResult.endIndex() - wordResult.startIndex();
                        for(int i = 0; i < wordLength; i++) {
                            stringBuilder.append('*');
                        }
                    }
                })
                .ignoreCase(true)
                .ignoreWidth(true)
                .ignoreNumStyle(true)
                .ignoreChineseStyle(true)
                .ignoreEnglishStyle(true)
                .ignoreRepeat(false)
                .enableNumCheck(true)
                .enableEmailCheck(true)
                .enableUrlCheck(true)
                .enableWordCheck(true)
                .numCheckLen(1024)
                .init();
    }


}
