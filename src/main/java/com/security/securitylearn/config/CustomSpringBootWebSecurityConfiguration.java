package com.security.securitylearn.config;

import com.security.securitylearn.common.JsonLoginPostProcessor;
import com.security.securitylearn.common.LoginPostProcessor;
import com.security.securitylearn.filter.PreLoginFilter;
import com.security.securitylearn.jwt.JwtAuthenticationFilter;
import com.security.securitylearn.jwt.JwtTokenPair;
import com.security.securitylearn.security.CustomLogoutHandler;
import com.security.securitylearn.security.CustomLogoutSuccessHandler;
import com.security.securitylearn.security.SimpleAccessDeniedHandler;
import com.security.securitylearn.security.SimpleAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collection;

/**
 * @author qian
 */
@Configuration
@ConditionalOnClass(WebSecurityConfigurerAdapter.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class CustomSpringBootWebSecurityConfiguration {

    private static final String LOGIN_PROCESS_URL = "/process";


    @Bean
    public LoginPostProcessor jsonLoginPostProcessor() {
        return new JsonLoginPostProcessor();
    }

    @Bean
    public PreLoginFilter preLoginFilter(Collection<LoginPostProcessor> processors) {
        return new PreLoginFilter(LOGIN_PROCESS_URL, processors);
    }


    @Configuration
    @Order(SecurityProperties.BASIC_AUTH_ORDER)
    static class DefaultConfigurerAdapter extends WebSecurityConfigurerAdapter{

        @Autowired
        private PreLoginFilter preLoginFilter;

        @Autowired
        private AuthenticationSuccessHandler authenticationSuccessHandler;

        @Autowired
        private AuthenticationFailureHandler authenticationFailureHandler;

        @Autowired
        private RedisTemplate<String, JwtTokenPair> redis;

        @Autowired
        private JwtAuthenticationFilter jwtAuthenticationFilter;

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            super.configure(auth);
        }

        @Override
        public void configure(WebSecurity web) throws Exception {
            super.configure(web);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable()
                    .cors()
                    .and()
                    // 在使用jwt进行验证后，要将session生成策略改成无状态策略
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    // 定义异常处理
                    .exceptionHandling().accessDeniedHandler(new SimpleAccessDeniedHandler()).authenticationEntryPoint(new SimpleAuthenticationEntryPoint())
                    .and()
                    // 定义访问权限部分
                    .authorizeRequests()
                    .antMatchers("/jwt/test").hasRole("ADMIN")
                    .anyRequest().authenticated()
                    .and()
                    .addFilterBefore(preLoginFilter, UsernamePasswordAuthenticationFilter.class)
                    // 添加处理jwt的过滤器
                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                    .formLogin()
                    .loginProcessingUrl(LOGIN_PROCESS_URL)
                    .successHandler(authenticationSuccessHandler)
                    .failureHandler(authenticationFailureHandler)
                    .and()
                    .logout()
                    .addLogoutHandler(new CustomLogoutHandler())
                    .logoutSuccessHandler(new CustomLogoutSuccessHandler(redis));
        }
    }
}
