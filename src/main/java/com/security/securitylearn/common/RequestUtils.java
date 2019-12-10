package com.security.securitylearn.common;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.IOException;

@Slf4j
public class RequestUtils {

    /**
     * 获取request中的数据
     * @param requestWrapper
     * @return
     */
    public static String obtainBody(HttpServletRequestWrapper requestWrapper) {
        String line = null;
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
        return builder.toString();
    }
}
