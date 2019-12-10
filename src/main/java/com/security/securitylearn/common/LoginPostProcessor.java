package com.security.securitylearn.common;

import com.security.securitylearn.enums.LoginTypeEnum;

import javax.servlet.ServletRequest;

public interface LoginPostProcessor {

    /**
     * 返回登录方式枚举
     * @return
     */
    LoginTypeEnum getLoginType();

    /**
     * 获取用户名
     * @param request
     * @return
     */
    String obtainUsername(ServletRequest request);

    /**
     * 获取密码
     * @param request
     * @return
     */
    String obtainPassword(ServletRequest request);
}
