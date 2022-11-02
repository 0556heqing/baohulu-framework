package com.baohulu.framework.common.utils.encrypt;

import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * AES加密工具类
 *
 * @author heqing
 * @date 2022/10/12 17:07
 */
public class EncryptAesUtils {

    private static final String AES = "AES";
    private static final String KEY_AES = "AES/ECB/PKCS5Padding";

    /**
     * 字符串的加密数据
     * @param src 源字符串
     * @param key 密钥
     * @return 加密后数据
     * @throws Exception 异常
     */
    public static String encrypt(String key, String src) throws Exception {
        if(StringUtils.isEmpty(key)) {
            return src;
        }
        if (StringUtils.isEmpty(src)) {
            return "";
        }
        byte[] rSrc  = src.getBytes(StandardCharsets.UTF_8);
        if(rSrc.length == 0){
            return "";
        }
        SecretKeySpec keySpec = mySqlAesKey(key);
        // AES/ECB/PKCS5Padding  算法/模式/补码方式
        Cipher cipher = Cipher.getInstance(KEY_AES);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encrypted = cipher.doFinal(rSrc);
        return byte2hex(encrypted);
    }

    /**
     * 解密
     * @param src 源字符串
     * @param key 密钥
     * @return 解密后字符串
     * @throws Exception 异常
     */
    public static String decrypt(String key, String src) throws Exception {
        if(StringUtils.isBlank(key)) {
            return src;
        }
        byte[] encrypted = hex2byte(src);
        if (encrypted == null || encrypted.length == 0) {
            return "";
        }
        SecretKeySpec keySpec = mySqlAesKey(key);
        Cipher cipher = Cipher.getInstance(KEY_AES);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] original = cipher.doFinal(encrypted);
        return new String(original, StandardCharsets.UTF_8);
    }

    /**
     * 与linux 保持一致
     * @param key 密钥
     * @return 密钥规范
     */
    private static SecretKeySpec mySqlAesKey(final String key) {
        try {
            final byte[] finalKey = new byte[16];
            int i = 0;
            for (byte b : key.getBytes(StandardCharsets.UTF_8)) {
                finalKey[i++ % 16] ^= b;
            }
            return new SecretKeySpec(finalKey, AES);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static byte[] hex2byte(String strHex) {
        if (strHex == null) {
            return null;
        }
        int l = strHex.length(), half = 2;
        if (l % half == 1) {
            return null;
        }
        byte[] b = new byte[l / 2];
        for (int i = 0; i != l / half; i++) {
            b[i] = (byte) Integer.parseInt(strHex.substring(i * 2, i * 2 + 2), 16);
        }
        return b;
    }

    public static String byte2hex(byte[] bs) {
        StringBuilder hs = new StringBuilder();
        for (byte b : bs) {
            String sTmp = (Integer.toHexString(b & 0XFF));
            if (sTmp.length() == 1) {
                hs.append("0").append(sTmp);
            } else {
                hs.append(sTmp);
            }
        }
        return hs.toString().toUpperCase();
    }

}
