package com.baohulu.framework.common;

import com.baohulu.framework.basic.consts.Network;
import com.baohulu.framework.common.domain.BaseDomain;
import com.baohulu.framework.common.utils.file.*;
import com.baohulu.framework.common.utils.file.pdf.FtlTemplateBean;
import com.google.zxing.NotFoundException;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import net.lingala.zip4j.exception.ZipException;;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * @author heqing
 * @date 2022/11/02 14:20
 */
public class TestFile extends BaseDomain {

    @Test
    public void copyFile() {
        String inputFile = "D:/workspace/test/test.pdf";
        String outputFile = "D:/workspace/test/test1.pdf";

        FileUtils.copyFile(inputFile, outputFile);
    }

    @Test
    public void copyDirectory() {
        String inputFile = "D:/workspace/test";
        String outputFile = "D:/workspace/test1";

        FileUtils.copyDirectory(inputFile, outputFile);
    }

    @Test
    public void delFile() {
        String file = "D:/workspace/test1";
        String txtFile = file + "/test.txt";
        FileUtils.delFile(txtFile);
        FileUtils.delFile(file);
    }

    @Test
    public void createFile() {
        String file = "D:/workspace/test1";
        String txtFile = file + "/test.txt";

//        FileUtils.createDirectory(file);
        FileUtils.createFile(txtFile);
    }

    @Test
    public void findChildrenList() {
        String file = "D:/workspace/test";
        List<String> files = FileUtils.findChildrenList(new File(file), true);
        System.out.println("-->" + files);
    }

    @Test
    public void fileName() {
        String filePath = "D:/workspace/test/test1.txt";
        String suffix = FileUtils.getSuffixName(filePath);
        System.out.println("文件格式名 --> " + suffix);

        String prefix = FileUtils.getPrefixName(filePath);
        System.out.println("文件名 --> " + prefix);

        String encodingName = FileUtils.encodingFileName(filePath);
        System.out.println("编码后的文件名 --> " + encodingName);
    }

    @Test
    public void byteToFile() {
        String filePath = "D:/workspace/test/test.doc";
        byte[] bytes = FileUtils.fileToByte(filePath);

        String toPath = "D:/workspace/test/1.doc";
        FileUtils.byteToFile(toPath, bytes);
    }

    @Test
    public void createFileByUrlPath() {
        String urlPath = "https://alifei01.cfp.cn/cms/image/image/1db876eb7b084f36b91517587536ba94.jpg";
        String toFile = "D:/workspace/test/sub/1.jpg";
        FileUtils.createFileByUrlPath(urlPath, toFile);
    }

    @Test
    public void compress() throws ZipException {
        String password = "0556";

        String sourceFile1 = "D:/workspace/test/test.doc";
        String targetFileName1 = "D:/workspace/zip/doc.rar";
        ZipUtils.compress(sourceFile1, targetFileName1, password);

        String sourceFile2 = "D:/workspace/test";
        String targetFileName2 = "D:/workspace/zip/file.zip";
        ZipUtils.compress(sourceFile2, targetFileName2, password);
    }

    @Test
    public void uncompress() throws ZipException {
        String password = "0556";
        String targetFile = "D:/workspace/zip";

        String sourceFile1 = "D:/workspace/zip/doc.rar";
        ZipUtils.uncompress(sourceFile1, targetFile, password);

        String sourceFile2 = "D:/workspace/zip/file.zip";
        ZipUtils.uncompress(sourceFile2, targetFile, password);
    }

    @Test
    public void txt() {
        String filePath = "D:/workspace/test/temp.txt";
        String content = "hello world\n";
        TextFiles.writeFile(filePath, content, false);

        String content1 = "你好，贺小白\n";
        TextFiles.writeFile(filePath, content1, true);

        String read = TextFiles.readFile(filePath);
        System.out.println(read);
    }

    @Test
    public void isPicture()  {
        String file1 = "D:/workspace/test/test.pdf";
        boolean isPic1 = ImageUtils.isPicture(file1);
        System.out.println("文件1 --> " + isPic1);

        String file2 = "D:/workspace/test/test.jpeg";
        boolean isPic2 = ImageUtils.isPicture(file2);
        System.out.println("文件2 --> " + isPic2);
    }

    @Test
    public void markImageByMosaic() {
        String sourceFilePath = "D:/workspace/test/test.jpeg";
        String targetFilePath = "D:/workspace/test/temp.jpeg";
        int size = 30;
        ImageUtils.markImageByMosaic(sourceFilePath, targetFilePath, size);
    }

    @Test
    public void getImage() throws FileNotFoundException {
        String text = ImageUtils.getText();
        System.out.println("-->" + text);
        String targetFilePath = "D:/workspace/test/temp.jpg";

        ImageUtils.createVerification(text, targetFilePath);
    }

    @Test
    public void qrCode() throws IOException, NotFoundException {
        String text = "https://www.baidu.com";
        String targetFilePath = "D:/workspace/test/temp.png";
        String logoFilePath = "D:/workspace/test/sub/1.jpg";

        // 生成二维码图片
//        ImageUtil.qrCodeEncode(text, 500, 500, targetFilePath);

        BufferedImage image = ImageUtils.qrCodeEncode(Network.UTF_8, text, 400 ,400);
        ImageUtils.insertLogoImageToQRCode(image, logoFilePath, 100, 100);
        image = ImageUtils.addUpFontToQRCode(image, "顶部文字");
        image = ImageUtils.addBottomFontToQRCode(image, "底部文字,这是一段说明");
        ImageUtils.writeFile(image, targetFilePath);

        String content = ImageUtils.qrCodeDecode(targetFilePath);
        System.out.println("Content: " + content);
    }

    @Test
    public void thumbnails() throws IOException {
        String sourceFilePath = "D:/workspace/test/test.jpeg";
        String targetFilePath = "D:/workspace/test/temp.jpeg";

        // 指定大小缩放
//        Thumbnails.of(sourceFilePath)
//                .size(1000, 800)
//                // 不保持比例
//                .keepAspectRatio(false)
//                .toFile(targetFilePath);

        // 按比例进行缩放
//        Thumbnails.of(sourceFilePath)
//                .scale(2f)
//                .toFile(targetFilePath);

        // 按角度旋转。 角度为正数时，顺时针；角度为负数时，逆时针
//        Thumbnails.of(sourceFilePath)
//                .size(400,300)
//                .rotate(45)
//                .toFile(targetFilePath);

        // 添加水印
        String logoFilePath = "D:/workspace/test/sub/2.jpg";
        Thumbnails.of(sourceFilePath)
                .size(500,500)
                .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File(logoFilePath)), 0.5f)
                .toFile(targetFilePath);

        // 裁剪
//        Thumbnails.of(sourceFilePath)
//                .sourceRegion(Positions.CENTER, 200, 200)
//                .size(400, 400)
//                .toFile(targetFilePath);

//        // 批量操作文件夹中的文件
//        String logoFilePath = "D:/workspace/test/sub";
//        Thumbnails.of(new File(logoFilePath).listFiles())
//                .size(400, 400)
//                .toFiles(Rename.PREFIX_DOT_THUMBNAIL);
    }

    @Test
    public void createImage() {
        String filePath="D:\\workspace\\test\\1.png";

        String ftlTemplateAbsolutePath = this.getClass().getClassLoader().getResource("ftl").getFile();
        String fontsAbsolutePath= this.getClass().getClassLoader().getResource("fonts").getFile();

        FtlTemplateBean ftlTemplateBean = new FtlTemplateBean();
        ftlTemplateBean.setFtlTemplatePath(ftlTemplateAbsolutePath);
        ftlTemplateBean.setFontsPath(fontsAbsolutePath);
        ftlTemplateBean.setFtlTemplateName("recipe.ftl");
        ImageUtils.createImage(ftlTemplateBean, getFrameworkDeta(), filePath);
    }
}
