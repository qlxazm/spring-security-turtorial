package com.security.securitylearn.service;

import com.security.securitylearn.entity.SysUser;

public interface SysUserService {
    /**
     * 根据用户名查询用户信息
     * @param username
     * @return
     */
    SysUser getSysUserByUserName(String username);

    /**
     * 更新用户信息
     * @param user
     */
    void updateSysUser(SysUser user);

    /**
     * 删除用户
     * @param username
     */
    void removeSysUserByUserName(String username);
}
