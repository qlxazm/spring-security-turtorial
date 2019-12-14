package com.security.securitylearn.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

@Slf4j
public class RequestUtils {

    /**
     * 获取request中的数据
     * @param requestWrapper
     * @return
     */
    public static String obtainBody(HttpServletRequestWrapper requestWrapper) {

        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(requestWrapper.getInputStream(), "UTF-8"));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            log.error("读取request中的数据出错！");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    log.error("关闭BufferedReader出错！");
                }
            }
        }
        return builder.toString();


        /*String line = null;
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();


        try {
            reader = requestWrapper.getReader();
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            log.error("读取request中的数据出错！");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    log.error("关闭BufferedReader出错！");
                }
            }
        }
        return builder.toString();*/
    }
}
