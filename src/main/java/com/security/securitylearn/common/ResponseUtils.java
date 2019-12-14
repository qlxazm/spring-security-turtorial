package com.security.securitylearn.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author qian
 * @date 2019/12/12 18:15
 */
@Slf4j
public class ResponseUtils {

    /**
     *
     * @param response
     * @param rest
     * @throws IOException
     */
    public static void responseJsonWriter(HttpServletResponse response, Rest rest) throws IOException {

        if (response.isCommitted()) {
            return;
        }

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
