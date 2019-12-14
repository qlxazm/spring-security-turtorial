package com.security.securitylearn.jwt;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.security.securitylearn.common.JwtPayloadBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;

import org.springframework.security.authentication.event.AuthenticationFailureCredentialsExpiredEvent;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;
import org.springframework.util.Assert;

import java.security.interfaces.RSAPublicKey;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;

/**
 * 生成JWT的工具类
 * @author qian
 * @date 2019/12/12 14:39
 */
@Slf4j
public class JwtTokenGenerator {

    private static final String JWT_EXP_KEY = "exp";

    private JwtPayloadBuilder tokenBuilder = new JwtPayloadBuilder();

    private JwtTokenStorage jwtTokenStorage;

    private JwtProperties jwtProperties;

    private KeyPair keyPair;

    public JwtTokenGenerator(JwtTokenStorage jwtTokenStorage, JwtProperties jwtProperties) {
        this.jwtTokenStorage = jwtTokenStorage;
        this.jwtProperties = jwtProperties;
        this.keyPair = new KeyPairFactory().create(jwtProperties.getKeyLocation(), jwtProperties.getKeyAlias(), jwtProperties.getKeyPass());
    }

    /**
     * 生成jwtToken
     * @param aud          jwt接收者
     * @param exp          jwt的过期时间
     * @param roles        角色列表
     * @param additional   额外的信息
     * @return             jwt字符串
     */
    private String jwtToken(String aud, int exp, Set<String> roles, Map<String, String> additional) {
        String payload = tokenBuilder
                .iss(jwtProperties.getIss())
                .sub(jwtProperties.getSub())
                .aud(aud)
                .expDays(exp)
                .roles(roles)
                .additional(additional)
                .builder();

        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RsaSigner signer = new RsaSigner(privateKey);
        return JwtHelper.encode(payload, signer).getEncoded();
    }

    /**
     * 生成token对
     * @param aud        jwt的接收者
     * @param roles      角色集合
     * @param additional 额外的信息
     * @return           accessToken + refreshToken组成的Token对
     */
    public JwtTokenPair jwtTokenPair(String aud, Set<String> roles, Map<String, String> additional) {
        String accessToken = jwtToken(aud, jwtProperties.getAccessExpDays(), roles, additional);
        String refreshToken = jwtToken(aud, jwtProperties.getRefreshExpDays(), roles, additional);

        JwtTokenPair tokenPair = new JwtTokenPair();
        tokenPair.setAccessToken(accessToken);
        tokenPair.setRefreshToken(refreshToken);

        // 将生成的 token对 缓存下来
        jwtTokenStorage.put(tokenPair, aud);

        return tokenPair;
    }

    /**
     * 解密token并判断是否过期
     * @param token
     * @return
     */
    public JSONObject decodeAndVerify(String token) throws JwtExpiredException{
        Assert.hasText(token, "token不能为空");

        // 获取公钥进行解密
        RSAPublicKey rsaPublicKey = (RSAPublicKey) keyPair.getPublic();
        SignatureVerifier rsaVerifier = new RsaVerifier(rsaPublicKey);
        Jwt jwt = JwtHelper.decodeAndVerify(token, rsaVerifier);

        String claims = jwt.getClaims();
        JSONObject jsonObject = JSONUtil.parseObj(claims);

        String exp = jsonObject.getStr(JWT_EXP_KEY);

        if (isExpired(exp)) {
            throw new JwtExpiredException();
        }
        return jsonObject;
    }

    /**
     * 是否过期
     * @param exp
     * @return
     */
    private boolean isExpired(String exp) {
        return LocalDateTime.now().isAfter(LocalDateTime.parse(exp, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}
