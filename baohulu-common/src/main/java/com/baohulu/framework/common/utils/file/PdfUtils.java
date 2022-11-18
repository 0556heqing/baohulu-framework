package com.baohulu.framework.common.utils.file;

import com.baohulu.framework.basic.enums.ReturnEnum;
import com.baohulu.framework.basic.exception.BusinessException;
import com.baohulu.framework.common.utils.FreemarkerUtils;
import com.baohulu.framework.common.utils.file.pdf.document.DocumentVo;
import com.baohulu.framework.common.utils.file.pdf.FtlTemplateBean;
import com.baohulu.framework.common.utils.file.pdf.ITextRendererObjectFactory;
import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import com.itextpdf.text.*;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * pdf工具类
 *
 * @author heqing
 * @date 2022/11/15 10:17
 */
public class PdfUtils extends FileUtils {

    /**
     * 通过documents4j 实现word转pdf
     *
     * @param sourcePath 源文件地址 如 /root/example.doc
     * @param targetPath 目标文件地址 如 /root/example.pdf
     */
    public static void wordToPdf(String sourcePath, String targetPath) {
        File inputWord = new File(sourcePath);
        if (!inputWord.exists()) {
            throw new BusinessException(ReturnEnum.PARAM_ERROR, "word文档不存在");
        }
        File outputFile = new File(targetPath);
        try  {
            StopWatch stopWatch = new StopWatch("转为pdf文件耗时");
            stopWatch.start();

            InputStream docxInputStream = new FileInputStream(inputWord);
            OutputStream outputStream = new FileOutputStream(outputFile);
            IConverter converter = LocalConverter.builder().build();
            String suffixName = getSuffixName(sourcePath);
            if(DOCX.equals(suffixName)) {
                converter.convert(docxInputStream).as(DocumentType.DOCX).to(outputStream).as(DocumentType.PDF).execute();
            } else if(DOC.equals(suffixName)) {
                converter.convert(docxInputStream).as(DocumentType.DOC).to(outputStream).as(DocumentType.PDF).execute();
            } else if(TEXT.equals(suffixName)){
                converter.convert(docxInputStream).as(DocumentType.TEXT).to(outputStream).as(DocumentType.PDF).execute();
            }
            docxInputStream.close();
            outputStream.close();

            stopWatch.stop();
            System.out.println("转为pdf文件耗时 ---> 耗时：" + stopWatch.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将图片转换成pdf文件
     * @param imgFilePath 需要被转换的img所存放的位置
     * @param pdfFilePath 转换后的pdf所存放的位置
     * @return
     * @throws IOException
     */
    public static void imgToPdf(String imgFilePath, String pdfFilePath) {
        File file=new File(imgFilePath);
        if(file.exists()){
            Document document = new Document();
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(pdfFilePath);
                PdfWriter.getInstance(document, fos);
                // 设置文档的大小
                document.setPageSize(PageSize.A4);
                // 打开文档
                document.open();
                // 读取一个图片
                Image image = Image.getInstance(imgFilePath);

                image.setAlignment(Image.ALIGN_CENTER);
                // 设置图片的绝对位置
                image.setAbsolutePosition(0, 0);
                //拉伸图片
                image.scaleAbsolute(600, 850);
                // 插入一个图片
                document.add(image);
            } catch (DocumentException de) {
                de.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            } finally {
                document.close();
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /***
     * 将pdf转为图片
     *
     * @param pdfPath : pdf地址
     * @param targetPath : 图片地址
     * @param heightOffset : 高
     * @param dpi : 清晰度
     **/
    public static void pdfToImage(String pdfPath, String targetPath, int heightOffset, int dpi) {
        try {
            File pdfFile = new File(pdfPath);
            FileInputStream instream = new FileInputStream(pdfFile);
            InputStream byteInputStream = null;
            try {
                PDDocument doc = PDDocument.load(instream);
                PDFRenderer renderer = new PDFRenderer(doc);
                int pageCount = doc.getNumberOfPages();

                List<BufferedImage> list = new ArrayList<BufferedImage>();
                if (pageCount > 0) {
                    int totalHeight = 0;
                    int width = 0;

                    for (int i = 0; i < pageCount; i++) {
                        BufferedImage image = renderer.renderImageWithDPI(i, dpi);
                        list.add(image);
                        totalHeight += image.getHeight();
                        if (width < image.getWidth()) {
                            width = image.getWidth();
                        }
                        image.flush();
                    }

                    BufferedImage tag = new BufferedImage(width, totalHeight, BufferedImage.TYPE_INT_RGB);
                    tag.getGraphics();
                    Graphics g = tag.createGraphics();
                    int startHeight = 0;
                    for (BufferedImage image : list) {
                        g.drawImage(image, 0, startHeight, width, image.getHeight(), null);
                        g.drawImage(image, 0, startHeight, width, image.getHeight(), null);
                        startHeight += image.getHeight() + heightOffset;
                    }
                    g.dispose();

                    ByteArrayOutputStream bs = new ByteArrayOutputStream();
                    ImageOutputStream imOut;
                    imOut = ImageIO.createImageOutputStream(bs);
                    ImageIO.write(tag, "png", imOut);
                    byteInputStream = new ByteArrayInputStream(bs.toByteArray());
                    byteInputStream.close();

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            File uploadFile = new File(targetPath);
            FileOutputStream fops;
            fops = new FileOutputStream(uploadFile);
            fops.write(readInputStream(byteInputStream));
            fops.flush();
            fops.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***
     * 创建pdf
     *
     * @param ftlTemplate : 模板信息
     * @param documentVo : 填充数据
     * @param outputFile : 输出文件
     **/
    public static void createPdf(FtlTemplateBean ftlTemplate, DocumentVo documentVo, String outputFile) {
        try {
            Map<String, Object> variables = documentVo.fillDataMap();
            String htmlContent = FreemarkerUtils.generate(ftlTemplate.getFtlTemplatePath(), ftlTemplate.getFtlTemplateName(), variables);
            generateByHtml(ftlTemplate.getFontsPath(), htmlContent, outputFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void generateByHtml(String fontsPath, String htmlContent, String outputFile) throws Exception {
        OutputStream out = null;
        ITextRenderer iTextRenderer = null;
        GenericObjectPool genericObjectPool = ITextRendererObjectFactory.getObjectPool(new String[]{fontsPath});
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            org.w3c.dom.Document doc = builder.parse(new ByteArrayInputStream(htmlContent.getBytes("UTF-8")));
            File f = new File(outputFile);
            if (f != null && !f.getParentFile().exists()) {
                f.getParentFile().mkdir();
            }
            out = new FileOutputStream(outputFile);
            iTextRenderer = (ITextRenderer)genericObjectPool.borrowObject();
            try {
                iTextRenderer.setDocument(doc, (String)null);
                iTextRenderer.layout();
                iTextRenderer.createPDF(out);
            } catch (Exception e1) {
                genericObjectPool.invalidateObject(iTextRenderer);
                iTextRenderer = null;
                throw e1;
            }
        } catch (Exception e2) {
            throw e2;
        } finally {
            if (out != null) {
                out.close();
            }
            if (iTextRenderer != null) {
                try {
                    genericObjectPool.returnObject(iTextRenderer);
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            }
        }
    }

    /**
     * 在pdf上添加图片
     * @param imgFilePath 加入的图片地址
     * @param pdfFilePath pdf地址
     * @param x 图片放入的x轴
     * @param y 图片放入的y轴
     * @return
     */
    public static boolean pdfAddImg(String imgFilePath, String pdfFilePath, int x, int y, int w, int h) {
        InputStream input = null;
        PdfReader reader = null;
        PdfStamper stamper = null;
        try {
            // 读取模板文件
            input = new FileInputStream(new File(pdfFilePath));
            reader = new PdfReader(input);
            stamper = new PdfStamper(reader, new FileOutputStream(pdfFilePath));

            // 读图片
            Image image = Image.getInstance(imgFilePath);
            // 获取操作的页面
            PdfContentByte under = stamper.getOverContent(1);
            // 根据域的大小缩放图片
            image.scaleToFit(w, h);
            // 添加图片
            image.setAbsolutePosition(x, y);
            under.addImage(image);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stamper != null) {
                    stamper.close();
                }
                if (reader != null) {
                    reader.close();
                }
                if (input != null) {
                    input.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    // todo Echarts 图形插入pdf
}
