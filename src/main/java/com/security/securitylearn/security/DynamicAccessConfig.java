package com.security.securitylearn.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 动态配置权限的配置类
 * @author qian
 * @date 2019/12/15 10:46
 */
@Configuration
public class DynamicAccessConfig {

    @Bean
    public RequestMatcherGenerator requestMatcherGenerator() {
        return resources -> resources.stream()
                .map(resource -> new AntPathRequestMatcher(resource.getPattern(), resource.getMethod()))
                .collect(Collectors.toSet());
    }

    @Bean
    public FilterInvocationSecurityMetadataSource filterInvocationSecurityMetadataSource() {
        return new DynamicFilterInvocationSecurityMetadataSource();
    }

    @Bean
    public RoleVoter roleVoter(){
        return new RoleVoter();
    }

    @Bean
    public AccessDecisionManager affirmativeBased(List<AccessDecisionVoter<?>> decisionVoters) {
        return new AffirmativeBased(decisionVoters);
    }
}
