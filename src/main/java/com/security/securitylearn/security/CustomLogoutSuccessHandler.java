package com.security.securitylearn.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.security.securitylearn.common.Rest;
import lombok.extern.slf4j.Slf4j;
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
    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();
        String username = user.getUsername();
        log.info("用户{}，退出成功啦！", username);
    }

    private static void responseJsonWriter(HttpServletResponse response, Rest rest) throws IOException{
        response.setStatus(HttpServletResponse.SC_OK);
        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // 使用jackson
        ObjectMapper objectMapper = new ObjectMapper();
        String restBody = objectMapper.writeValueAsString(rest);
        PrintWriter writer = response.getWriter();
        writer.print(restBody);
        writer.flush();
        writer.close();
    }
}
