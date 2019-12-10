package com.security.securitylearn.service.impl;

import com.security.securitylearn.entity.SysUser;
import com.security.securitylearn.repository.UserRepository;
import com.security.securitylearn.service.SysUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private UserRepository userRepository;

    @Override
    public SysUser getSysUserByUserName(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void updateSysUser(SysUser user) {
        userRepository.save(user);
    }

    @Override
    public void removeSysUserByUserName(String username) {
        userRepository.deleteByUsername(username);
    }
}
