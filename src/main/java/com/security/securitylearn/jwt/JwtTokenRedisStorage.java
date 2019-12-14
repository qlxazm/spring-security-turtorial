package com.security.securitylearn.jwt;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * 用redis缓存产生的token对
 * @author qian
 * @date 2019/12/12 14:48
 */
public class JwtTokenRedisStorage implements JwtTokenStorage {

    private RedisTemplate<String, JwtTokenPair> redis;

    public JwtTokenRedisStorage(RedisTemplate<String, JwtTokenPair> redis) {
        this.redis = redis;
    }

    @Override
    public JwtTokenPair put(JwtTokenPair jwtTokenPair, String username) {
        redis.opsForValue().set(username, jwtTokenPair);
        return jwtTokenPair;
    }

    @Override
    public JwtTokenPair get(String username) {
        JwtTokenPair pair = redis.opsForValue().get(username);
        return pair;
    }

    @Override
    public void expire(String username) {

    }
}
