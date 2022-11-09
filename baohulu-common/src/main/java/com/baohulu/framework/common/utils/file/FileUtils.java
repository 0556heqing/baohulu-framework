package com.baohulu.framework.common.utils.file;

import com.baohulu.framework.basic.consts.Network;
import com.baohulu.framework.basic.consts.Separators;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import java.util.List;

/**
 * 文件操作工具类
 * 实现文件的创建、删除、复制以及目录的创建、删除、复制等功能
 *
 * @author heqing
 * @date 2022/11/02 14:09
 */
@Slf4j
public class FileUtils extends org.apache.commons.io.FileUtils {

    /**
     * 复制单个文件，如果目标文件存在，则不覆盖
     * @param srcFileName 待复制的文件名
     * @param descFileName 目标文件名
     * @return 如果复制成功，则返回true，否则返回false
     */
    public static boolean copyFile(String srcFileName, String descFileName) {
        return FileUtils.copyFileCover(srcFileName, descFileName, false);
    }

    /**
     * 复制单个文件
     * @param srcFileName 待复制的文件名
     * @param descFileName 目标文件名
     * @param cover 如果目标文件已存在，是否覆盖
     * @return 如果复制成功，则返回true，否则返回false
     */
    public static boolean copyFileCover(String srcFileName, String descFileName, boolean cover) {
        File srcFile = new File(srcFileName);
        // 判断源文件是否存在
        if (!srcFile.exists()) {
            log.debug("复制文件失败，源文件 " + srcFileName + " 不存在!");
            return false;
        }
        // 判断源文件是否是合法的文件
        else if (!srcFile.isFile()) {
            log.debug("复制文件失败，" + srcFileName + " 不是一个文件!");
            return false;
        }
        File descFile = new File(descFileName);
        // 判断目标文件是否存在
        if (descFile.exists()) {
            // 如果目标文件存在，并且允许覆盖
            if (cover) {
                log.debug("目标文件已存在，准备删除!");
                if (!FileUtils.delFile(descFileName)) {
                    log.debug("删除目标文件 " + descFileName + " 失败!");
                    return false;
                }
            } else {
                log.debug("复制文件失败，目标文件 " + descFileName + " 已存在!");
                return false;
            }
        } else {
            if (!descFile.getParentFile().exists()) {
                // 如果目标文件所在的目录不存在，则创建目录
                log.debug("目标文件所在的目录不存在，创建目录!");
                // 创建目标文件所在的目录
                if (!descFile.getParentFile().mkdirs()) {
                    log.debug("创建目标文件所在的目录失败!");
                    return false;
                }
            }
        }

        // 准备复制文件
        // 读取的位数
        int readByte = 0;
        InputStream ins = null;
        OutputStream outs = null;
        try {
            // 打开源文件
            ins = new FileInputStream(srcFile);
            // 打开目标文件的输出流
            outs = new FileOutputStream(descFile);
            byte[] buf = new byte[1024];
            // 一次读取1024个字节，当readByte为-1时表示文件已经读取完毕
            while ((readByte = ins.read(buf)) != -1) {
                // 将读取的字节流写入到输出流
                outs.write(buf, 0, readByte);
            }
            log.debug("复制单个文件 " + srcFileName + " 到" + descFileName
                    + "成功!");
            return true;
        } catch (Exception e) {
            log.debug("复制文件失败：" + e.getMessage());
            return false;
        } finally {
            // 关闭输入输出流，首先关闭输出流，然后再关闭输入流
            if (outs != null) {
                try {
                    outs.close();
                } catch (IOException oute) {
                    oute.printStackTrace();
                }
            }
            if (ins != null) {
                try {
                    ins.close();
                } catch (IOException ine) {
                    ine.printStackTrace();
                }
            }
        }
    }

    /**
     * 复制整个目录的内容，如果目标目录存在，则不覆盖
     * @param srcDirName 源目录名
     * @param descDirName 目标目录名
     * @return 如果复制成功返回true，否则返回false
     */
    public static boolean copyDirectory(String srcDirName, String descDirName) {
        return FileUtils.copyDirectoryCover(srcDirName, descDirName,
                false);
    }

    /**
     * 复制整个目录的内容
     * @param srcDirName 源目录名
     * @param descDirName 目标目录名
     * @param cover 如果目标目录存在，是否覆盖
     * @return 如果复制成功返回true，否则返回false
     */
    public static boolean copyDirectoryCover(String srcDirName, String descDirName, boolean cover) {
        File srcDir = new File(srcDirName);
        // 判断源目录是否存在
        if (!srcDir.exists()) {
            log.debug("复制目录失败，源目录 " + srcDirName + " 不存在!");
            return false;
        }
        // 判断源目录是否是目录
        else if (!srcDir.isDirectory()) {
            log.debug("复制目录失败，" + srcDirName + " 不是一个目录!");
            return false;
        }
        // 如果目标文件夹名不以文件分隔符结尾，自动添加文件分隔符
        String descDirNames = descDirName;
        if (!descDirNames.endsWith(File.separator)) {
            descDirNames = descDirNames + File.separator;
        }
        File descDir = new File(descDirNames);
        // 如果目标文件夹存在
        if (descDir.exists()) {
            if (cover) {
                // 允许覆盖目标目录
                log.debug("目标目录已存在，准备删除!");
                if (!FileUtils.delFile(descDirNames)) {
                    log.debug("删除目录 " + descDirNames + " 失败!");
                    return false;
                }
            } else {
                log.debug("目标目录复制失败，目标目录 " + descDirNames + " 已存在!");
                return false;
            }
        } else {
            // 创建目标目录
            log.debug("目标目录不存在，准备创建!");
            if (!descDir.mkdirs()) {
                log.debug("创建目标目录失败!");
                return false;
            }

        }

        boolean flag = true;
        // 列出源目录下的所有文件名和子目录名
        File[] files = srcDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 如果是一个单个文件，则直接复制
            if (files[i].isFile()) {
                flag = FileUtils.copyFile(files[i].getAbsolutePath(),
                        descDirName + "\\"+ files[i].getName());
                // 如果拷贝文件失败，则退出循环
                if (!flag) {
                    break;
                }
            }
            // 如果是子目录，则继续复制目录
            if (files[i].isDirectory()) {
                flag = FileUtils.copyDirectory(files[i]
                        .getAbsolutePath(), descDirName + "\\" + files[i].getName());
                // 如果拷贝目录失败，则退出循环
                if (!flag) {
                    break;
                }
            }
        }

        if (!flag) {
            log.debug("复制目录 " + srcDirName + " 到 " + descDirName + " 失败!");
            return false;
        }
        log.debug("复制目录 " + srcDirName + " 到 " + descDirName + " 成功!");
        return true;

    }

    /**
     * 删除文件，可以删除单个文件或文件夹
     *
     * @param fileName 被删除的文件名
     * @return 如果删除成功，则返回true，否是返回false
     */
    public static boolean delFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            log.debug(fileName + " 文件不存在!");
            return true;
        } else {
            if (file.isFile()) {
                return deleteFile(fileName);
            } else {
                return deleteDirectory(fileName);
            }
        }
    }

    /**
     * 删除单个文件
     *
     * @param fileName 被删除的文件名
     * @return 如果删除成功，则返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                log.debug("删除文件 " + fileName + " 成功!");
                return true;
            } else {
                log.debug("删除文件 " + fileName + " 失败!");
                return false;
            }
        } else {
            log.debug(fileName + " 文件不存在!");
            return true;
        }
    }

    /**
     *
     * 删除目录及目录下的文件
     *
     * @param dirName 被删除的目录所在的文件路径
     * @return 如果目录删除成功，则返回true，否则返回false
     */
    public static boolean deleteDirectory(String dirName) {
        String dirNames = dirName;
        if (!dirNames.endsWith(File.separator)) {
            dirNames = dirNames + File.separator;
        }
        File dirFile = new File(dirNames);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            log.debug(dirNames + " 目录不存在!");
            return true;
        }
        boolean flag = true;
        // 列出全部文件及子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                // 如果删除文件失败，则退出循环
                if (!flag) {
                    break;
                }
            }
            // 删除子目录
            else if (files[i].isDirectory()) {
                flag = deleteDirectory(files[i]
                        .getAbsolutePath());
                // 如果删除子目录失败，则退出循环
                if (!flag) {
                    break;
                }
            }
        }

        if (!flag) {
            log.debug("删除目录失败!");
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            log.debug("删除目录 " + dirName + " 成功!");
            return true;
        } else {
            log.debug("删除目录 " + dirName + " 失败!");
            return false;
        }

    }

    /**
     * 创建单个文件
     * @param descFileName 文件名，包含路径
     * @return 如果创建成功，则返回true，否则返回false
     */
    public static boolean createFile(String descFileName) {
        File file = new File(descFileName);
        if (file.exists()) {
            log.debug("文件 " + descFileName + " 已存在!");
            return false;
        }
        if (descFileName.endsWith(File.separator)) {
            log.debug(descFileName + " 为目录，不能创建目录!");
            return false;
        }
        if (!file.getParentFile().exists()) {
            // 如果文件所在的目录不存在，则创建目录
            if (!file.getParentFile().mkdirs()) {
                log.debug("创建文件所在的目录失败!");
                return false;
            }
        }
        // 创建文件
        try {
            if (file.createNewFile()) {
                log.debug(descFileName + " 文件创建成功!");
                return true;
            } else {
                log.debug(descFileName + " 文件创建失败!");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.debug(descFileName + " 文件创建失败!");
            return false;
        }
    }

    /**
     * 创建目录
     * @param descDirName 目录名,包含路径
     * @return 如果创建成功，则返回true，否则返回false
     */
    public static boolean createDirectory(String descDirName) {
        String descDirNames = descDirName;
        if (!descDirNames.endsWith(File.separator)) {
            descDirNames = descDirNames + File.separator;
        }
        File descDir = new File(descDirNames);
        if (descDir.exists()) {
            log.debug("目录 " + descDirNames + " 已存在!");
            return false;
        }
        // 创建目录
        if (descDir.mkdirs()) {
            log.debug("目录 " + descDirNames + " 创建成功!");
            return true;
        } else {
            log.debug("目录 " + descDirNames + " 创建失败!");
            return false;
        }
    }

    /**
     * 获目录下的文件列表
     * @param dir 搜索目录
     * @return 文件列表
     */
    public static List<String> findChildrenList(File dir, boolean searchDirs) {
        List<String> files = Lists.newArrayList();
        for (String subFiles : dir.list()) {
            File file = new File(dir + "/" + subFiles);
            if (!file.isDirectory()) {
                files.add(file.getName());
            } else {
                if(searchDirs) {
                    List<String> subFileList = findChildrenList(file, true);
                    files.addAll(subFileList);
                }
            }
        }
        return files;
    }

    /**
     * 获取文件扩展名
     *
     * @param filename 文件名
     * @return String 扩展名
     */
    public static String getSuffixName(String filename) {
        return getSuffixName(filename, "");
    }

    /**
     * 获取文件扩展名
     *
     * @param filename 文件名
     * @param defExt   默认扩展名
     * @return String 扩展名
     */
    public static String getSuffixName(String filename, String defExt) {
        if ((filename != null) && (filename.length() > 0)) {
            int i = filename.lastIndexOf('.');

            if ((i > 0) && (i < (filename.length() - 1))) {
                return (filename.substring(i + 1)).toLowerCase();
            }
        }
        return defExt.toLowerCase();
    }

    /**
     * 获取文件名称[不含扩展名]
     *
     * @param fileName 文件名全名
     * @return String 文件名
     */
    public static String getPrefixName(String fileName) {
        int splitIndex = fileName.lastIndexOf(Separators.POINT);
        return fileName.substring(0, splitIndex);
    }

    /**
     * 获取文件名称[不含路径]
     *
     * @param fileName 文件名全名
     * @return String 文件名
     */
    public static String getFileName(String fileName) {
        int splitIndex = fileName.lastIndexOf(Separators.POINT);
        String name = fileName.substring(0, splitIndex);
        splitIndex = name.lastIndexOf("/");
        return name.substring(splitIndex+1, name.length());
    }

    /**
     * 功能描述：文件编码
     *
     * @param fileName 文件名
     * @return String 编码后的文件名
     * @description: 防止文件名中文乱码
     */
    public static String encodingFileName(String fileName) {
        int max = 250;
        String returnFileName = "";
        try {
            returnFileName = URLEncoder.encode(fileName, Network.UTF_8);
            returnFileName = StringUtils.replace(returnFileName, "+", "%20");
            if (returnFileName.length() > max) {
                returnFileName = new String(fileName.getBytes("GB2312"), "ISO8859-1");
                returnFileName = StringUtils.replace(returnFileName, " ", "%20");
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return returnFileName;
    }

    /***
     * 读取文件成字节数组
     *
     * @param path 文件地址
     * @return
     */
    public static byte[] fileToByte(String path){
        try {
            FileInputStream in =new FileInputStream(new File(path));
            byte[] data=new byte[in.available()];
            in.read(data);
            in.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /***
     * 将字节数组写入文件
     *
     * @param path 文件地址
     * @return
     */
    public static void byteToFile(String path, byte[] data) {
        try {
            FileOutputStream outputStream  =new FileOutputStream(new File(path));
            outputStream.write(data);
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 下载网络文件
     *
     * @param urlPath 文件地址
     * @param toFile 文件
     * @return
     */
    public static void createFileByUrlPath(String urlPath, String toFile) {
        InputStream inputStream = getInputStreamByUrlPath(urlPath);
        inputStreamToFile(inputStream, new File(toFile));
    }

    /***
     * 通过网络地址获取文件流
     * @param urlPath : 网络文件地址
     * @return: java.io.InputStream
     **/
    public static InputStream getInputStreamByUrlPath(String urlPath) {
        InputStream inputStream = null;
        try {
            // 统一资源
            URL url = new URL(urlPath);
            // 连接类的父类，抽象类
            URLConnection urlConnection = url.openConnection();
            // http的连接类
            HttpURLConnection httpUrlConnection = (HttpURLConnection) urlConnection;
            //设置超时
            httpUrlConnection.setConnectTimeout(1000*5);
            // 设置字符编码
            httpUrlConnection.setRequestProperty("Charset", Network.UTF_8);
            // 打开到此 URL引用的资源的通信链接（如果尚未建立这样的连接）。
            httpUrlConnection.connect();
            // 获取输入流
            inputStream = httpUrlConnection.getInputStream();
        }catch (Exception e){
            e.printStackTrace();
            log.error("获取网络文件流失败【{}】",urlPath);
        }
        return inputStream;
    }

    /***
     * 将流写入文件
     * @param inputStream : 输入流
     * @param file : 目标文件
     **/
    public static void inputStreamToFile(InputStream inputStream, File file) {
        FileOutputStream outputStream = null;
        try{
            outputStream = new FileOutputStream(file);
            int read;
            byte[] bytes = new byte[1024];
            while ((read = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, read);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try{
                if(inputStream!= null){
                    inputStream.close();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            try{
                if(outputStream != null){
                    outputStream.close();
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

}
