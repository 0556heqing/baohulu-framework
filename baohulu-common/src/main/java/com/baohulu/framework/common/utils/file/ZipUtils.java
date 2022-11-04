package com.baohulu.framework.common.utils.file;

import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * 压缩/解压 工具类
 *
 * @author heqing
 * @date 2022/11/03 15:40
 */
@Slf4j
public class ZipUtils extends FileUtils {

    /**
     * 压缩
     *
     * @param sourceFile 压缩源文件
     * @param targetFile 解压文件，默认加上后缀
     * @param password   密码
     */
    public static void compress(String sourceFile, String targetFile, String password) throws ZipException {
        // 判断目标文件路径是否存在，不存在创建文件夹
        File target = new File(targetFile);
        createDirectory(target.getParent());

        ZipParameters zipParameters = new ZipParameters();
        // 压缩方式  0:仅打包，不压缩; 8:默认; 99:加密压缩
        zipParameters.setCompressionMethod(CompressionMethod.DEFLATE);
        // 压缩级别
        zipParameters.setCompressionLevel(CompressionLevel.FASTEST);

        ZipFile zipFile = new ZipFile(targetFile);
        // 是否加密
        if (StringUtils.isNotBlank(password)) {
            zipParameters.setEncryptFiles(true);
            zipParameters.setEncryptionMethod(EncryptionMethod.AES);
            zipParameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);
            zipFile.setPassword(password.toCharArray());
        }

        // 要打包的文件
        File currentFile = new File(sourceFile);
        if(currentFile.isDirectory()) {
            zipFile.addFolder(currentFile, zipParameters);
        } else {
            zipFile.addFile(sourceFile, zipParameters);
        }
    }

    /**
     * 解压
     *
     * @param sourceFile 解压源文件
     * @param targetFile 压源文件
     * @param password   密码
     */
    public static void uncompress(String sourceFile, String targetFile, String password) throws ZipException {
        File source = new File(sourceFile);
        if (!source.exists()) {
            log.error("解压源文件失败，源文件{}不存在!", sourceFile);
            return;
        }

        // 判断目标文件路径是否存在，不存在创建文件夹
        createDirectory(targetFile);

        // 解压
        ZipFile zipFile = new ZipFile(new File(sourceFile));
        if (zipFile.isEncrypted()) {
            zipFile.setPassword(password.toCharArray());
        }
        zipFile.extractAll(targetFile);
    }
}
