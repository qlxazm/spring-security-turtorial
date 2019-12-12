package com.security.securitylearn.keytool;

import org.springframework.core.io.ClassPathResource;

import javax.crypto.Cipher;
import java.io.BufferedInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

/**
 * 参考微博：https://www.jb51.net/article/145274.htm
 * 使用 keytool -genkey -alias signLegal -keystore examplestanstore2 -validity 1800 -keyalg RSA 生成证书库
 * 使用 keytool -export -keystore examplestanstore2 -alias signLegal -file stanSmith.crt -rfc 导出公钥文件
 * @author qian
 * @date 2019/12/12 16:39
 */
public class RSAEntry {

    /**
     * 证书库文件位置
     */
    private static final String KEY_STORE_FILE = "examplestanstore2";

    /**
     * 公钥文件
     */
    private static final String PUBLIC_KEY_FILE = "stanSmith.crt";

    /**
     * 证书别名
     */
    private static final String ALIAS = "signLegal";

    /**
     * 证书库密码
     */
    private static final String STORE_PASS = "123456";

    /**
     * 证书密码
     */
    private static final String KEY_PASS = "qlxazm";

    /**
     * 将二进制转换成十六进制
     * @param bytes
     * @return
     */
    public static String bytesToHexString(byte[] bytes) {
        if (bytes == null) {
            return "null";
        }
        StringBuilder builder = new StringBuilder();
        for (byte c : bytes) {
            int b = 0xF & c >> 4;
            builder.append("0123456789abcdef".charAt(b));
            b = 0xF & c;
            builder.append("0123456789abcdef".charAt(b));
        }
        return builder.toString();
    }


    public static void main(String[] args) {
        try {
            // 1、从证书库中读取私钥
            ClassPathResource classPathResource = new ClassPathResource(KEY_STORE_FILE);
            KeyStore ks = KeyStore.getInstance("JKS");
            BufferedInputStream ksbufin = new BufferedInputStream(classPathResource.getInputStream());

            ks.load(ksbufin, STORE_PASS.toCharArray());
            PrivateKey privateKey = (PrivateKey) ks.getKey(ALIAS, KEY_PASS.toCharArray());

            // 2、取出公钥
            ClassPathResource classPathResource1 = new ClassPathResource(PUBLIC_KEY_FILE);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Certificate cert = cf.generateCertificate(classPathResource1.getInputStream());
            PublicKey publicKey = cert.getPublicKey();

            // 3、使用公钥进行加密、解密
            String data = "这是要被加密的字符串";
            // 构建加解密类
            Cipher cipher = Cipher.getInstance("RSA");
            // 设置为加密模式
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            // 加密
            byte[] encodedData = cipher.doFinal(data.getBytes());
            System.out.println("加密后的内容：" + bytesToHexString(encodedData));
            // 设置为解密状态
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] decodedData = cipher.doFinal(encodedData);
            System.out.println("解密之后的数据：" + new String(decodedData));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
