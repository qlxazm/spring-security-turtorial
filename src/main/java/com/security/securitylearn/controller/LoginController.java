package com.security.securitylearn.controller;

import com.security.securitylearn.common.Rest;
import com.security.securitylearn.common.RestBody;
import com.security.securitylearn.entity.SysUser;
import com.security.securitylearn.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private SysUserService sysUserService;

    /**
     * 登录失败处理逻辑
     * @return
     */
    @PostMapping("/failure")
    public Rest loginFailure() {
        return RestBody.failure(HttpStatus.UNAUTHORIZED.value(), "登录失败拉！");
    }

    /**
     * 处理登录成功的逻辑
     * @return
     */
    @PostMapping("/success")
    public Rest loginSuccess() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();
        SysUser sysUser = sysUserService.getSysUserByUserName(username);
        return RestBody.okData(sysUser, "登录成功啦！");
    }
}
