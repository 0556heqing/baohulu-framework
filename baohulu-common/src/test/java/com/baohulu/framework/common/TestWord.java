package com.baohulu.framework.common;

import com.baohulu.framework.common.utils.file.WordUtils;
import com.baohulu.framework.common.utils.file.word.ChartData;
import com.baohulu.framework.common.utils.file.word.ImageData;
import com.baohulu.framework.common.utils.file.word.WordUtil;
import com.baohulu.framework.common.utils.file.word.enums.ChartTypeEnum;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xwpf.usermodel.*;
import org.junit.Test;

import java.io.*;
import java.util.*;

/**
 * @author heqing
 * @date 2022/11/10 10:25
 */
public class TestWord {

    @Test
    public void readText() throws IOException {
        System.out.println("-------docx----------");
        String docxFile = "D:\\workspace\\test\\test11.docx";
        List<String> contentList =  WordUtils.readText(docxFile);
        contentList.stream().forEach(System.out::println);


        System.out.println("-------doc----------");
        docxFile = "D:\\workspace\\test\\test.doc";
        contentList =  WordUtils.readText(docxFile);
        contentList.stream().forEach(System.out::println);
    }

    @Test
    public void readTable() throws IOException {
        System.out.println("-------docx----------");
        String docxFile = "D:\\workspace\\test\\test1.docx";
        List<List<Object>> tableList = WordUtils.readTable(docxFile);
        tableList.stream().forEach(System.out::println);


        System.out.println("-------doc----------");
        docxFile = "D:\\workspace\\test\\test.doc";
        tableList = WordUtils.readTable(docxFile);
        tableList.stream().forEach(System.out::println);
    }

    /**
     * word写入文字，千变万化。没办法提供统一的工具类样式。。这里只提供一个示例
     * @throws IOException
     */
    @Test
    public void writeText() throws IOException, InvalidFormatException {
        // 1、创建word文档对象
        XWPFDocument document = new XWPFDocument();

        // 2.组装数据
        XWPFParagraph titleParagraph = document.createParagraph();
        // 设置段落居中
        titleParagraph.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun titleRun = titleParagraph.createRun();
        titleRun.setText("Java操作word文档");
        titleRun.setFontSize(20);
        titleRun.setBold(true);

        XWPFParagraph firstParagraph = document.createParagraph();
        XWPFRun firstRun = firstParagraph.createRun();
        firstRun.setText("具体操作方式：");
        firstRun.setFontFamily("仿宋");
        firstRun.setFontSize(12);
        //换行
        firstParagraph.setWordWrap(true);


        XWPFParagraph twoParagraph = document.createParagraph();
        twoParagraph.setIndentationFirstLine(200);
        XWPFRun twoRun = twoParagraph.createRun();
        twoRun.setFontFamily("仿宋");
        twoRun.setFontSize(12);
        twoRun.setText("继承POI操作Word中类XWPFDocument。");

        // 4、输出到word文档
        FileOutputStream fos = new FileOutputStream("D:\\workspace\\test\\test1.docx");
        document.write(fos); // 导出word
        // 5、关闭流
        fos.close();
        document.close();
    }

    @Test
    public void writeImage() throws IOException, InvalidFormatException {
        // 1、创建word文档对象
        XWPFDocument document = new XWPFDocument();
        // 2.组装图形数据
        ImageData imageData = new ImageData();
        imageData.setPath("D:\\workspace\\test\\test.jpeg");
        // 3.添加表格
        WordUtils.writeImage(document, imageData);
        // 4、输出到word文档
        FileOutputStream fos = new FileOutputStream("D:\\workspace\\test\\test1.docx");
        document.write(fos); // 导出word
        // 5、关闭流
        fos.close();
        document.close();
    }

    @Test
    public void writeTable() throws IOException {
        // 1、创建word文档对象
        XWPFDocument document = new XWPFDocument();
        // 2.组装图形数据
        List<String> head = Arrays.asList("第一列", "第二列", "第三列", "第四列");
        List<List<Object>> dataList = new ArrayList<>();
        for(int i=0; i<5; i++) {
            List<Object> data = Arrays.asList(i, "贺小"+i, 0.56, new Date());
            dataList.add(data);
        }
        // 3.添加表格
        WordUtils.writeTable(document, head, dataList);
        // 4、输出到word文档
        FileOutputStream fos = new FileOutputStream("D:\\workspace\\test\\test1.doc");
        document.write(fos); // 导出word
        // 5、关闭流
        fos.close();
        document.close();
    }


    private ChartData getCharData() {
        ChartData chartData = new ChartData();
        LinkedHashMap<String, Integer> values = new LinkedHashMap<>();
        values.put("2021-01", 10);
        values.put("2021-02", 35);
        values.put("2021-03", 21);
        values.put("2021-04", 46);
        values.put("2021-05", 79);
        values.put("2021-06", 88);
        chartData.setValues(values);
        chartData.setTitle("图形标题");
        chartData.setTitleX("x轴标题");
        chartData.setTitleY("y轴标题");
        return chartData;
    }

    @Test
    public void writeChart() throws IOException, InvalidFormatException {
        // 1、创建word文档对象
        XWPFDocument document = new XWPFDocument();
        // 2.组装图形数据
        ChartData chartData = getCharData();
        // 3.绘制图形
        WordUtils.writeChart(document, chartData);
        chartData.setChartType(ChartTypeEnum.BAR);
        WordUtils.writeChart(document, chartData);
        chartData.setChartType(ChartTypeEnum.PIE);
        WordUtils.writeChart(document, chartData);
        // 4、输出到word文档
        FileOutputStream fos = new FileOutputStream("D:\\workspace\\test\\test1.docx");
        document.write(fos); // 导出word
        // 5、关闭流
        fos.close();
        document.close();
    }

    // todo 根据模板生成word

}
