package com.security.securitylearn;

import com.security.securitylearn.jwt.JwtTokenPair;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author qian
 * @date 2019/12/13 17:30
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private RedisTemplate<String, JwtTokenPair> redis;

    @Test
    public void connectTest() {
        RedisConnection connection = redisConnectionFactory.getConnection();
        log.info(new String(connection.get("bookName".getBytes())));
    }

    /*@Test
    public void setTest() {
        JwtTokenPair pair = new JwtTokenPair();
        pair.setRefreshToken("refreshToken");
        pair.setAccessToken("accessToken");
        redis.opsForValue().set("qlx", pair);
    }*/

    @Test
    public void getTest() {
        JwtTokenPair pair = redis.opsForValue().get("qlx");
        log.info("refreshToken:{}, accessToken", pair.getRefreshToken(), pair.getAccessToken());
    }
}
