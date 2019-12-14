package com.security.securitylearn.jwt;

import com.security.securitylearn.common.RequestUtils;
import com.security.securitylearn.common.ResponseUtils;
import com.security.securitylearn.common.RestBody;
import com.security.securitylearn.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author qian
 * @date 2019/12/12 14:50
 *  * @ConditionalOnProperty(prefix = "jwt.config", name = "enabled") 表示当配置项jwt.config.enabled为true时才使用这个java配置类
 */
@Slf4j
@EnableConfigurationProperties(JwtProperties.class)
@ConditionalOnProperty(prefix = "jwt.config", name = "enabled")
@Configuration
public class JwtConfig {

    @Bean
    public JwtTokenStorage jwtTokenStorage(RedisTemplate<String, JwtTokenPair> redis) {
        return new JwtTokenRedisStorage(redis);
    }

    @Bean
    public JwtTokenGenerator tokenGenerator(JwtTokenStorage jwtTokenStorage, JwtProperties properties) {
        return new JwtTokenGenerator(jwtTokenStorage, properties);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtTokenStorage jwtTokenStorage, JwtTokenGenerator jwtTokenGenerator, SysUserService sysUserService) {
        return new JwtAuthenticationFilter(jwtTokenGenerator, jwtTokenStorage, sysUserService);
    }

    /**
     * 登录成功之后返回 jwt
     * @param jwtTokenGenerator
     * @return
     */
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(JwtTokenGenerator jwtTokenGenerator) {
        return (request, response, authentication) -> {
            //response.isCommitted()可检查看服务端是否已将数据输出到客户端.如果返回值是TRUE则已将数据输出到客户端,是FALSE则还没有
            if (response.isCommitted()){
                log.debug("响应已经committed");
                return;
            }
            Map<String, Object> map = new HashMap<>();
            map.put("time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            map.put("flag", "success_login");

            User user = (User) authentication.getPrincipal();

            Collection<GrantedAuthority> authorities = user.getAuthorities();
            Set<String> roles = new HashSet<>();
            if (!CollectionUtils.isEmpty(authorities)) {
                for (GrantedAuthority authority : authorities) {
                    roles.add(authority.getAuthority());
                }
            }

            // 生成 jwtTokenPair
            JwtTokenPair tokenPair = jwtTokenGenerator.jwtTokenPair(user.getUsername(), roles, null);

            map.put("access_token", tokenPair.getAccessToken());
            map.put("refresh_token", tokenPair.getRefreshToken());

            ResponseUtils.responseJsonWriter(response, RestBody.okData(map, "登录成功！"));
        };
    }

    /**
     * 认证失败
     * @param jwtTokenGenerator
     * @return
     */
    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler(JwtTokenGenerator jwtTokenGenerator) {
        return (request, response, authentication) -> {
            if (response.isCommitted()){
                log.debug("响应已经committed");
                return;
            }
            Map<String, Object> map = new HashMap<>();
            map.put("time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            map.put("flag", "failure_login");

            ResponseUtils.responseJsonWriter(response, RestBody.build(HttpStatus.UNAUTHORIZED.value(), map, "认证失败！", "-9999"));
        };
    }
}
