/*
 * acooly.cn Inc.
 * Copyright (c) 2016 All Rights Reserved.
 * create by zhangpu
 * date:2016年4月26日
 *
 */
package com.acooly.openapi.framework.common.utils;

import com.acooly.core.utils.Encodes;
import com.acooly.core.utils.Strings;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import javax.crypto.Cipher;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.*;
import java.nio.charset.Charset;
import java.security.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * RAS 非对称加密工具类
 *
 * <p>基础说明：
 *
 * <p>
 * <li>不管明文长度是多少，RSA 生成的密文长度总是固定的。
 * <li>明文长度不能超过密钥长度。比如 Java 默认的RSA加密实现不允许明文长度超过密钥长度减去 11(单位是字节，也就是 byte)。也就是说，如果我们定义的密钥(我们可以通过
 * java.security.KeyPairGenerator.initialize(int keysize) 来定义密钥长度)长度为1024(单位是位，也就是
 * bit)，生成的密钥长度就是 1024位 / 8位/字节 = 128字节，那么我们需要加密的明文长度不能超过 128字节 - 11 字节 = 117字节。也就是说，我们最大能将 117
 * 字节长度的明文进行加密，否则会出问题(抛诸如 javax.crypto.IllegalBlockSizeException: Data must not be longer than
 * 53 bytes 的异常)。
 * <li>BC(Provider --> Security.addProvider(new BouncyCastleProvider());) 提供的加密算法能够支持到的 RSA
 * 明文长度最长为密钥长度。
 *
 * @author zhangpu
 */
public class RSA {

    public static final String SIGN_ALGO_SHA1 = "SHA1withRSA";
    public static final String SIGN_ALGO_MD5 = "MD5withRSA";
    public static final String DEFAULT_KEY_ALGO = "RSA";
    public static final String DEFAULT_CHARSET = "utf-8";

    public static final String KEY_STORE_JKS = "JKS";
    public static final String KEY_STORE_PKCS12 = "PKCS12";
    public static final String DEFAULT_KEY_STORE = KEY_STORE_PKCS12;

    /**
     * 私钥签名
     *
     * @param dataBytes          明文数据bytes
     * @param privateKey         私钥 参考 loadPrivateKey方法
     * @param signatureAlgorithm 签名算法 如：SHA1withRSA，MD5withRSA
     * @return 签名的二进制结果
     */
    public static byte[] sign(
            @NotNull byte[] dataBytes, @NotNull PrivateKey privateKey, @Null String signatureAlgorithm) {
        String sa = Strings.isBlankDefault(signatureAlgorithm, SIGN_ALGO_SHA1);
        try {
            Signature signature = Signature.getInstance(sa);
            signature.initSign(privateKey);
            signature.update(dataBytes);
            return signature.sign();
        } catch (Exception e) {
            throw new RuntimeException("sign with " + sa + " fail:" + e.getMessage());
        }
    }

    public static String signBase64(
            @NotNull String data,
            @NotNull PrivateKey privateKey,
            @Null String signatureAlgorithm,
            @Null String dataCharset) {
        byte[] result = sign(getBytes(data, dataCharset), privateKey, signatureAlgorithm);
        return Encodes.encodeBase64(result);
    }

    public static String signBase64(@NotNull String data, @NotNull PrivateKey privateKey) {
        return signBase64(data, privateKey, null, null);
    }

    public static String signHex(
            @NotNull String data,
            @NotNull PrivateKey privateKey,
            @Null String signatureAlgorithm,
            @Null String dataCharset) {
        byte[] result = sign(getBytes(data, dataCharset), privateKey, signatureAlgorithm);
        return Encodes.encodeHex(result);
    }

    public static String signHex(@NotNull String data, @NotNull PrivateKey privateKey) {
        return signHex(data, privateKey, null, null);
    }

    /**
     * 公钥验签
     *
     * @param data               明文数据bytes
     * @param signData           签名数据bytes
     * @param publicKey          公钥 参考 loadPublicKey(...)方法
     * @param signatureAlgorithm 签名算法
     * @return
     */
    public static boolean verify(
            byte[] data, byte[] signData, PublicKey publicKey, String signatureAlgorithm) {
        String sa = Strings.isBlankDefault(signatureAlgorithm, SIGN_ALGO_SHA1);
        try {
            Signature signature = Signature.getInstance(sa);
            signature.initVerify(publicKey);
            signature.update(data);
            return signature.verify(signData);
        } catch (Exception e) {
            throw new RuntimeException("verify with " + sa + " fail." + e.getMessage());
        }
    }

    public static boolean verifyBase64(
            String text,
            String signBase64,
            PublicKey publicKey,
            String signatureAlgorithm,
            String charset) {
        byte[] data = getBytes(text, charset);
        byte[] signData = Encodes.decodeBase64(signBase64);
        return verify(data, signData, publicKey, signatureAlgorithm);
    }

    public static boolean verifyBase64(String text, String signBase64, PublicKey publicKey) {
        return verifyBase64(text, signBase64, publicKey, null, null);
    }

    public static boolean verifyHex(
            String text, String signHex, PublicKey publicKey, String signatureAlgorithm, String charset) {
        byte[] data = getBytes(text, charset);
        byte[] signData = Encodes.decodeHex(signHex);
        return verify(data, signData, publicKey, signatureAlgorithm);
    }

    public static boolean verifyHex(String text, String signHex, PublicKey publicKey) {
        return verifyHex(text, signHex, publicKey, null, null);
    }

    /**
     * 公钥加密
     *
     * @param plainBytes 明文bytes
     * @param publicKey  公钥
     * @return 密文bytes
     */
    public static byte[] encryptByPublicKey(byte[] plainBytes, PublicKey publicKey) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            // Cipher在非对称加密场景，没有必要自己做thread cache，JCE实现的provider默认做了~
            Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            int inputLen = plainBytes.length;
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            int keyLength = ((RSAPublicKey) publicKey).getModulus().bitLength() / 8;
            int plainLength = keyLength - 11;
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > plainLength) {
                    cache = cipher.doFinal(plainBytes, offSet, plainLength);
                } else {
                    cache = cipher.doFinal(plainBytes, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * plainLength;
            }
            byte[] encryptedData = out.toByteArray();
            return encryptedData;
        } catch (Exception e) {
            throw new RuntimeException("publicKey encrypt fail:" + e.getMessage());
        }
    }

    public static String encryptByPublicKeyBase64(
            String plainText, PublicKey publicKey, String charset) {
        return Encodes.encodeBase64(encryptByPublicKey(getBytes(plainText, charset), publicKey));
    }

    public static String encryptByPublicKeyBase64(String plainText, PublicKey publicKey) {
        return encryptByPublicKeyBase64(plainText, publicKey, DEFAULT_CHARSET);
    }

    public static String encryptByPublicKeyHex(
            String plainText, PublicKey publicKey, String charset) {
        return Encodes.encodeHex(encryptByPublicKey(getBytes(plainText, charset), publicKey));
    }

    public static String encryptByPublicKeyHex(String plainText, PublicKey publicKey) {
        return encryptByPublicKeyHex(plainText, publicKey, DEFAULT_CHARSET);
    }

    /**
     * 私钥解密
     *
     * @param encryptedBytes 密文数据bytes
     * @param privateKey     私钥
     * @return 明文数据bytes
     */
    public static byte[] decryptByPrivateKey(byte[] encryptedBytes, PrivateKey privateKey) {
        ByteArrayOutputStream out = null;
        try {
            Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            int inputLen = encryptedBytes.length;
            out = new ByteArrayOutputStream();
            int offSet = 0;
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            int keyLength = ((RSAPrivateKey) privateKey).getModulus().bitLength() / 8;
            while (inputLen - offSet > 0) {
                if (inputLen - offSet > keyLength) {
                    cache = cipher.doFinal(encryptedBytes, offSet, keyLength);
                } else {
                    cache = cipher.doFinal(encryptedBytes, offSet, inputLen - offSet);
                }
                out.write(cache, 0, cache.length);
                i++;
                offSet = i * keyLength;
            }
            byte[] decryptedData = out.toByteArray();
            return decryptedData;
        } catch (Exception e) {
            throw new RuntimeException("privateKey decrypt fail:" + e.getMessage());
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    public static String decryptByPrivateKeyBase64(
            String encryptedBase64, PrivateKey privateKey, String charset) {
        byte[] plainBytes = decryptByPrivateKey(Encodes.decodeBase64(encryptedBase64), privateKey);
        try {
            return new String(plainBytes, Strings.isBlankDefault(charset, DEFAULT_CHARSET));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("不支持的字符集:" + e.getMessage());
        }
    }

    public static String decryptByPrivateKeyHex(
            String plainHex, PrivateKey privateKey, String charset) {
        byte[] plainBytes = decryptByPrivateKey(Encodes.decodeHex(plainHex), privateKey);
        try {
            return new String(plainBytes, Strings.isBlankDefault(charset, DEFAULT_CHARSET));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("不支持的字符集:" + e.getMessage());
        }
    }

    /**
     * 通过keystore方式加载私钥
     *
     * @param keystoreUri
     * @param keystoreType
     * @param keystorePassword
     * @return
     */
    public static PrivateKey loadPrivateKeyFromKeyStore(
            String keystoreUri, String keystoreType, String keystorePassword) {
        InputStream in = null;
        try {
            KeyStore keyStore = KeyStore.getInstance(keystoreType);
            Resource resource = new DefaultResourceLoader().getResource(keystoreUri);
            in = resource.getInputStream();
            keyStore.load(in, keystorePassword.toCharArray());

            Enumeration<String> enumas = keyStore.aliases();
            String keyAlias = null;
            if (enumas.hasMoreElements()) {
                keyAlias = enumas.nextElement();
            }
            return (PrivateKey) keyStore.getKey(keyAlias, keystorePassword.toCharArray());
        } catch (Exception e) {
            throw new RuntimeException("通过keystore加载私钥失败:" + e.getMessage());
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    /**
     * 加载私钥
     *
     * @param privateKeyBytes 秘钥的二进制格式
     * @param keyAlgorithm    如：RSA
     * @param providerName    可以为空，如：BC
     * @return
     */
    public static PrivateKey loadPrivateKey(
            byte[] privateKeyBytes, String keyAlgorithm, String providerName) {
        String algorithm = Strings.isBlankDefault(keyAlgorithm, DEFAULT_KEY_ALGO);
        try {
            // 构造PKCS8EncodedKeySpec对象
            PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
            // KEY_ALGORITHM 指定的加密算法
            KeyFactory keyFactory = null;
            if (Strings.isBlank(providerName)) {
                keyFactory = KeyFactory.getInstance(algorithm);
            } else {
                keyFactory = KeyFactory.getInstance(algorithm, providerName);
            }
            // 取私钥匙对象
            return keyFactory.generatePrivate(pkcs8KeySpec);
        } catch (Exception e) {
            throw new RuntimeException("load privateKey fail. " + e.getMessage());
        }
    }

    /**
     * 加载私钥
     *
     * @param privateKeyBase64 秘钥为BASE64编码
     * @param keyAlgorithm     如：RSA
     * @param providerName     可以为空，如：BC
     * @return
     */
    public static PrivateKey loadPrivateKey(
            String privateKeyBase64, String keyAlgorithm, String providerName) {
        byte[] privateKeyBytes = Encodes.decodeBase64(privateKeyBase64);
        return loadPrivateKey(privateKeyBytes, keyAlgorithm, providerName);
    }

    /**
     * @param privateKeyFile 秘钥文件（内容为BASE64）
     * @param keyAlgorithm   如：RSA
     * @param providerName   可以为空
     * @return
     */
    public static PrivateKey loadPrivateKey(
            File privateKeyFile, String keyAlgorithm, String providerName) {
        byte[] privateKeyBytes = null;
        try {
            privateKeyBytes =
                    Encodes.decodeBase64(
                            FileUtils.readFileToString(privateKeyFile, Charset.defaultCharset()));
        } catch (Exception e) {
            throw new RuntimeException("加载秘钥文件内容失败:" + e.getMessage());
        }
        return loadPrivateKey(privateKeyBytes, keyAlgorithm, providerName);
    }

    public static PrivateKey loadPrivateKey(File privateKeyFile) {
        return loadPrivateKey(privateKeyFile, null, null);
    }

    /**
     * 加载公钥
     *
     * @param publicKeyBytes 公钥bytes
     * @param keyAlgorithm   算法，如：RSA
     * @param providerName   可为空
     * @return
     */
    public static PublicKey loadPublicKey(
            byte[] publicKeyBytes, String keyAlgorithm, String providerName) {
        String algorithm = Strings.isBlankDefault(keyAlgorithm, DEFAULT_KEY_ALGO);
        try {
            // KEY_ALGORITHM 指定的加密算法
            KeyFactory keyFactory = null;
            if (Strings.isBlank(providerName)) {
                keyFactory = KeyFactory.getInstance(algorithm);
            } else {
                keyFactory = KeyFactory.getInstance(algorithm, providerName);
            }
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
            return keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            throw new RuntimeException("load publicKey fail. " + e.getMessage());
        }
    }

    /**
     * 加载公钥
     *
     * @param publicKeyBase64 秘钥为BASE64编码
     * @param keyAlgorithm    如：RSA
     * @param providerName    可以为空，如：BC
     * @return
     */
    public static PublicKey loadPublicKey(
            String publicKeyBase64, String keyAlgorithm, String providerName) {
        byte[] publicKeyBytes = Encodes.decodeBase64(publicKeyBase64);
        return loadPublicKey(publicKeyBytes, keyAlgorithm, providerName);
    }

    /**
     * 加载公钥
     *
     * @param publicKeyFile 公钥秘钥文件，内容为BASE64编码
     * @param keyAlgorithm  如：RSA
     * @param providerName  可以为空，如：BC
     * @return
     */
    public static PublicKey loadPublicKey(
            File publicKeyFile, String keyAlgorithm, String providerName) {
        byte[] publicKeyBytes = null;
        try {
            publicKeyBytes =
                    Encodes.decodeBase64(FileUtils.readFileToString(publicKeyFile, Charset.defaultCharset()));
        } catch (Exception e) {
            throw new RuntimeException("加载公钥文件内容失败:" + e.getMessage());
        }
        return loadPublicKey(publicKeyBytes, keyAlgorithm, providerName);
    }

    public static PublicKey loadPublicKey(File publicKeyFile) {
        return loadPublicKey(publicKeyFile, null, null);
    }

    /**
     * 证书文件加载
     *
     * @param springResourceUri
     * @return
     */
    public static PublicKey loadPublicKeyFromCert(String springResourceUri) {
        InputStream in = null;
        try {
            Resource resource = new DefaultResourceLoader().getResource(springResourceUri);
            in = resource.getInputStream();
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate x509 = (X509Certificate) cf.generateCertificate(in);
            return x509.getPublicKey();
        } catch (Exception e) {
            throw new RuntimeException("加载公钥文件内容失败:" + e.getMessage());
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    /**
     * 证书pem格式加载
     *
     * @param pemCert
     * @return
     */
    public static PublicKey loadPublicKeyFromCertWithPem(String pemCert) {
        InputStream in = null;
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            in = new ByteArrayInputStream(Encodes.decodeBase64(pemCert));
            X509Certificate x509 = (X509Certificate) cf.generateCertificate(in);
            return x509.getPublicKey();
        } catch (Exception e) {
            throw new RuntimeException("加载公钥文件内容失败:" + e.getMessage());
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    /**
     * 从keystore加载公钥
     *
     * @param keystoreUri
     * @param keystoreType
     * @param keystorePassword
     * @return
     */
    public static PublicKey loadPublicKeyFromKeyStore(
            String keystoreUri, String keystoreType, String keystorePassword) {
        InputStream in = null;
        try {
            KeyStore keyStore = KeyStore.getInstance(keystoreType);
            Resource resource = new DefaultResourceLoader().getResource(keystoreUri);
            in = resource.getInputStream();
            keyStore.load(in, keystorePassword.toCharArray());

            Enumeration<String> enumas = keyStore.aliases();
            String keyAlias = null;
            if (enumas.hasMoreElements()) {
                keyAlias = enumas.nextElement();
            }
            return keyStore.getCertificate(keyAlias).getPublicKey();
        } catch (Exception e) {
            throw new RuntimeException("通过keystore加载证书公钥失败:" + e.getMessage());
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    public static Map<String, Object> genKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(DEFAULT_KEY_ALGO);
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put("publicKey", publicKey);
        keyMap.put("privateKey", privateKey);
        return keyMap;
    }

    private static byte[] getBytes(String data, String dataCharset) {
        String charset = Strings.isBlankDefault(dataCharset, "UTF-8");
        try {
            return data.getBytes(charset);
        } catch (Exception e) {
            throw new RuntimeException(data + "不支持编码:" + charset);
        }
    }

    public static void main(String[] args) throws Exception {
        Map<String, Object> keys = genKeyPair();
        final PublicKey publicKey = (PublicKey) keys.get("publicKey");
        final PrivateKey privateKey = (PrivateKey) keys.get("privateKey");

        final String data =
                "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                        + "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                        + "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        System.out.println("plain: " + data);
        // 签名和验签
        // String sign = RSA.signBase64(data, privateKey);
        // boolean verify = RSA.verifyBase64(data, sign, publicKey);
        // System.out.println("plain:" + data);
        // System.out.println("sign: " + sign);
        // System.out.println("verify: " + verify);

        // 公钥加密和私钥解密

        int threadCount = 2;
        int testCount = 10;
        final int timesPerThread = 1;
        ExecutorService pool = Executors.newFixedThreadPool(threadCount);
        final CountDownLatch countDownLatch = new CountDownLatch(testCount);
        long start = System.currentTimeMillis();
        for (int i = 0; i < testCount; i++) {
            pool.execute(
                    new Runnable() {
                        @Override
                        public void run() {
                            // 线程内加密10次
                            String encrypt = null;
                            for (int j = 0; j < timesPerThread; j++) {
                                encrypt = RSA.encryptByPublicKeyBase64(data, publicKey, null);
                            }
                            // 线程内解密10次
                            // String decrypt = null;
                            // for (int j = 0; j < timesPerThread; j++) {
                            // decrypt = RSA.decryptByPrivateKeyBase64(encrypt,
                            // privateKey, null);
                            // }
                            // System.out.println("thread" +
                            // Thread.currentThread().getName() + " encrypted:" +
                            // encrypt);
                            // System.out.println("thread" +
                            // Thread.currentThread().getName() + " decrypted: " +
                            // decrypt);
                            // System.out.println(
                            // "thread" + Thread.currentThread().getId() + " cache
                            // stats: " + RSACipherCache.getStats());
                            countDownLatch.countDown();
                        }
                    });
        }
        countDownLatch.await();
        pool.shutdown();
        System.out.println("seconds: " + (System.currentTimeMillis() - start));
        System.out.println("count: " + testCount * timesPerThread);
    }
}
