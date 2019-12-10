package com.security.securitylearn.config;

import com.security.securitylearn.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;

/**
 * @author qian
 */
@Slf4j
@Configuration
public class UserDetailsServiceConfiguration {

    @Autowired
    private SysUserService sysUserService;

    @Bean
    public UserDetailsRepository userDetailsRepository() {
        UserDetailsRepository repository = new UserDetailsRepository(sysUserService);

        // 这里创建了一个默认的用户。用户名为admin，密码为admin。
        // 一定要设置authorities，它代表用户所具有的权限
//        repository.createUser();
        return repository;
    }

    @Bean
    @ConditionalOnBean({UserDetailsRepository.class})
    public UserDetailsManager userDetailsManager(UserDetailsRepository repository) {
        // 将具体的操作代理到userDetailsRepository上
        return new UserDetailsManager() {
            @Override
            public void createUser(UserDetails userDetails) {
                repository.createUser();
            }

            @Override
            public void updateUser(UserDetails userDetails) {
                repository.updateUser(userDetails);
            }

            @Override
            public void deleteUser(String s) {
                repository.deleteUser(s);
            }

            @Override
            public void changePassword(String s, String s1) {
                repository.changePassword(s, s1);
            }

            @Override
            public boolean userExists(String s) {
                return repository.userExists(s);
            }

            @Override
            public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
                return repository.loadUserByUsername(s);
            }
        };
    }
}
