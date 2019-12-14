package com.security.securitylearn.redis;

import com.security.securitylearn.jwt.JwtTokenPair;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @EnableCaching 用于开启缓存
 * @author qian
 * @date 2019/12/13 16:41
 */
@Slf4j
@EnableConfigurationProperties(RedisProperties.class)
@Configuration
public class RedisConfig {

    /**
     * redis连接池的配置
     * @param properties
     * @return
     */
    @Bean
    public JedisPoolConfig jedisPoolConfig(RedisProperties properties) {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 设置最大连接数
        jedisPoolConfig.setMaxTotal(properties.getPoolMaxActive());
        // 设置最小连接数
        jedisPoolConfig.setMinIdle(properties.getPoolMinIdle());
        // 设置最大等待时间
        jedisPoolConfig.setMaxWaitMillis(properties.getPoolMaxWait());
        return jedisPoolConfig;
    }

    /**
     * 建立一个单机版的redis工厂
     * @param properties
     * @param poolConfig
     * @return
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory(RedisProperties properties, JedisPoolConfig poolConfig) {
        // 单机版的redis工厂
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(properties.getHost());
        redisStandaloneConfiguration.setDatabase(properties.getDatabase());
//        redisStandaloneConfiguration.setPassword(properties.getAuth());
        redisStandaloneConfiguration.setPort(properties.getPort());

        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jpcb = (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder) JedisClientConfiguration.builder();

        jpcb.poolConfig(poolConfig);

        JedisClientConfiguration clientConfiguration = jpcb.build();

        return new JedisConnectionFactory(redisStandaloneConfiguration, clientConfiguration);
    }

    /**
     * 设置操作redis的模板
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public RedisTemplate<String, JwtTokenPair> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, JwtTokenPair> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    /*@Bean
    public CacheManager cacheManager(RedisTemplate<String, JwtTokenPair> template) {
        RedisCacheConfiguration redisCacheConfiguration=RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(null);
        return RedisCacheManager.builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
                .cacheDefaults(redisCacheConfiguration).build();
    }*/

}
