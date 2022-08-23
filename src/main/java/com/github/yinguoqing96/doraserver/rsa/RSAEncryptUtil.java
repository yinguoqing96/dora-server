package com.github.yinguoqing96.doraserver.rsa;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Yin Guoqing
 * @date 2022/8/17
 */
@Slf4j
public class RSAEncryptUtil {

    /**
     * 公钥
     */
    private static final String PUBLIC_KEY = "publicKey";
    /**
     * 私钥
     */
    private static final String PRIVATE_KEY = "privateKey";


    public static Map<String, String> getKeys() {
        //随机生成公钥和秘钥
        return genKeyPair();
    }

    /**
     * 获取双向key
     */
    public static Map<String, String> genKeyPair() {
        Map<String, String> map = new HashMap<>();
        KeyPair keyPair;
        try {
            // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            // 初始化密钥对生成器，密钥大小为96-1024位
            generator.initialize(1024);
            // 生成一个密钥对，保存在keyPair中
            keyPair = generator.generateKeyPair();
            // 得到私钥
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
            // 得到公钥
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            // 将公钥和私钥保存到Map
            String publicKeyString = Base64.getEncoder().encodeToString(publicKey.getEncoded());
            // 得到私钥字符串
            String privateKeyString = Base64.getEncoder().encodeToString((privateKey.getEncoded()));
            map.put(PUBLIC_KEY, publicKeyString);
            map.put(PRIVATE_KEY, privateKeyString);
        } catch (NoSuchAlgorithmException e) {
            e.getMessage();
        }
        return map;
    }

    /**
     * RSA私钥加密
     */
    public static String privateKeyEncrypt(String str, String privateKey) {
        try {
            // base64编码的公钥
            byte[] decoded = Base64.getDecoder().decode(privateKey.getBytes(StandardCharsets.UTF_8));
            PrivateKey priKey = KeyFactory.getInstance("RSA").
                    generatePrivate(new PKCS8EncodedKeySpec(decoded));
            // RSA加密
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, priKey);

            // 当长度过长的时候，需要分割后加密 117个字节
            byte[] resultBytes = getMaxResultEncrypt(str, cipher);

            String outStr = Base64.getEncoder().encodeToString(resultBytes);
            return outStr;
        } catch (Exception e) {
            log.error("Exception: {}", e);
        }
        return null;
    }

    /**
     * RSA公钥解密
     */
    public static String publicKeyDecrypt(String str, String publicKey) {
        try {
            // base64编码的私钥
            byte[] decoded = Base64.getDecoder().decode(publicKey.getBytes(StandardCharsets.UTF_8));
            PublicKey pubKey = KeyFactory.getInstance("RSA")
                    .generatePublic(new X509EncodedKeySpec(decoded));
            // RSA解密
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, pubKey);
            // 当长度过长的时候，需要分割后解密 128个字节
            String outStr = new String(getMaxResultDecrypt(str, cipher));
            return outStr;
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
        }
        return null;
    }

    /**
     * RSA公钥加密
     */
    public static String publicKeyEncrypt(String str, String publicKey) {
        try {
            //base64编码的公钥
            byte[] decoded = Base64.getDecoder().decode(publicKey.getBytes(StandardCharsets.UTF_8));
            RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").
                    generatePublic(new X509EncodedKeySpec(decoded));
            //RSA加密
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);

            //当长度过长的时候，需要分割后加密 117个字节
            byte[] resultBytes = getMaxResultEncrypt(str, cipher);

            String outStr = Base64.getEncoder().encodeToString(resultBytes);
            return outStr;
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
        }
        return null;
    }

    private static byte[] getMaxResultEncrypt(String str, Cipher cipher) throws IllegalBlockSizeException, BadPaddingException {
        byte[] inputArray = str.getBytes();
        int inputLength = inputArray.length;
        // 最大加密字节数，超出最大字节数需要分组加密
        int MAX_ENCRYPT_BLOCK = 117;
        // 标识
        int offSet = 0;
        byte[] resultBytes = {};
        byte[] cache = {};
        while (inputLength - offSet > 0) {
            if (inputLength - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(inputArray, offSet, MAX_ENCRYPT_BLOCK);
                offSet += MAX_ENCRYPT_BLOCK;
            } else {
                cache = cipher.doFinal(inputArray, offSet, inputLength - offSet);
                offSet = inputLength;
            }
            resultBytes = Arrays.copyOf(resultBytes, resultBytes.length + cache.length);
            System.arraycopy(cache, 0, resultBytes, resultBytes.length - cache.length, cache.length);
        }
        return resultBytes;
    }

    /**
     * RSA私钥解密
     */
    public static String privateKeyDecrypt(String str, String privateKey) {
        try {
            // base64编码的私钥
            byte[] decoded = Base64.getDecoder().decode(privateKey.getBytes(StandardCharsets.UTF_8));
            RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA")
                    .generatePrivate(new PKCS8EncodedKeySpec(decoded));
            // RSA解密
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            // String outStr = new String(cipher.doFinal(inputByte));
            // 当长度过长的时候，需要分割后解密 128个字节
            String outStr = new String(getMaxResultDecrypt(str, cipher));
            return outStr;
        } catch (Exception e) {
            log.error("Exception: {}", e.getMessage());
        }
        return null;
    }

    private static byte[] getMaxResultDecrypt(String str, Cipher cipher) throws IllegalBlockSizeException, BadPaddingException {
        byte[] inputArray = Base64.getDecoder().decode(str.getBytes(StandardCharsets.UTF_8));
        int inputLength = inputArray.length;
        // 最大解密字节数，超出最大字节数需要分组加密
        int MAX_ENCRYPT_BLOCK = 128;
        // 标识
        int offSet = 0;
        byte[] resultBytes = {};
        byte[] cache = {};
        while (inputLength - offSet > 0) {
            if (inputLength - offSet > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(inputArray, offSet, MAX_ENCRYPT_BLOCK);
                offSet += MAX_ENCRYPT_BLOCK;
            } else {
                cache = cipher.doFinal(inputArray, offSet, inputLength - offSet);
                offSet = inputLength;
            }
            resultBytes = Arrays.copyOf(resultBytes, resultBytes.length + cache.length);
            System.arraycopy(cache, 0, resultBytes, resultBytes.length - cache.length, cache.length);
        }
        return resultBytes;
    }

}