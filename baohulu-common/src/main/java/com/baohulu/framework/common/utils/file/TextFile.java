package com.baohulu.framework.common.utils.file;

import com.baohulu.framework.basic.consts.Network;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * 文本工具类
 *
 * @author heqing
 * @date 2022/11/02 14:25
 */
@Slf4j
public class TextFile extends FileUtils {

    /**
     * 写入文件
     *
     * @param fileName 文件存储路径和名称
     * @param content   文件内容
     * @param append    如果文件存在是否追加。不追加会删除就内容
     * @return boolean 生成文件是否成功
     */
    public static void writeFile(String fileName, String content, boolean append) {
        try {
            write(new File(fileName), content, Network.UTF_8, append);
            log.debug("文件 " + fileName + " 写入成功!");
        } catch (IOException e) {
            log.debug("文件 " + fileName + " 写入失败! " + e.getMessage());
        }
    }

    /**
     * 读取文件内容
     *
     * @param fileName 文件名
     * @return String 文件内容
     */
    public static String readFile(String fileName) {
        File file = new File(fileName);
        Long fileLength = file.length();
        byte[] fileContent = new byte[fileLength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(fileContent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(fileContent, Network.UTF_8);
        } catch (UnsupportedEncodingException e) {
            log.error("The OS does not support " + Network.UTF_8);
            e.printStackTrace();
            return null;
        }
    }

}
