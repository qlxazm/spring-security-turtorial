package com.security.securitylearn.security;

import com.security.securitylearn.service.MetaResourceService;
import com.security.securitylearn.service.RoleService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Set;

/**
 * 实现动态配置权限
 * @author qian
 * @date 2019/12/15 10:45
 */
public class DynamicFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

    @Resource
    private RequestMatcherGenerator requestMatcherGenerator;
    @Resource
    private MetaResourceService metaResourceService;
    @Resource
    private RoleService roleService;

    /**
     * 这个方法需要根据对象o来获取请求的URI
     * 在根根URI查询数据库，得到访问这个URI所需要的角色
     * @param o
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        // 获取request
        final HttpServletRequest request = ((FilterInvocation) o).getRequest();

        // 将获取到的request进行匹配
        Set<RequestMatcher> requestMatchers = requestMatcherGenerator.convertToRequestMatcher(metaResourceService.getPatternAndResources());
        AntPathRequestMatcher antPathRequestMatcher = (AntPathRequestMatcher) requestMatchers.stream()
                .filter(requestMatcher -> requestMatcher.matches(request))
                .findAny()
                .orElseThrow(() -> new AccessDeniedException("这是一次非法访问！"));

        // 根据pattern获取该pattern对应的uri被授予的角色
        String pattern = antPathRequestMatcher.getPattern();
        Set<String> roles = roleService.getRolesByPattern(pattern);

        return CollectionUtils.isEmpty(roles) ? null : SecurityConfig.createList(roles.toArray(new String[roles.size()]));
    }

    /**
     * 这个方法需要返回所有的角色
     * @return
     */
    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        Set<String> roles = roleService.getAllRoles();
        return CollectionUtils.isEmpty(roles) ? null : SecurityConfig.createList(roles.toArray(new String[roles.size()]));
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return FilterInvocation.class.isAssignableFrom(aClass);
    }
}
