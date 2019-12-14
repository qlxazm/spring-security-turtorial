package com.security.securitylearn.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * AuthenticationEntryPoint接口用于处理匿名用户访问无权限资源时的异常
 * @author qian
 * @date 2019/12/14 9:49
 */
public class SimpleAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {

        Map<String, String> map = new HashMap<>();
        map.put("uri", request.getRequestURI());
        map.put("msg", "认证失败啦！");

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("utf-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ObjectMapper objectMapper = new ObjectMapper();
        String responseBody = objectMapper.writeValueAsString(map);

        PrintWriter printWriter = response.getWriter();
        printWriter.print(responseBody);
        printWriter.flush();
        printWriter.close();
    }
}
