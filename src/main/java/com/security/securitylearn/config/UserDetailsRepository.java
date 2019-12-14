package com.security.securitylearn.config;

import com.security.securitylearn.entity.SysUser;
import com.security.securitylearn.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Objects;

/**
 * @author qian
 */
public class UserDetailsRepository {

    private SysUserService sysUserService;

    public UserDetailsRepository(SysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }

    public void createUser() {
        SysUser sysUser = new SysUser();
        sysUser.setUsername("admin");
        sysUser.setEncodePassword("admin");
        sysUser.setAge(18);
        sysUserService.updateSysUser(sysUser);
    }

    public void updateUser(UserDetails user) {
        SysUser sysUser = sysUserService.getSysUserByUserName(user.getUsername());
        sysUser.setAge(18);
        sysUser.setEncodePassword(user.getPassword());
        sysUserService.updateSysUser(sysUser);
    }

    public void deleteUser(String s) {
        sysUserService.removeSysUserByUserName(s);
    }

    public void changePassword(String oldPassword, String newPassword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new AccessDeniedException("不能修改密码，当前用户还没有登录！");
        }
        String username = authentication.getName();
        UserDetails userDetails = loadUserByUsername(username);

        if (userDetails == null) {
            throw new IllegalStateException("用户不存在！");
        }

        // 自定义的修改密码的逻辑
        if (!Objects.equals(oldPassword, userDetails.getPassword())) {
            throw new IllegalStateException("旧密码与数据库中的密码不一致!");
        }
        SysUser sysUser = sysUserService.getSysUserByUserName(username);
        sysUser.setEncodePassword(newPassword);
        sysUserService.updateSysUser(sysUser);
    }

    public boolean userExists(String s) {
        return Objects.nonNull(sysUserService.getSysUserByUserName(s));
    }

    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        SysUser sysUser = sysUserService.getSysUserByUserName(s);
        if (Objects.nonNull(sysUser)) {
            return User.withUsername(sysUser.getUsername())
                    .password(sysUser.getEncodePassword())
                    .authorities(AuthorityUtils.createAuthorityList("ROLE_ADMIN"))
                    .build();
        }
        throw new UsernameNotFoundException("username:" + s + "not found");
    }
}
