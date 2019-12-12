package com.security.securitylearn.jwt;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author qian
 * @date 2019/12/12 14:50
 *  * @ConditionalOnProperty(prefix = "jwt.config", name = "enabled") 表示当配置项jwt.config.enabled为true时才使用这个java配置类
 */
@EnableConfigurationProperties(JwtProperties.class)
@ConditionalOnProperty(prefix = "jwt.config", name = "enabled")
@Configuration
public class JwtConfig {

    @Bean
    public JwtTokenStorage jwtTokenStorage() {
        return new JwtTokenRedisStorage();
    }

    @Bean
    public JwtTokenGenerator tokenGenerator(JwtTokenStorage jwtTokenStorage, JwtProperties properties) {
        return new JwtTokenGenerator(jwtTokenStorage, properties);
    }
}
