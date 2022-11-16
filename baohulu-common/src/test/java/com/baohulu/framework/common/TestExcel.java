package com.baohulu.framework.common;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.enums.CellExtraTypeEnum;
import com.alibaba.excel.enums.WriteDirectionEnum;
import com.alibaba.excel.metadata.data.*;
import com.alibaba.excel.util.ListUtils;
import com.alibaba.excel.util.MapUtils;
import com.alibaba.excel.write.merge.LoopMergeStrategy;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.baohulu.framework.common.file.excel.CustomExcelReadListener;
import com.baohulu.framework.common.file.excel.CustomExcelWriteHandler;
import com.baohulu.framework.common.file.excel.model.DemoAnnotationData;
import com.baohulu.framework.common.file.excel.model.DemoData;
import com.baohulu.framework.common.file.excel.model.ImageDemoData;
import com.baohulu.framework.common.file.excel.model.WriteCellDemoData;
import com.baohulu.framework.common.utils.file.ExcelUtils;
import com.baohulu.framework.common.utils.file.FileUtils;
import com.baohulu.framework.common.utils.file.excel.EasyExcelWriterFactory;
import com.baohulu.framework.common.utils.file.excel.SheetData;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

/**
 * @author heqing
 * @date 2022/11/09 09:33
 */
public class TestExcel {

    private List<DemoAnnotationData> annotationData() {
        List<DemoAnnotationData> list = ListUtils.newArrayList();
        for (int i = 0; i < 10; i++) {
            DemoAnnotationData data = new DemoAnnotationData();
            data.setString("字符串" + i);
            data.setDate(new Date());
            data.setDoubleData(0.56);
            list.add(data);
        }
        return list;
    }

    private List<DemoData> data() {
        List<DemoData> list = ListUtils.newArrayList();
        for (int i = 0; i < 10; i++) {
            DemoData data = new DemoData();
            data.setName("字符串" + i);
            data.setDate(new Date());
            data.setNumber(0.56);
            list.add(data);
        }
        return list;
    }

    private List<List<String>> head() {
        List<List<String>> headList = new ArrayList<>();
        List<String> head1 = Arrays.asList("a");
        List<String> head2 = Arrays.asList("b");
        headList.add(head1);
        headList.add(head2);
        return headList;
    }

    private List<List<Object>> dataList() {
        List<List<Object>> dataList = new ArrayList<List<Object>>();
        List<Object> data1 = Arrays.asList(1, "hello");
        List<Object> data2 = Arrays.asList(2, "贺小白");
        dataList.add(data1);
        dataList.add(data2);
        return dataList;
    }

    private List<List<String>> variableTitleHead() {
        List<List<String>> list = ListUtils.newArrayList();
        List<String> head0 = ListUtils.newArrayList();
        head0.add("第一列");
        List<String> head1 = ListUtils.newArrayList();
        head1.add("第二列");
        List<String> head2 = ListUtils.newArrayList();
        head2.add("第三列");
        list.add(head0);
        list.add(head1);
        list.add(head2);
        return list;
    }

    @Test
    public void imageWrite() throws Exception {
        String fileName = "D:/workspace/test/test1.xlsx";

        String imagePath = "D:/workspace/test/test.jpeg";
        try (InputStream inputStream = FileUtils.openInputStream(new File(imagePath))) {
            List<ImageDemoData> list =  ListUtils.newArrayList();

            ImageDemoData imageDemoData = new ImageDemoData();
            list.add(imageDemoData);
            // 放入五种类型的图片 实际使用只要选一种即可
            imageDemoData.setByteArray(FileUtils.readFileToByteArray(new File(imagePath)));
            imageDemoData.setFile(new File(imagePath));
            imageDemoData.setString(imagePath);
            imageDemoData.setInputStream(inputStream);
            imageDemoData.setUrl(new URL(
                    "https://alifei01.cfp.cn/cms/image/image/1db876eb7b084f36b91517587536ba94.jpg"));

            // 这里演示
            // 需要额外放入文字
            // 而且需要放入2个图片
            // 第一个图片靠左
            // 第二个靠右 而且要额外的占用他后面的单元格
            WriteCellData<Void> writeCellData = new WriteCellData<>();
            imageDemoData.setWriteCellDataFile(writeCellData);
            // 这里可以设置为 EMPTY 则代表不需要其他数据了
            writeCellData.setType(CellDataTypeEnum.STRING);
            writeCellData.setStringValue("额外的放一些文字");

            // 可以放入多个图片
            List<ImageData> imageDataList = new ArrayList<>();
            ImageData imageData = new ImageData();
            imageDataList.add(imageData);
            writeCellData.setImageDataList(imageDataList);
            // 放入2进制图片
            imageData.setImage(FileUtils.readFileToByteArray(new File(imagePath)));
            // 图片类型
            imageData.setImageType(ImageData.ImageType.PICTURE_TYPE_PNG);
            // 上 右 下 左 需要留空
            // 这个类似于 css 的 margin
            // 这里实测 不能设置太大 超过单元格原始大小后 打开会提示修复。暂时未找到很好的解法。
            imageData.setTop(5);
            imageData.setRight(40);
            imageData.setBottom(5);
            imageData.setLeft(5);

            // 放入第二个图片
            imageData = new ImageData();
            imageDataList.add(imageData);
            writeCellData.setImageDataList(imageDataList);
            imageData.setImage(FileUtils.readFileToByteArray(new File(imagePath)));
            imageData.setImageType(ImageData.ImageType.PICTURE_TYPE_PNG);
            imageData.setTop(5);
            imageData.setRight(5);
            imageData.setBottom(5);
            imageData.setLeft(50);
            // 设置图片的位置 假设 现在目标 是 覆盖 当前单元格 和当前单元格右边的单元格
            // 起点相对于当前单元格为0 当然可以不写
            imageData.setRelativeFirstRowIndex(0);
            imageData.setRelativeFirstColumnIndex(0);
            imageData.setRelativeLastRowIndex(0);
            // 前面3个可以不写  下面这个需要写 也就是 结尾 需要相对当前单元格 往右移动一格
            // 也就是说 这个图片会覆盖当前单元格和 后面的那一格
            imageData.setRelativeLastColumnIndex(1);

            // 写入数据
            EasyExcel.write(fileName, ImageDemoData.class).sheet().doWrite(list);
        }
    }

    @Test
    public void writeCellDataWrite() {
        String fileName = "D:/workspace/test/test1.xlsx";
        WriteCellDemoData writeCellDemoData = new WriteCellDemoData();

        // 设置超链接
        WriteCellData<String> hyperlink = new WriteCellData<>("官方网站");
        writeCellDemoData.setHyperlink(hyperlink);
        HyperlinkData hyperlinkData = new HyperlinkData();
        hyperlink.setHyperlinkData(hyperlinkData);
        hyperlinkData.setAddress("https://github.com/alibaba/easyexcel");
        hyperlinkData.setHyperlinkType(HyperlinkData.HyperlinkType.URL);

        // 设置备注
        WriteCellData<String> comment = new WriteCellData<>("备注的单元格信息");
        writeCellDemoData.setCommentData(comment);
        CommentData commentData = new CommentData();
        comment.setCommentData(commentData);
        commentData.setAuthor("贺小白");
        commentData.setRichTextStringData(new RichTextStringData("这是一个备注"));
        // 备注的默认大小是按照单元格的大小 这里想调整到4个单元格那么大 所以向后 向下 各额外占用了一个单元格
        commentData.setRelativeLastColumnIndex(1);
        commentData.setRelativeLastRowIndex(1);

        // 设置公式
        WriteCellData<String> formula = new WriteCellData<>();
        writeCellDemoData.setFormulaData(formula);
        FormulaData formulaData = new FormulaData();
        formula.setFormulaData(formulaData);
        // 将 123456789 中的第一个数字替换成 2
        // 这里只是例子 如果真的涉及到公式 能内存算好尽量内存算好 公式能不用尽量不用
        formulaData.setFormulaValue("REPLACE(123456789,1,1,2)");

        // 设置单个单元格的样式 当然样式 很多的话 也可以用注解等方式。
        WriteCellData<String> writeCellStyle = new WriteCellData<>("单元格样式");
        writeCellStyle.setType(CellDataTypeEnum.STRING);
        writeCellDemoData.setWriteCellStyle(writeCellStyle);
        WriteCellStyle writeCellStyleData = new WriteCellStyle();
        writeCellStyle.setWriteCellStyle(writeCellStyleData);
        // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND 不然无法显示背景颜色.
        writeCellStyleData.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        // 背景绿色
        writeCellStyleData.setFillForegroundColor(IndexedColors.GREEN.getIndex());

        // 设置单个单元格多种样式
        WriteCellData<String> richTest = new WriteCellData<>();
        richTest.setType(CellDataTypeEnum.RICH_TEXT_STRING);
        writeCellDemoData.setRichText(richTest);
        RichTextStringData richTextStringData = new RichTextStringData();
        richTest.setRichTextStringDataValue(richTextStringData);
        richTextStringData.setTextString("红色绿色默认");
        // 前2个字红色
        WriteFont writeFont = new WriteFont();
        writeFont.setColor(IndexedColors.RED.getIndex());
        richTextStringData.applyFont(0, 2, writeFont);
        // 接下来2个字绿色
        writeFont = new WriteFont();
        writeFont.setColor(IndexedColors.GREEN.getIndex());
        richTextStringData.applyFont(2, 4, writeFont);

        List<WriteCellDemoData> data = new ArrayList<>();
        data.add(writeCellDemoData);
        EasyExcel.write(fileName, WriteCellDemoData.class).inMemory(true).sheet("模板").doWrite(data);
    }

    @Test
    public void writeAnnotation() {
        String fileName1 = "D:/workspace/test/test1.xlsx";

        // 注解写入方式
        ExcelUtils.write(fileName1, "test", DemoAnnotationData.class, annotationData());
    }

    @Test
    public void write() {
        // 写入
        String fileName1 = "D:/workspace/test/test1.xlsx";
        ExcelUtils.write(fileName1, "test", DemoData.class, data());

        // 不创建对象的写
        String fileName2 = "D:/workspace/test/test2.xlsx";
        ExcelUtils.write(fileName2, "test", head(), dataList());

        // 排除某个列
        String fileName3 = "D:/workspace/test/test3.xlsx";
        Set<String> excludeColumnFiledNames = new HashSet<>();
        excludeColumnFiledNames.add("date");
        ExcelUtils.writeExcludeColumn(fileName3, "test", DemoData.class, data(), excludeColumnFiledNames);

        // 只写入某个列
        String fileName4 = "D:/workspace/test/test4.xlsx";
        Set<String> includeColumnFieldNames = new HashSet<>();
        includeColumnFieldNames.add("string");
        ExcelUtils.writeIncludeColumn(fileName4, "test", DemoData.class, data(), includeColumnFieldNames);

        // 根据模板写入
        String templateFileName = "D:/workspace/test/test.xlsx";
        String fileName5 = "D:/workspace/test/test5.xlsx";
        ExcelUtils.write(templateFileName, fileName5, "模板", DemoData.class, data());

        // 带样式的写入
        String fileName6 = "D:/workspace/test/test6.xlsx";
        // 头的策略
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        // 背景设置为红色
        headWriteCellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontHeightInPoints((short)20);
        headWriteCellStyle.setWriteFont(headWriteFont);
        // 内容的策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND 不然无法显示背景颜色.头默认了 FillPatternType所以可以不指定
        contentWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        // 背景绿色
        contentWriteCellStyle.setFillForegroundColor(IndexedColors.GREEN.getIndex());
        WriteFont contentWriteFont = new WriteFont();
        // 字体大小
        contentWriteFont.setFontHeightInPoints((short)20);
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        // 这个策略是 头是头的样式 内容是内容的样式 其他的策略可以自己实现
        HorizontalCellStyleStrategy style = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
        ExcelUtils.write(fileName6, "test", DemoData.class, data(), style);

        // 合并单元格。 LoopMergeStrategy（行、列）
        String fileName7 = "D:/workspace/test/test7.xlsx";
        LoopMergeStrategy loopMergeStrategy = new LoopMergeStrategy(2, 1);
        ExcelUtils.write(fileName7, "test", DemoData.class, data(), loopMergeStrategy);

        // 自定义拦截器
        String fileName8 = "D:/workspace/test/test8.xlsx";
        ExcelUtils.write(fileName8, "test", DemoData.class, data(), new CustomExcelWriteHandler());

        // 可变标题处理
        String fileName9 = "D:/workspace/test/test9.xlsx";
        EasyExcel.write(fileName9, DemoData.class).head(variableTitleHead()).sheet("test").doWrite(data());

        // 分批次写入(不建议使用)
        ExcelUtils.writeAppend(fileName1, "test", DemoData.class, data());
        ExcelUtils.writeAppend(fileName1, "test", DemoData.class, data());

        // 分批次写入(建议使用)
        try (ExcelWriter excelWriter = EasyExcel.write(fileName1, DemoData.class).build()) {
            // 这里注意 如果同一个sheet只要创建一次
            WriteSheet writeSheet = EasyExcel.writerSheet("模板").build();
            // 去调用写入,这里我调用了五次，实际使用时根据数据库分页的总的页数来
            for (int i = 0; i < 5; i++) {
                // 分页去数据库查询数据 这里可以去数据库查询每一页的数据
                excelWriter.write(data(), writeSheet);
            }
        }
    }

    @Test
    public void writeMany() {
        String fileName1 = "D:/workspace/test/test1.xlsx";
        EasyExcelWriterFactory factory = ExcelUtils.writeWithSheets(fileName1);
        factory.write("test1", head(), dataList());
        factory.writeModel("test2", DemoData.class, data());
        factory.finish();

        // 同时写入多个sheet数据
        String fileName2 = "D:/workspace/test/test2.xlsx";
        List<SheetData> sheetDataList = new ArrayList<>();
        for(int i=0; i<3; i++) {
            SheetData sheetData = new SheetData();
            sheetData.setSheetName("test" + i);
            sheetData.setClazz(DemoData.class);
            sheetData.setData(data());
            sheetDataList.add(sheetData);
        }
        ExcelUtils.write(fileName2, sheetDataList);
    }

    @Test
    public void fill() {
        String fileName1 = "D:/workspace/test/test1.xlsx";
        String fileName2 = "D:/workspace/test/test2.xlsx";

        // 填充 {字段名}
        Map<String, Object> map = MapUtils.newHashMap();
        map.put("name", "张三");
        map.put("number", 5.2);
        EasyExcel.write(fileName2).withTemplate(fileName1).sheet().doFill(map);

        // 填充列表 {.字段名}
        EasyExcel.write(fileName2)
                .withTemplate(fileName1)
                .sheet()
                .doFill(() -> {
                    // 分页查询数据
                    return data();
                });

        // 复杂
        try (ExcelWriter excelWriter = EasyExcel.write(fileName2).withTemplate(fileName1).build()) {
            WriteSheet writeSheet = EasyExcel.writerSheet().build();
            // 这里注意 入参用了forceNewRow 代表在写入list的时候不管list下面有没有空行 都会创建一行，然后下面的数据往后移动。默认 是false，会直接使用下一行，如果没有则创建。
            // forceNewRow 如果设置了true,有个缺点 就是他会把所有的数据都放到内存了，所以慎用
            // 简单的说 如果你的模板有list,且list不是最后一行，下面还有数据需要填充 就必须设置 forceNewRow=true 但是这个就会把所有数据放到内存 会很耗内存
            // 如果数据量大 list不是最后一行 参照下一个
            FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
            excelWriter.fill(data(), fillConfig, writeSheet);
            Map<String, Object> map1 = MapUtils.newHashMap();
            map1.put("date", "2022年05月01日");
            map1.put("total", 10);
            excelWriter.fill(map1, writeSheet);
        }

        // 横向排列
        try (ExcelWriter excelWriter = EasyExcel.write(fileName2).withTemplate(fileName1).build()) {
            WriteSheet writeSheet = EasyExcel.writerSheet().build();
            FillConfig fillConfig = FillConfig.builder().direction(WriteDirectionEnum.HORIZONTAL).build();
            excelWriter.fill(data(), fillConfig, writeSheet);
            excelWriter.fill(data(), fillConfig, writeSheet);

            map = new HashMap<>();
            map.put("date", "2019年10月9日13:28:28");
            excelWriter.fill(map, writeSheet);
        }

        // 复杂排列
        try (ExcelWriter excelWriter = EasyExcel.write(fileName2).withTemplate(fileName1).build()) {
            WriteSheet writeSheet = EasyExcel.writerSheet().build();
            FillConfig fillConfig = FillConfig.builder().direction(WriteDirectionEnum.HORIZONTAL).build();
            // 如果有多个list 模板上必须有{前缀.} 这里的前缀就是 data1，然后多个list必须用 FillWrapper包裹
            excelWriter.fill(new FillWrapper("data1", data()), fillConfig, writeSheet);
            excelWriter.fill(new FillWrapper("data1", data()), fillConfig, writeSheet);
            excelWriter.fill(new FillWrapper("data2", data()), writeSheet);
            excelWriter.fill(new FillWrapper("data2", data()), writeSheet);

            map = new HashMap<>();
            map.put("date", "2019年10月9日13:28:28");

            excelWriter.fill(map, writeSheet);
        }
    }

    @Test
    public void read() {
        String fileName1 = "D:/workspace/test/test1.xlsx";

        List<Map<Integer, String>> syncRead = ExcelUtils.syncRead(fileName1, 1, 5);
        System.out.println("-->" + syncRead);

        List<DemoData> syncRead1 = ExcelUtils.syncReadModel(fileName1, DemoData.class, 1, 1);
        System.out.println("-->" + syncRead1);

        ExcelUtils.asyncReadModel(fileName1, new CustomExcelReadListener(), DemoAnnotationData.class, 1, 1);

        // 读取链接、批注
        EasyExcel.read(fileName1, DemoAnnotationData.class, new CustomExcelReadListener())
                // 需要读取批注 默认不读取
                .extraRead(CellExtraTypeEnum.COMMENT)
                // 需要读取超链接 默认不读取
                .extraRead(CellExtraTypeEnum.HYPERLINK)
                // 需要读取合并单元格信息 默认不读取
                .extraRead(CellExtraTypeEnum.MERGE)
                .sheet(1)
                .headRowNumber(1)
                .doRead();
    }
}
