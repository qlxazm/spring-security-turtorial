package com.security.securitylearn.service.impl;

import com.security.securitylearn.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author yd
 * @date 2019/12/15 10:59
 */
@Service
public class RoleServiceImpl implements RoleService {

    /**
     * 这里应该是根据参数pattern从数据库中查询出角色信息
     * @param pattern
     * @return
     */
    @Override
    public Set<String> getRolesByPattern(String pattern) {

        Set<String> roles = new HashSet<>();
        roles.add("ROLE_ADMIN");
        roles.add("ROLE_USER");

        return roles;
    }

    /**
     * 这里应该是从数据库中查询出所有可用的角色
     * @return
     */
    @Override
    public Set<String> getAllRoles() {
        Set<String> roles = new HashSet<>();
        roles.add("ROLE_ADMIN");
        roles.add("ROLE_USER");

        return roles;
    }
}
