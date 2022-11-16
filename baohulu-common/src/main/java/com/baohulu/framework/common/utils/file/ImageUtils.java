package com.baohulu.framework.common.utils.file;

import com.baohulu.framework.basic.consts.Network;
import com.baohulu.framework.common.utils.FreemarkerUtils;
import com.baohulu.framework.common.utils.file.image.ImageTool;
import com.baohulu.framework.common.utils.file.image.QrCodeLuminanceSource;
import com.baohulu.framework.common.utils.file.pdf.document.DocumentVo;
import com.baohulu.framework.common.utils.file.pdf.FtlTemplateBean;
import com.google.zxing.*;
import com.google.zxing.common.HybridBinarizer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.xhtmlrenderer.swing.Java2DRenderer;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Hashtable;
import java.util.Map;

/**
 * 图片工具类（包含二维码、验证码图片、马赛克等）
 *
 * @author heqing
 * @date 2022/11/02 13:49
 */
@Slf4j
public class ImageUtils extends FileUtils {

    /**
     * 功能描述：判断文件是否为图片
     *
     * @param filename 文件名
     * @return boolean 文件是否为图片
     */
    public static boolean isPicture(String filename) {
        // 文件名称为空的场合
        if (StringUtils.isEmpty(filename)) {
            // 返回不合法
            return false;
        }
        // 获得文件后缀名
        String tmpName = getSuffixName(filename);
        // 声明图片后缀名数组
        String[][] imageArray = {{"bmp", "0"}, {"dib", "1"},
                {"gif", "2"}, {"jfif", "3"}, {"jpe", "4"},
                {"jpeg", "5"}, {"jpg", "6"}, {"png", "7"},
                {"tif", "8"}, {"tiff", "9"}, {"ico", "10"}};
        // 遍历名称数组
        for (int i = 0; i < imageArray.length; i++) {
            // 判断单个类型文件的场合
            if (imageArray[i][0].equals(tmpName.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 将图片打上马赛克
     *
     * @param sourceFilePath 原图片路径
     * @param targetFilePath 目标图片路径
     * @param size 马赛克尺寸，即每个矩形的宽高
     */
    public static void markImageByMosaic(String sourceFilePath, String targetFilePath, int size) {
        boolean isPic = ImageUtils.isPicture(sourceFilePath);
        if (!isPic) {
            log.error("原始文件不是图片");
            return;
        }
        File sourceFile = new File(sourceFilePath);
        if (!sourceFile.exists()) {
            log.error("原始图片不存在");
            return;
        }
        // 获取文件后缀
        String suffix = getSuffixName(sourceFilePath);
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(sourceFile);
            outputStream = new FileOutputStream(targetFilePath);
            markImageByMosaic(inputStream, outputStream, suffix, size);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream!= null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 将图片打上马赛克
     *
     * @param inputStream 源文件流
     * @param outputStream 输出文件
     * @param imageType 图片类型
     * @param size 马赛克尺寸，即每个矩形的宽高
     */
    public static void markImageByMosaic(InputStream inputStream, OutputStream outputStream, String imageType, int size){
        try{
            if(size<=0){
                log.error("马赛克尺寸必须大于零");
                return;
            }

            //1. 初始化图像处理各变量
            // 读取该图片
            BufferedImage img = ImageIO.read(inputStream);
            //原图片宽 和 高
            int width = img.getWidth(null), height = img.getHeight(null);
            if (width < size || height < size) {
                log.error("马赛克尺寸设置不正确");
                return;
            }
            BufferedImage bi = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);

            //2. 设置各方向绘制的马赛克块个数
            //x方向 和 y方向 绘制个数
            int xcount = 0, ycount = 0;
            if (width % size == 0) {
                xcount = width / size;
            } else {
                xcount = width / size + 1;
            }
            if (height % size == 0) {
                ycount = height / size;
            } else {
                ycount = height / size + 1;
            }
            // x坐标 和 y坐标
            int x = 0, y = 0;

            //3. 绘制马赛克(绘制矩形并填充颜色)
            Graphics2D g = bi.createGraphics();
            for (int i = 0; i < xcount; i++) {
                for (int j = 0; j < ycount; j++) {
                    //马赛克矩形格大小
                    int mwidth = size;
                    int mheight = size;
                    //横向最后一个比较特殊，可能不够一个size
                    if(i == xcount-1){
                        mwidth = width-x;
                    }
                    // 纵向最后一个不够一个size
                    if(j == ycount-1){
                        mheight = height-y;
                    }

                    //矩形颜色取中心像素点RGB值
                    int centerX = x, centerY = y;
                    if (mwidth % 2 == 0) {
                        centerX += mwidth / 2;
                    } else {
                        centerX += (mwidth - 1) / 2;
                    }
                    if (mheight % 2 == 0) {
                        centerY += mheight / 2;
                    } else {
                        centerY += (mheight - 1) / 2;
                    }

                    // 填充马赛克
                    Color color = new Color(img.getRGB(centerX, centerY));
                    g.setColor(color);
                    g.fillRect(x, y, mwidth, mheight);
                    y = y + size;
                }
                // 还原y坐标
                y = 0;
                // 计算x坐标
                x = x + size;
            }
            g.dispose();
            // 保存图片
            ImageIO.write(bi, imageType, outputStream);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 获取验证码图片
     *
     * @param text 图片中的内容（验证码）
     * @param targetFile 保存文件
     * @return
     */
    public static void createVerification(String text, String targetFile) {
        boolean isPic = ImageUtils.isPicture(targetFile);
        if (!isPic) {
            log.error("保存文件必须是图片后缀");
            return;
        }

        // 判断目标文件路径是否存在，不存在创建文件夹
        File target = new File(targetFile);
        createDirectory(target.getParent());

        String suffixName = getSuffixName(targetFile);
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(targetFile);
            createVerification(text, suffixName, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream!= null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取验证码图片
     *
     * @param text 图片中的内容（验证码）
     * @param imageType 图片格式
     * @param outputStream 输出流
     * @return
     */
    public static void createVerification(String text, String imageType, OutputStream outputStream) {
        // 创建图片的方法
        BufferedImage image = ImageTool.createImage();
        //获取画笔
        Graphics2D g = (Graphics2D) image.getGraphics();
        char[] chars = text.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            String s = String.valueOf(chars[i]);
            // 定义字符的x坐标
            float x = i * 1.0F * ImageTool.WEIGHT / chars.length;
            // 设置字体，随机
            g.setFont(ImageTool.randomFont());
            // 设置颜色，随机
            g.setColor(ImageTool.randomColor());
            g.drawString(s, x, ImageTool.HEIGHT - 10);
        }

        // 画干扰线，验证码干扰线用来防止计算机解析图片
        ImageTool.drawLine(image);

        try {
            ImageIO.write(image, imageType, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取验证码文本的方法
     *
     * @return 验证码中的内容
     */
    public static String getText() {
        int charLength = 4;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < charLength; i++) {
            //随机生成字符，因为只有画字符串的方法，没有画字符的方法，所以需要将字符变成字符串再画
            //验证码数组
            String codes = "23456789abcdefghjkmnopqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ";
            int index = ImageTool.R.nextInt(codes.length());
            sb.append(codes.charAt(index));
        }
        return sb.toString();
    }

    /**
     * 生成二维码图片
     * @param content 二维码对应内容
     * @param qrWidth 宽度
     * @param qrHeight 长度
     * @param targetFile 二维码存放路径
     */
    public static void qrCodeEncode(String content, int qrWidth, int qrHeight, String targetFile) {
        BufferedImage image = qrCodeEncode(Network.UTF_8, content, qrWidth, qrHeight);
        writeFile(image, targetFile);
    }

    /**
     * 生成二维码图片流
     * @param charSet 二维码编码方式
     * @param content 内容
     * @param qrWidth 宽度
     * @param qrHeight 长度
     * @return
     */
    public static BufferedImage qrCodeEncode(String charSet, String content, int qrWidth, int qrHeight) {
        BufferedImage image = ImageTool.createQrCodeImage(charSet,content,qrWidth,qrHeight);
        return image;
    }

    /**
     * 对已经生成好的二维码设置logo
     * @param source 二维码
     * @param logoPath logo图片地址
     * @param logoWidth logo宽度
     * @param logoHeight logo高度
     */
    public static void insertLogoImageToQRCode(BufferedImage source, String logoPath, int logoWidth, int logoHeight){
        Image logo = ImageTool.compressLogo(logoPath, logoWidth, logoHeight);
        if(logo == null) {
            log.error("解析logo图片失败！地址{}", logoPath);
            return;
        }
        Graphics2D graph = source.createGraphics();
        int qrWidth = source.getWidth();
        int qrHeight = source.getHeight();
        int x = (qrWidth - logoWidth) / 2;
        int y = (qrHeight - logoHeight) / 2;
        graph.drawImage(logo, x, y, logoWidth, logoHeight, null);
        Shape shape = new RoundRectangle2D.Float(x, y, logoWidth, logoHeight, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }

    /**
     * 顶部增加说明文字
     * @param source
     * @param text
     */
    public static BufferedImage addUpFontToQRCode(BufferedImage source, String text) {
        int textHeight = 20;

        int qrWidth = source.getWidth();
        int qrHeight = source.getHeight();

        BufferedImage tempImage = new BufferedImage(qrWidth, qrHeight + textHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graph = tempImage.createGraphics();
        // 顶部文字
        BufferedImage textImage = ImageTool.textToImage(text, qrWidth, textHeight,20);
        graph.drawImage(textImage, 0, 0, textImage.getWidth(), textImage.getHeight(), null);
        // 二维码
        graph.drawImage(source, 0, textHeight, qrWidth, qrHeight, null);
        graph.dispose();
        return tempImage;
    }

    /**
     * 增加底部的说明文字
     * @param source 二维码
     * @param text 说明内容
     */
    public static BufferedImage addBottomFontToQRCode(BufferedImage source, String text) {
        int textHeight = 24;

        int qrWidth = source.getWidth();
        int qrHeight = source.getHeight();

        BufferedImage tempImage = new BufferedImage(qrWidth, qrHeight + textHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D graph = tempImage.createGraphics();
        // 二维码
        graph.drawImage(source, 0, 0, qrWidth, qrHeight, null);
        // 底部文字
        BufferedImage textImage = ImageTool.textToImage(text, qrWidth, textHeight,16);
        graph.drawImage(textImage, 0, qrHeight, textImage.getWidth(), textImage.getHeight(), null);
        graph.dispose();
        return tempImage;
    }

    /**
     * 解析二维码图片中的内容
     *
     * @param sourceFilePath 二维码图片地址
     */
    public static String qrCodeDecode(String sourceFilePath) throws IOException, NotFoundException {
        File sourceFile = new File(sourceFilePath);
        if (!sourceFile.exists()) {
            log.error("解析二维码图片失败，二维码图片文件{}不存在!", sourceFilePath);
            return "";
        }

        BufferedImage image;
        image = ImageIO.read(sourceFile);
        if (image == null) {
            return null;
        }
        QrCodeLuminanceSource source = new QrCodeLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result;
        Hashtable hints = new Hashtable();
        hints.put(DecodeHintType.CHARACTER_SET, Network.UTF_8);
        result = new MultiFormatReader().decode(bitmap, hints);
        String resultStr = result.getText();
        return resultStr;
    }

    /**
     * 将图片写入对应的文件
     * @param image 图片
     * @param targetFile 二维码存放路径
     */
    public static void writeFile(BufferedImage image, String targetFile) {
        // 判断目标文件路径是否存在，不存在创建文件夹
        File target = new File(targetFile);
        createDirectory(target.getParent());

        // 获取后缀
        String suffixName = getSuffixName(targetFile);

        try {
            ImageIO.write(image, suffixName, new File(targetFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据模板生成图片
     * @param ftlTemplate 模板信息
     * @param documentVo 填充数据
     * @param outputFile 输出路径
     */
    public static void createImage(FtlTemplateBean ftlTemplate, DocumentVo documentVo, String outputFile) {
        try {
            Map<String, Object> variables = documentVo.fillDataMap();
            String htmlContent = FreemarkerUtils.generate(ftlTemplate.getFtlTemplatePath(), ftlTemplate.getFtlTemplateName(), variables);

            byte[] bytes = htmlContent.getBytes("UTF-8");
            ByteArrayInputStream bin = new ByteArrayInputStream(bytes);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(bin);
            Java2DRenderer renderer = new Java2DRenderer(document, 1000, 1000);
            BufferedImage img = renderer.getImage();

            ImageIO.write(img, "png", new File(outputFile));
            bin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
