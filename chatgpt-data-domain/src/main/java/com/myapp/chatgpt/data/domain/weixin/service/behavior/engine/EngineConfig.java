package com.myapp.chatgpt.data.domain.weixin.service.behavior.engine;

import com.myapp.chatgpt.data.domain.weixin.service.behavior.logic.LogicFilter;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @description: 消息配置类，提供一些通用的服务,数据源
 * 使用工厂模式将过滤器都集中在一个 map 中
 * @author: 云奇迹
 * @date: 2024/3/20
 */
public class EngineConfig {

    @Resource
    private LogicFilter subscribe;

    @Resource
    private LogicFilter unsubscribe;

    @Resource
    private LogicFilter validCode;

    @Resource
    private LogicFilter scan;

    @Resource(name = "openAi")
    private LogicFilter openAi;

    protected static Map<String, Map<String, LogicFilter>> logicFilterMap = new HashMap<>();

    @PostConstruct
    public void init() {
        logicFilterMap.put("text", new HashMap<String, LogicFilter>() {{
            put("验证码", validCode);
            put("openAi", openAi);
        }});

        logicFilterMap.put("event", new HashMap<String, LogicFilter>() {{
            put("subscribe", subscribe);
            put("unsubscribe", unsubscribe);
            put("SCAN", scan);
        }});

    }


}
