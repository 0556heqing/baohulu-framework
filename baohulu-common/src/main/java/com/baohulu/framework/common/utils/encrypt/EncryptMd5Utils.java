package com.baohulu.framework.common.utils.encrypt;

import org.apache.commons.lang3.StringUtils;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * md5加密工具类
 *
 * @author heqing
 * @date 2022/10/13 19:35
 */
public class EncryptMd5Utils {

    private static final String MD5 = "MD5";

    public static String encrypt(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance(MD5);
            byte[] buffer = input.getBytes();
            md.update(buffer);
            byte[] bDigest = md.digest();
            md.reset();
            BigInteger bi = new BigInteger(1, bDigest);
            return bi.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String stringTo32Md5(String input) {
        String decString = encrypt(input);
        if(StringUtils.isNotBlank(decString)) {
            String decLowerStr = decString.toLowerCase();
            decString = StringUtils.isNotBlank(decString) ? StringUtils.leftPad(decLowerStr, 32) : null;
        }
       return decString;
    }

    public static String stringToMd5Hex(String input) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance(MD5);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

        char[] charArray = input.toCharArray();
        byte[] byteArray = new byte[charArray.length];

        for(int i = 0; i < charArray.length; ++i) {
            byteArray[i] = (byte)charArray[i];
        }

        byte[] md5Bytes = md5.digest(byteArray);
        StringBuilder hexValue = new StringBuilder();

        for(byte md5Byte : md5Bytes) {
            int val = md5Byte & 255;
            if(val < 16) {
                hexValue.append("0");
            }
            hexValue.append(Integer.toHexString(val));
        }

        return hexValue.toString();
    }

}
