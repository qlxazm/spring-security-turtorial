package com.security.securitylearn.service;

import java.util.Set;

/**
 * @author yd
 * @date 2019/12/15 10:59
 */
public interface RoleService {

    Set<String> getRolesByPattern(String pattern);

    Set<String> getAllRoles();

}
