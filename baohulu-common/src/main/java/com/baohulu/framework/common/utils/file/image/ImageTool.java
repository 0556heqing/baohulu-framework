package com.baohulu.framework.common.utils.file.image;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import sun.font.FontDesignMetrics;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Random;

/**
 * 生成图片工具类
 *
 * @author heqing
 * @date 2022/11/04 09:45
 */
public class ImageTool {

    /**
     * 验证码图片的长和宽
     */
    public final static int WEIGHT = 150;
    public final static int HEIGHT = 60;

    /**
     * 获取随机数对象
     */
    public final static Random r = new Random();

    /**
     * 获取随机的颜色
     *
     * @return
     */
    public static Color randomColor() {
        // 这里为什么是225，因为当r，g，b都为255时，即为白色，为了好辨认，需要颜色深一点。
        int r1= r.nextInt(225);
        int g1 = r.nextInt(225);
        int b1 = r.nextInt(225);
        return new Color(r1, g1, b1);
    }

    /**
     * 获取随机字体
     *
     * @return
     */
    public static Font randomFont() {
        //字体数组
        String[] fontNames = {"Georgia", "微软雅黑", "楷体_GB2312"};
        //获取随机的字体
        int index = r.nextInt(fontNames.length);
        String fontName = fontNames[index];
        //随机获取字体的样式，0是无样式，1是加粗，2是斜体，3是加粗加斜体
        int style = r.nextInt(4);
        //随机获取字体的大小
        int size = r.nextInt(12) + 36;
        //返回一个随机的字体
        return new Font(fontName, style, size);
    }

    /**
     * 创建图片的方法
     *
     * @return
     */
    public static BufferedImage createImage() {
        //创建图片缓冲区
        BufferedImage image = new BufferedImage(WEIGHT, HEIGHT, BufferedImage.TYPE_INT_RGB);
        //获取画笔
        Graphics2D g = (Graphics2D) image.getGraphics();
        //设置背景色随机
        g.setColor(new Color(255, 255, r.nextInt(245) + 10));
        g.fillRect(0, 0, WEIGHT, HEIGHT);
        return image;
    }

    /**
     * 画干扰线，验证码干扰线用来防止计算机解析图片
     *
     * @param image
     */
    public static void drawLine(BufferedImage image) {
        //定义干扰线的数量
        int num = r.nextInt(10);
        Graphics2D g = (Graphics2D) image.getGraphics();
        for (int i = 0; i < num; i++) {
            int x1 = r.nextInt(WEIGHT);
            int y1 = r.nextInt(HEIGHT);
            int x2 = r.nextInt(WEIGHT);
            int y2 = r.nextInt(HEIGHT);
            g.setColor(randomColor());
            g.drawLine(x1, y1, x2, y2);
        }
    }

    /**
     * 创建二维码
     * @param charSet 编码方式
     * @param content 二维码内容
     * @param qrWidth 二维码长度
     * @param qrHeight 二维码高度
     * @return
     */
    public static BufferedImage createQrCodeImage(String charSet, String content, int qrWidth, int qrHeight){
        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, charSet);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = null;
        try {
            // qrHeight : 修改二维码底部高度
            bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, qrWidth , qrHeight, hints);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        return image;
    }

    /**
     * 缩小logo图片
     * @param logoPath
     * @param logoWidth
     * @param logoHeight
     * @return
     */
    public static Image compressLogo(String logoPath, int logoWidth, int logoHeight){
        File file = new File(logoPath);
        if (!file.exists()) {
            return null;
        }
        Image original = null;
        try {
            original = ImageIO.read(new File(logoPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int width = original.getWidth(null);
        int height = original.getHeight(null);
        if (width > logoWidth) {
            width = logoWidth;
        }
        if (height > logoHeight) {
            height = logoHeight;
        }
        Image image = original.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = tag.getGraphics();
        // 绘制缩小后的图
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return image;
    }

    /**
     * 将文明说明增加到二维码上
     * @param str
     * @param width
     * @param height
     * @param fontSize 字体大小
     * @return
     */
    public static BufferedImage textToImage(String str, int width, int height,int fontSize) {
        BufferedImage textImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = (Graphics2D)textImage.getGraphics();
        //开启文字抗锯齿
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setBackground(Color.WHITE);
        g2.clearRect(0, 0, width, height);
        g2.setPaint(Color.BLACK);
        FontRenderContext context = g2.getFontRenderContext();
        Font font = new Font("微软雅黑", Font.BOLD, fontSize);
        g2.setFont(font);
        LineMetrics lineMetrics = font.getLineMetrics(str, context);
        FontMetrics fontMetrics = FontDesignMetrics.getMetrics(font);
        float offset = (width - fontMetrics.stringWidth(str)) / 2;
        float y = (height + lineMetrics.getAscent() - lineMetrics.getDescent() - lineMetrics.getLeading()) / 2;
        g2.drawString(str, (int)offset, (int)y);
        return textImage;
    }
}
