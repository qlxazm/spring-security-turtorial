package com.security.securitylearn.jwt;

/**
 * 用redis缓存产生的token对
 * @author qian
 * @date 2019/12/12 14:48
 */
public class JwtTokenRedisStorage implements JwtTokenStorage {
    @Override
    public JwtTokenPair put(JwtTokenPair jwtTokenPair, String username) {
        return null;
    }

    @Override
    public JwtTokenPair get(String username) {
        return null;
    }

    @Override
    public void expire(String username) {

    }
}
