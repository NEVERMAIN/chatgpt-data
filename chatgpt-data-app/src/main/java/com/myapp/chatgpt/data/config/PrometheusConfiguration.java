package com.myapp.chatgpt.data.config;

import io.micrometer.core.aop.CountedAspect;
import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.CollectorRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @description: Prometheus 数据采集
 * @author: 云奇迹
 * @date: 2024/4/1
 */
@EnableAspectJAutoProxy
@Configuration
public class PrometheusConfiguration {

    @Bean
    public CollectorRegistry collectorRegistry() {
        return new CollectorRegistry();
    }

    @Bean
    public PrometheusMeterRegistry prometheusMeterRegistry(PrometheusConfig config, CollectorRegistry collectorRegistry) {
        return new PrometheusMeterRegistry(config, collectorRegistry, Clock.SYSTEM);
    }

    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }

    @Bean
    public CountedAspect countedAspect(MeterRegistry registry) {
        return new CountedAspect(registry);
    }

}
