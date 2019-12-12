package com.security.securitylearn.jwt;

/**
 * @author qian
 * @date 2019/12/12 14:43
 */
public interface JwtTokenStorage {

    /**
     * 缓存token对
     * @param jwtTokenPair token对
     * @param username     用户名
     * @return             缓存的token对
     */
    JwtTokenPair put(JwtTokenPair jwtTokenPair, String username);

    /**
     * 获取token对
     * @param username 用户名
     * @return         token对
     */
    JwtTokenPair get(String username);

    void expire(String username);
}
