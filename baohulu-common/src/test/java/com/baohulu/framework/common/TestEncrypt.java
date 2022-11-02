package com.baohulu.framework.common;

import com.baohulu.framework.common.utils.encrypt.EncryptAesUtils;
import com.baohulu.framework.common.utils.encrypt.EncryptMd5Utils;
import org.junit.Test;

/**
 * @author heqing
 * @date 2022/10/13 09:54
 */
public class TestEncrypt {

    @Test
    public void testAes() {
        String key = "0123456789";
        // 需要加密的字串
        String str = "0556-贺小白";
        System.out.println(str);
        // 加密
        String enString = "";
        try {
            enString = EncryptAesUtils.encrypt(key, str);
            System.out.println("加密后的字串是：" + enString);
        } catch (Exception e) {
            System.out.println("加密失败! " + e.getMessage());
        }
        // 解密
        try {
            String deString = EncryptAesUtils.decrypt(key, enString);
            System.out.println("解密后的字串是：" + deString);
        } catch (Exception e) {
            System.out.println("解密失败! " + e.getMessage());
        }

        key = "123456789";
        // 解密
        try {
            String deString = EncryptAesUtils.decrypt(key, enString);
            System.out.println("解密后的字串是：" + deString);
        } catch (Exception e) {
            System.out.println("解密失败! " + e.getMessage());
        }
    }

    @Test
    public void testMd5() {
        // 需要加密的字串
        String str = "0556-贺小白";
        System.out.println(str);
        // 加密
        String enString = EncryptMd5Utils.encrypt(str);
        System.out.println("加密后的字串是：" + enString);
    }
}
