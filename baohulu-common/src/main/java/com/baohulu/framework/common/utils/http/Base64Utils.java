package com.baohulu.framework.common.utils.http;

import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * base64 工具，提高字符/文件与base的相互转换工具
 *
 * @author heqing
 * @date 2022/10/13 10:53
 */
@Slf4j
public class Base64Utils {

    /**
     * BASE64加密
     */
    public static String stringToBase64(String str) {
        try {
            byte[] key = str.getBytes(StandardCharsets.UTF_8);
            return (new BASE64Encoder()).encodeBuffer(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * BASE64解密
     */
    public static String base64ToString(String str) {
        try {
            byte[] key = (new BASE64Decoder()).decodeBuffer(str);
            return new String(key, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 文件转base64字符串
     */
    public static String fileToBase64(String fileName) {
        File file;
        file = new File(fileName);
        if (!file.exists()) {
            log.error("该文件不存在，请检查！");
            return "";
        }
        try {
            InputStream inputStream = new FileInputStream(file);
            return inputStreamToBase64(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * base64字符串转文件
     *
     * @return 文件
     */
    public static File base64ToFile(String base64, String fileName) {
        File file = null;
        FileOutputStream out = null;
        try {
            // 解码，然后将字节转换为文件
            file = new File(fileName);
            if (!file.exists()) {
                //noinspection ResultOfMethodCallIgnored
                boolean isSuccess = file.createNewFile();
                if(!isSuccess) {
                    log.error("创建文件失败，请检查！");
                    return file;
                }
            }
            // 将字符串转换为byte数组
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] bytes = decoder.decodeBuffer(base64);
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            byte[] buffer = new byte[1024];
            out = new FileOutputStream(file);
            int byteRead;
            while ((byteRead = in.read(buffer)) != -1) {
                // 文件写操作
                out.write(buffer, 0, byteRead);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 输入流转base64字符串
     */
    public static String inputStreamToBase64(InputStream fis) {
        String base64 = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            byte[] data = bos.toByteArray();
            bos.close();

            base64 = Base64.getEncoder().encodeToString(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return base64;
    }

}
