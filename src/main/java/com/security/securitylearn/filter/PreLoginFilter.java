package com.security.securitylearn.filter;

import com.security.securitylearn.common.LoginPostProcessor;
import com.security.securitylearn.common.ParameterRequestWrapper;
import com.security.securitylearn.enums.LoginTypeEnum;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY;
import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY;

/**
 * @author qian
 */
public class PreLoginFilter extends GenericFilterBean {

    /**
     * 从request中获取登录方式的key
     */
    private static final String LOGIN_TYPE_KEY = "login_type";

    /**
     * 登陆方式与登录处理逻辑的映射表
     */
    private Map<LoginTypeEnum, LoginPostProcessor> processors = new HashMap<>();

    private RequestMatcher matcher;

    public PreLoginFilter(String loginProcessUrl, Collection<LoginPostProcessor> postProcessors) {
        Assert.notNull(loginProcessUrl, "登录处理的接口不能为空");
        matcher = new AntPathRequestMatcher(loginProcessUrl, "POST");
        LoginPostProcessor defaultProcessor = defaultLoginPostProcessor();
        processors.put(defaultProcessor.getLoginType(), defaultProcessor);

        if (!CollectionUtils.isEmpty(postProcessors)) {
            postProcessors.forEach(processor -> processors.put(processor.getLoginType(), processor));
        }
    }

    /**
     * 生成一个默认的PostProcessor
     * @return
     */
    private LoginPostProcessor defaultLoginPostProcessor() {
        return new LoginPostProcessor() {
            @Override
            public LoginTypeEnum getLoginType() {
                return LoginTypeEnum.FORM;
            }

            @Override
            public String obtainUsername(ServletRequest request) {
                return request.getParameter(SPRING_SECURITY_FORM_USERNAME_KEY);
            }

            @Override
            public String obtainPassword(ServletRequest request) {
                return request.getParameter(SPRING_SECURITY_FORM_PASSWORD_KEY);
            }
        };
    }

    /**
     * 从request中获取登录方式
     * @param request
     * @return
     */
    private LoginTypeEnum getLoginTypeFromRequest(HttpServletRequest request) {
        LoginTypeEnum[] types = LoginTypeEnum.values();
        String loginType = request.getParameter(LOGIN_TYPE_KEY);
        return types[Integer.parseInt(loginType)];
    }

    /**
     * @param servletRequest
     * @param servletResponse
     * @param filterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        ParameterRequestWrapper wrapper = new ParameterRequestWrapper((HttpServletRequest) servletRequest);
        if (matcher.matches((HttpServletRequest) servletRequest)) {

            LoginTypeEnum loginType = getLoginTypeFromRequest((HttpServletRequest)servletRequest);
            LoginPostProcessor postProcessor = processors.get(loginType);

            String username = postProcessor.obtainUsername(servletRequest);
            String password = postProcessor.obtainPassword(servletRequest);

            wrapper.setAttribute(SPRING_SECURITY_FORM_USERNAME_KEY, username);
            wrapper.setAttribute(SPRING_SECURITY_FORM_PASSWORD_KEY, password);
        }
        filterChain.doFilter(wrapper, servletResponse);
    }
}
