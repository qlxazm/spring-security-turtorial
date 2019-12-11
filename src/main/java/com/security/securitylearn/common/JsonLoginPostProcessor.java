package com.security.securitylearn.common;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.security.securitylearn.enums.LoginTypeEnum;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY;
import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY;

/**
 * @author qian
 */
public class JsonLoginPostProcessor implements LoginPostProcessor {

    private static ThreadLocal<String> passwordThreadLocal = new ThreadLocal<>();

    @Override
    public LoginTypeEnum getLoginType() {
        return LoginTypeEnum.JSON;
    }

    @Override
    public String obtainUsername(ServletRequest request) {
        HttpServletRequestWrapper httpServletRequestWrapper = new HttpServletRequestWrapper((HttpServletRequest) request);
        String body = RequestUtils.obtainBody(httpServletRequestWrapper);

        JSONObject jsonObject = JSONUtil.parseObj(body);
        passwordThreadLocal.set(jsonObject.getStr(SPRING_SECURITY_FORM_PASSWORD_KEY));

        return jsonObject.getStr(SPRING_SECURITY_FORM_USERNAME_KEY);
    }

    @Override
    public String obtainPassword(ServletRequest request) {
        String s = passwordThreadLocal.get();
        passwordThreadLocal.remove();
        return s;
    }
}
