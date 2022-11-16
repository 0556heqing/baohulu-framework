package com.baohulu.framework.common;

import com.baohulu.framework.common.domain.BaseDomain;
import com.baohulu.framework.common.utils.file.PdfUtils;
import com.baohulu.framework.common.utils.file.pdf.FtlTemplateBean;
import org.junit.Test;

import java.io.IOException;

/**
 * pdf插入echart
 *
 * @author heqing
 * @date 2022/11/15 10:18
 */
public class TestPdf extends BaseDomain {

    @Test
    public void wordToPdf() throws IOException {
        String sourcePath = "D:\\workspace\\test\\test.doc";
        String targetPath = "D:\\workspace\\test\\test1.pdf";
        PdfUtils.wordToPdf(sourcePath, targetPath);
    }

    @Test
    public void imgToPdf() throws IOException {
        String sourcePath = "D:\\workspace\\test\\test.jpeg";
        String targetPath = "D:\\workspace\\test\\1.pdf";
        PdfUtils.imgToPdf(sourcePath, targetPath);
    }

    @Test
    public void pdfToImage() {
        String pdfPath = "D:\\workspace\\test\\test111.pdf";
        String pngPath = "D:\\workspace\\test\\1.png";
        // 36:模糊  72:正常  144:清晰  216:高清
        PdfUtils.pdfToImage(pdfPath, pngPath, 0, 144);
    }

    @Test
    public void createPdf() {
        String filePath = "D:\\workspace\\test\\1.pdf";
        
        String ftlTemplateAbsolutePath = this.getClass().getClassLoader().getResource("ftl").getFile();
        String fontsAbsolutePath= this.getClass().getClassLoader().getResource("fonts").getFile();

        FtlTemplateBean ftlTemplateBean = new FtlTemplateBean();
        ftlTemplateBean.setFtlTemplatePath(ftlTemplateAbsolutePath);
        ftlTemplateBean.setFontsPath(fontsAbsolutePath);
        ftlTemplateBean.setFtlTemplateName("recipe.ftl");
        PdfUtils.createPdf(ftlTemplateBean, getFrameworkDeta(), filePath);
    }

    @Test
    public void pdfAddImg() {
        String pngPath = "D:\\workspace\\test\\test.jpeg";
        String pdfPath = "D:\\workspace\\test\\1.pdf";
        PdfUtils.pdfAddImg(pngPath, pdfPath, 100, 100, 200, 200);
    }
}
