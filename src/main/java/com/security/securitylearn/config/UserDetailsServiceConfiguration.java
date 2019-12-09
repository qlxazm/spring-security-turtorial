package com.security.securitylearn.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;

/**
 * @author qian
 */
@Slf4j
@Configuration
public class UserDetailsServiceConfiguration {

    @Bean
    public UserDetailsRepository userDetailsRepository() {
        UserDetailsRepository repository = new UserDetailsRepository();

        // 这里创建了一个默认的用户。用户名为admin，密码为admin。其中密码加上前缀{noop}代表不加密密码，使用明文
        // 一定要设置authorities，它代表用户所具有的权限
        UserDetails defaultUser = User.withUsername("admin").password("{noop}admin").authorities(AuthorityUtils.NO_AUTHORITIES).build();
        repository.createUser(defaultUser);
        return repository;
    }

    @Bean
    @ConditionalOnBean({UserDetailsRepository.class})
    public UserDetailsManager userDetailsManager(UserDetailsRepository repository) {
        // 将具体的操作代理到userDetailsRepository上
        return new UserDetailsManager() {
            @Override
            public void createUser(UserDetails userDetails) {
                repository.createUser(userDetails);
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
