package com.security.securitylearn.config;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author qian
 */
public class UserDetailsRepository {

    private Map<String, UserDetails> users = new HashMap<>();

    public void createUser(UserDetails user) {
        users.putIfAbsent(user.getUsername(), user);
    }

    public void updateUser(UserDetails user) {
        users.put(user.getUsername(), user);
    }

    public void deleteUser(String s) {
        users.remove(s);
    }

    public void changePassword(String oldPassword, String newPassword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new AccessDeniedException("不能修改密码，当前用户还没有登录！");
        }
        String username = authentication.getName();
        UserDetails userDetails = users.get(username);

        if (userDetails == null) {
            throw new IllegalStateException("用户不存在！");
        }

        // 实现自定义的修改密码的逻辑

    }

    public boolean userExists(String s) {
        return users.containsKey(s);
    }

    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserDetails userDetails = users.get(s);
        if (userDetails == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return userDetails;
    }
}
