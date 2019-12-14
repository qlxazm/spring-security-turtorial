package com.security.securitylearn.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qian
 * @date 2019/12/14 14:58
 */
@RestController
@RequestMapping("/jwt")
public class JwtController {

    @RequestMapping("test")
    public String test() {
        return "jwt测试通过";
    }
}
