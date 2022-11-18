package com.baohulu.framework.common.utils.file;

import com.baohulu.framework.basic.enums.ReturnEnum;
import com.baohulu.framework.basic.exception.BusinessException;
import com.baohulu.framework.common.utils.StringUtils;
import com.baohulu.framework.common.utils.file.word.ChartData;
import com.baohulu.framework.common.utils.file.word.ImageData;
import com.baohulu.framework.common.utils.file.word.enums.ChartTypeEnum;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * word文档工具类
 *
 * @author heqing
 * @date 2022/11/10 10:32
 */
public class WordUtils extends FileUtils {

    /**
     * 读取word文档中的段落
     *
     * @param filename  文件地址
     */
    public static List<String> readText(String filename) throws IOException {
        File source = new File(filename);
        if (!source.exists()) {
            throw new BusinessException(ReturnEnum.PARAM_ERROR, "word文档不存在");
        }

        List<String> contentList = new LinkedList<>();

        InputStream is = new FileInputStream(source);
        String suffixName = getSuffixName(filename);
        if(DOCX.equals(suffixName)) {
            XWPFDocument document = new XWPFDocument(is);
            contentList = readText(document);
            document.close();
        } else if(DOC.equals(suffixName)) {
            HWPFDocument document = new HWPFDocument(is);
            contentList = readText(document);
            document.close();
        }
        is.close();
        return contentList;
    }

    /**
     * 读取word文档中的段落
     *
     * @param document  doc文件
     */
    public static List<String> readText(HWPFDocument document) {
        List<String> contentList = new LinkedList<>();
        Range range = document.getRange();
        int paraNum = range.numParagraphs();
        for (int i=0; i<paraNum; i++) {
            String content = range.getParagraph(i).text();
            if(StringUtils.isNotEmpty(content)) {
                contentList.add(content);
            }
        }
        return contentList;
    }

    /**
     * 读取word文档中的段落
     *
     * @param document  docx文件
     */
    public static List<String> readText(XWPFDocument document) {
        List<String> contentList = new LinkedList<>();
        List<XWPFParagraph> paras = document.getParagraphs();
        for (XWPFParagraph para : paras) {
            String content = para.getText();
            if(StringUtils.isNotEmpty(content)) {
                contentList.add(content);
            }
        }
        return contentList;
    }

    /**
     * 读取word文档中的表格
     *
     * @param filename  文件地址
     */
    public static List<List<Object>> readTable(String filename) throws IOException {
        File source = new File(filename);
        if (!source.exists()) {
            throw new BusinessException(ReturnEnum.PARAM_ERROR, "word文档不存在");
        }

        List<List<Object>> contentList = new LinkedList<>();
        InputStream is = new FileInputStream(source);
        String suffixName = getSuffixName(filename);
        if(DOCX.equals(suffixName)) {
            XWPFDocument document = new XWPFDocument(is);
            contentList = readTable(document);
            document.close();
        } else if(DOC.equals(suffixName)) {
            HWPFDocument document = new HWPFDocument(is);
            contentList = readTable(document);
            document.close();
        }
        is.close();
        return contentList;
    }

    /**
     * 读取word文档中的表格
     *
     * @param document  docx文件
     */
    public static List<List<Object>> readTable(XWPFDocument document) {
        List<List<Object>> contentList = new LinkedList<>();
        //获取文档中所有的表格
        List<XWPFTable> tables = document.getTables();
        for (XWPFTable table : tables) {
            //获取表格对应的行
            List<XWPFTableRow> rows = table.getRows();
            for (XWPFTableRow row : rows) {
                List<Object> dataList = new LinkedList<>();
                //获取行对应的单元格
                List<XWPFTableCell> cells = row.getTableCells();
                for (XWPFTableCell cell : cells) {
                    String content = cell.getText();
                    if(StringUtils.isNotEmpty(content)) {
                        dataList.add(content);
                    }
                }
                contentList.add(dataList);
            }
        }
        return contentList;
    }

    /**
     * 读取word文档中的表格
     *
     * @param document  doc文件
     */
    public static List<List<Object>> readTable(HWPFDocument document) {
        List<List<Object>> contentList = new LinkedList<>();
        Range range = document.getRange();
        //遍历range范围内的table。
        TableIterator tableIter = new TableIterator(range);
        while (tableIter.hasNext()) {
            Table table = tableIter.next();
            int rowNum = table.numRows();
            for (int j=0; j<rowNum; j++) {
                List<Object> dataList = new ArrayList<>();
                TableRow row = table.getRow(j);
                int cellNum = row.numCells();
                for (int k=0; k<cellNum; k++) {
                    TableCell cell = row.getCell(k);
                    String content = cell.text().trim();
                    if(StringUtils.isNotEmpty(content)) {
                        dataList.add(content);
                    }
                }
                contentList.add(dataList);
            }
        }
        return contentList;
    }

    /**
     * 将图片写入word文档中
     *
     * @param document  docx文件
     * @param imageData  图片信息
     */
    public static void writeImage(XWPFDocument document, ImageData imageData) throws IOException, InvalidFormatException {
        if(imageData == null || StringUtils.isEmpty(imageData.getPath())) {
            throw new BusinessException(ReturnEnum.PARAM_ERROR, "word文档图片数据错误");
        }
        File source = new File(imageData.getPath());
        if (!source.exists()) {
            throw new BusinessException(ReturnEnum.PARAM_ERROR, "word文档图片不存在");
        }
        if(imageData.getWidth() == null){
            imageData.setWidth(450);
        }
        if(imageData.getHeight() == null){
            imageData.setHeight(300);
        }
        
        // 创建一个段落对象。
        XWPFParagraph paragraph = document.createParagraph();
        XWPFRun run = paragraph.createRun();
        // 插入图片
        run.addPicture(new FileInputStream(imageData.getPath()),
                XWPFDocument.PICTURE_TYPE_PNG,
                imageData.getName(),
                Units.toEMU(imageData.getWidth()),
                Units.toEMU(imageData.getHeight()));
    }

    /**
     * 将表格写入word文档中
     *
     * @param document  docx文件
     * @param head  表头信息
     * @param dataList  表格内容
     */
    public static void writeTable(XWPFDocument document, List<String> head, List<List<Object>> dataList) {
        XWPFTable table = document.createTable();
        table.setWidth(8310);
        // 设置标题行
        XWPFTableRow headRow = table.getRow(0);
        headRow.setHeight(450);
        for(int i = 0; i < head.size(); i++) {
            XWPFTableCell cell = null;
            if(i == 0) {
                cell = headRow.getCell(0);
            } else {
                cell = headRow.addNewTableCell();
            }
            CTP ctP = (cell.getCTTc().sizeOfPArray() == 0) ? cell.getCTTc().addNewP() : cell.getCTTc().getPArray(0);
            XWPFParagraph p = cell.getParagraph(ctP);
            p.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun headRun = p.createRun();
            headRun.setText(head.get(i));
            headRun.setFontSize(14);
            //是否粗体
            headRun.setBold(true);
        }
        // 设置表格内容
        for(int i = 0; i < dataList.size(); i++) {
            XWPFTableRow tableRow= table.createRow();
            tableRow.setHeight(400);
            List<Object> data = dataList.get(i);
            for(int j = 0; j < data.size(); j++) {
                tableRow.getCell(j).setText(data.get(j).toString());
            }
        }
    }

    /**
     * 将图形写入word文档中
     *
     * @param document  docx文件
     * @param chartData  图形信息
     */
    public static void writeChart(XWPFDocument document, ChartData chartData) throws IOException, InvalidFormatException {
        // 校验数据
        if(chartData == null || chartData.getValues() == null || chartData.getValues().isEmpty()) {
            throw new BusinessException(ReturnEnum.PARAM_ERROR, "word文档图形数据错误");
        }

        // 1、创建chart图表对象,抛出异常
        XWPFChart chart = document.createChart(15 * Units.EMU_PER_CENTIMETER, 10 * Units.EMU_PER_CENTIMETER);

        // 2、图表相关设置
        // 图表标题
        chart.setTitleText(chartData.getTitle());
        // 图例是否覆盖标题
        chart.setTitleOverlay(false);

        // 3、图例设置
        XDDFChartLegend legend = chart.getOrAddLegend();
        // 图例位置:上下左右
        legend.setPosition(LegendPosition.TOP);

        // 4、X轴(分类轴)相关设置
        // 创建X轴,并且指定位置
        XDDFCategoryAxis xAxis = null;
        // 设置X轴数据
        Set<String> keys = chartData.getValues().keySet();
        String[] xAxisData = new String[keys.size()];
        keys.toArray(xAxisData);
        XDDFCategoryDataSource xAxisSource = XDDFDataSourcesFactory.fromArray(xAxisData);

        // 5、Y轴(值轴)相关设置
        // 创建Y轴,指定位置
        XDDFValueAxis yAxis = null;
        // 设置Y轴数据
        List<Integer> valueList = chartData.getValues().values().stream().collect(Collectors.toList());
        Integer[] yAxisData = new Integer[valueList.size()];
        valueList.toArray(yAxisData);
        XDDFNumericalDataSource<Integer> yAxisSource = XDDFDataSourcesFactory.fromArray(yAxisData);

        // 6. 组装图形
        ChartTypeEnum chartType = chartData.getChartType();
        if(chartType == null) {
            chartType = ChartTypeEnum.LINE;
        }
        switch(chartType) {
            case LINE:
                xAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
                // x轴标题
                xAxis.setTitle(chartData.getTitleX());
                yAxis = chart.createValueAxis(AxisPosition.LEFT);
                // Y轴标题
                yAxis.setTitle(chartData.getTitleY());
                // 7、创建折线图对象
                XDDFLineChartData lineChart = (XDDFLineChartData) chart.createData(ChartTypes.LINE, xAxis, yAxis);

                // 8、加载折线图数据集
                XDDFLineChartData.Series lineSeries = (XDDFLineChartData.Series) lineChart.addSeries(xAxisSource, yAxisSource);
                // 线条样式:true平滑曲线,false折线
                lineSeries.setSmooth(true);
                // 标记点大小
                lineSeries.setMarkerSize((short) 6);
                // 标记点样式
                lineSeries.setMarkerStyle(MarkerStyle.CIRCLE);

                // 9、绘制折线图
                chart.plot(lineChart);
                break;
            case BAR:
                xAxis = chart.createCategoryAxis(AxisPosition.BOTTOM);
                // x轴标题
                xAxis.setTitle(chartData.getTitleX());
                yAxis = chart.createValueAxis(AxisPosition.LEFT);
                // Y轴标题
                yAxis.setTitle(chartData.getTitleY());
                // 设置图柱的位置:BETWEEN居中
                yAxis.setCrossBetween(AxisCrossBetween.BETWEEN);
                // 7、创建柱状图对象
                XDDFBarChartData barChart = (XDDFBarChartData) chart.createData(ChartTypes.BAR, xAxis, yAxis);
                // 设置柱状图的方向:BAR横向,COL竖向,默认是BAR
                barChart.setBarDirection(BarDirection.COL);

                // 8、加载柱状图数据集
                XDDFBarChartData.Series barSeries = (XDDFBarChartData.Series) barChart.addSeries(xAxisSource, yAxisSource);
                // 图例标题
                barSeries.setTitle(chartData.getTitleY(), null);

                // 9、绘制柱状图
                chart.plot(barChart);
                break;
            case PIE:
                // 7、创建饼图对象,饼状图不需要X,Y轴,只需要数据集即可
                XDDFPieChartData pieChart = (XDDFPieChartData) chart.createData(ChartTypes.PIE, xAxis, yAxis);

                // 8、加载饼图数据集
                XDDFPieChartData.Series pieSeries = (XDDFPieChartData.Series) pieChart.addSeries(xAxisSource, yAxisSource);
                // 系列提示标题
                pieSeries.setTitle(chartData.getTitleY(), null);

                // 9、绘制饼图
                chart.plot(pieChart);
                break;
            default: throw new BusinessException(ReturnEnum.PARAM_ERROR, "暂不支持改类型的图形");
        }
    }

}
