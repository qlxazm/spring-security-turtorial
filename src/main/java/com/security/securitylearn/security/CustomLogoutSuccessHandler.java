package com.security.securitylearn.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.securitylearn.common.ResponseUtils;
import com.security.securitylearn.common.Rest;
import com.security.securitylearn.jwt.JwtTokenPair;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.ResponseUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author qian
 */
@Slf4j
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private RedisTemplate<String, JwtTokenPair> redis;

    public CustomLogoutSuccessHandler(RedisTemplate<String, JwtTokenPair> redis) {
        this.redis = redis;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();
        String username = user.getUsername();

        // 成功登出之后清除token
        redis.delete(username);

        log.info("用户{}，退出成功啦！", username);
    }

    private static void responseJsonWriter(HttpServletResponse response, Rest rest) throws IOException{
        ResponseUtils.responseJsonWriter(response, rest);
    }
}
