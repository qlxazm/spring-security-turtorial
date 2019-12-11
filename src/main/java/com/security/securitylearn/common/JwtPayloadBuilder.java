package com.security.securitylearn.common;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 构建jwt所使用的工具类
 * @author qian
 * @date 2019/12/11 14:21
 */
public class JwtPayloadBuilder {

    private Map<String, String> payload = new HashMap<>();

    /**
     * 附加的属性
     */
    private Map<String, String> additional;

    /**
     * jwt签发者
     */
    private String iss;

    /**
     * jwt所面向的用户
     */
    private String sub;

    /**
     * jwt的接收方
     */
    private String aud;

    /**
     * jwt的过期时间
     */
    private LocalDateTime exp;

    /**
     * jwt的签发时间
     */
    private LocalDateTime iat = LocalDateTime.now();

    /**
     * 权限集合
     */
    private Set<String> roles = new HashSet<>();

    /**
     * jwt的唯一身份标识
     */
    private String jti = IdUtil.simpleUUID();

    public JwtPayloadBuilder iss(String iss) {
        this.iss = iss;
        return this;
    }

    public JwtPayloadBuilder sub(String sub) {
        this.sub = sub;
        return this;
    }

    public JwtPayloadBuilder aud(String aud) {
        this.aud = aud;
        return this;
    }

    public JwtPayloadBuilder roles(Set<String> roles) {
        this.roles = roles;
        return this;
    }

    public JwtPayloadBuilder expDays(int days) {
        Assert.isTrue(days > 0, "jwt的过期时间必须大于签发时间！");
        this.exp = this.iat.plusDays(days);
        return this;
    }

    public JwtPayloadBuilder additional(Map<String, String> additional) {
        this.additional = additional;
        return this;
    }

    public String builder() {
        payload.put("iss", this.iss);
        payload.put("sub", this.sub);
        payload.put("aud", this.aud);
        payload.put("exp", this.exp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        payload.put("iat", this.iat.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        payload.put("jti", this.jti);

        if (!CollectionUtils.isEmpty(this.additional)) {
            payload.putAll(this.additional);
        }

        payload.put("roles", JSONUtil.toJsonStr(this.roles));

        return JSONUtil.toJsonStr(payload);
    }
}
