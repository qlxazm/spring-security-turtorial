package com.security.securitylearn.repository;

import com.security.securitylearn.entity.SysUser;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<SysUser, Integer> {
    /**
     * 根据用户名查询用户
     * @return
     */
    SysUser findByUsername(String username);

    /**
     * 根据用户名删除用户
     * @param username
     */
    void deleteByUsername(String username);
}
