package com.security.securitylearn.security;

import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Set;

/**
 * 定义一个函数式接口，用于将MetaResource转换成RequestMatcher
 * RequestMatcher则用来匹配请求的uri
 * @author qian
 * @date 2019/12/15 11:06
 */
@FunctionalInterface
public interface RequestMatcherGenerator {

    Set<RequestMatcher> convertToRequestMatcher(Set<MetaResource> resources);
}
