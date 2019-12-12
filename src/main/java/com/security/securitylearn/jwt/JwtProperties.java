package com.security.securitylearn.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.security.securitylearn.jwt.JwtProperties.JWT_PREFIX;

/**
 * 关于jwt的一些配置项
 * @author qian
 * @date 2019/12/12 14:27
 */
@Data
@ConfigurationProperties(prefix = JWT_PREFIX)
public class JwtProperties {

    static final String JWT_PREFIX = "jwt.config";

    /**
     * 配置项是否可用
     */
    private boolean enabled;

    /**
     * 密钥文件的位置
     */
    private String keyLocation;

    /**
     * 证书的别名
     */
    private String keyAlias;

    /**
     * 获取证书用的密码
     */
    private String keyPass;

    /**
     * jwt的签发者
     */
    private String iss;

    /**
     * jwt所面向的用户
     */
    private String sub;

    /**
     * accessToken的有效时间
     */
    private int accessExpDays;

    /**
     * refreshToken的有效时间
     */
    private int refreshExpDays;
}
