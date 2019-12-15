package com.security.securitylearn.security;

import lombok.Data;

/**
 * 用来封装从数据库中查询出来的元数据
 * @author qian
 * @date 2019/12/15 10:52
 */
@Data
public class MetaResource {

    /**
     * uri的模式，例如：/jwt/test/*
     */
    private String pattern;

    /**
     * 访问这个uri所用的请求方式，比如：POST、GET
     */
    private String method;
}
