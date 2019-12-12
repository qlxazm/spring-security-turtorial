package com.security.securitylearn.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.security.*;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.RSAPublicKeySpec;

/**
 * 读取密钥文件生成密钥对
 * 获取到公钥和私钥对之后，可以用私钥进行加密，公钥进行解密
 * @author qian
 * @date 2019/12/12 14:07
 */
@Slf4j
public class KeyPairFactory {

    private KeyStore store;

    private final Object lock = new Object();

    /**
     * 生成公钥和私钥对
     * @param keyFilePath 密钥文件的路径
     * @param keyAlias    证书的别名
     * @param keyPass     访问证书需要的密码
     * @return            公钥 + 私钥 组成的密码对
     */
    public KeyPair create(String keyFilePath, String keyAlias, String keyPass) {
        ClassPathResource resource = new ClassPathResource(keyFilePath);
        char[] pem = keyPass.toCharArray();

        try {
            synchronized (lock) {
                if (store == null) {
                    store = KeyStore.getInstance("jks");
                    store.load(resource.getInputStream(), pem);
                }
            }
            RSAPrivateCrtKey key = (RSAPrivateCrtKey)store.getKey(keyAlias, pem);
            RSAPublicKeySpec spec = new RSAPublicKeySpec(key.getModulus(), key.getPublicExponent());
            PublicKey publicKey = KeyFactory.getInstance("rsa").generatePublic(spec);
            return new KeyPair(publicKey, key);

        } catch (Exception e) {
            throw new IllegalStateException("从密钥文件中加载密钥错误！" + resource + e);
        }
    }
}
