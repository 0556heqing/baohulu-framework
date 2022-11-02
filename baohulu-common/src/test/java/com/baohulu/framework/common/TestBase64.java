package com.baohulu.framework.common;

import com.baohulu.framework.common.utils.http.Base64Utils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author heqing
 * @date 2022/10/13 11:00
 */
public class TestBase64 {

    @Test
    public void base64String() {
        String str = "0556_贺小白";

        String encrypt = Base64Utils.stringToBase64(str);
        System.out.println("字符串 转 base64 -->" + encrypt);

        String decrypt = Base64Utils.base64ToString(encrypt);
        System.out.println("base64 转 字符串 -->" + decrypt);
    }

    @Test
    public void base64File() {
        // 并行执行
        ConcurrentLinkedQueue<String> allQueue = new ConcurrentLinkedQueue<>();
        // 创建并发任务
        CompletableFuture<Boolean> c1 = CompletableFuture.supplyAsync(() -> allQueue.add(baseToFile("test.txt")));
        CompletableFuture<Boolean> c2 = CompletableFuture.supplyAsync(() -> allQueue.add(baseToFile("test.jpeg")));
        CompletableFuture<Boolean> c3 = CompletableFuture.supplyAsync(() -> allQueue.add(baseToFile("test.doc")));
        CompletableFuture<Boolean> c4 = CompletableFuture.supplyAsync(() -> allQueue.add(baseToFile("test.xlsx")));
        CompletableFuture<Boolean> c5 = CompletableFuture.supplyAsync(() -> allQueue.add(baseToFile("test.pptx")));
        CompletableFuture<Boolean> c6 = CompletableFuture.supplyAsync(() -> allQueue.add(baseToFile("test.pdf")));

        try {
            // anyOf ：任意一个成功就返回     allOf ：都必须成功才返回
            CompletableFuture.allOf(c1, c2, c3, c4, c5, c6).get(1, TimeUnit.MINUTES);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<String> allResult = new ArrayList<>(allQueue);
        System.out.println("-->" + allResult);
    }

    /**
     * 文件转换为base64后，生成新的文件
     * @param fileName
     * @return
     */
    private String baseToFile(String fileName) {
        String filePath = "D:/workspace/test/";
        String encrypt = Base64Utils.fileToBase64(filePath + fileName);
        Base64Utils.base64ToFile(encrypt, filePath + "to_" + fileName);
        return fileName + "转换完成";
    }
}
