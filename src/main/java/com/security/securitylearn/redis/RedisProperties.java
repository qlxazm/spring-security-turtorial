package com.security.securitylearn.redis;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.security.securitylearn.redis.RedisProperties.REDIS_PREFIX;

/**
 * redis的一些配置项目
 * @author qian
 * @date 2019/12/13 16:35
 */
@Data
@ConfigurationProperties(prefix = REDIS_PREFIX)
public class RedisProperties {

    static final String REDIS_PREFIX = "redis.config";

    private String auth;

    private String host;

    private int database;

    private int port;

    private int poolMaxActive;

    private int poolMinIdle;

    private int poolMaxWait;
}
