package com.baohulu.framework.common.file.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.*;
import com.alibaba.excel.enums.poi.FillPatternTypeEnum;
import com.baohulu.framework.common.file.CustomStringConverter;
import lombok.Data;

import java.util.Date;

/**
 * @ContentRowHeight :行高
 * @HeadRowHeight :行宽
 * @ColumnWidth :列宽
 *
 * @author heqing
 * @date 2022/11/08 16:05
 */
@Data
// 标题行高
@HeadRowHeight(20)
// 行高
@ContentRowHeight(15)
// 列宽
@ColumnWidth(30)
// 设置标题背景 IndexedColors.RED.getIndex()
@HeadStyle(fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND, fillForegroundColor = 10)
// 设置标题字体
@HeadFontStyle(fontHeightInPoints = 12)
// 内容的背景设置成绿色 IndexedColors.GREEN.getIndex()
@ContentStyle(fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND, fillForegroundColor = 17)
// 内容字体设置成20
@ContentFontStyle(fontHeightInPoints = 8)
public class DemoAnnotationData {

    // 字符串的头背景设置成粉红 IndexedColors.PINK.getIndex()
    @HeadStyle(fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND, fillForegroundColor = 14)
    // 字符串的头字体设置成20
    @HeadFontStyle(fontHeightInPoints = 12)
    // 字符串的内容的背景设置成天蓝 IndexedColors.SKY_BLUE.getIndex()
    @ContentStyle(fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND, fillForegroundColor = 40)
    // 字符串的内容字体设置成20
    @ContentFontStyle(fontHeightInPoints = 8)
    @ExcelProperty(value = {"主标题", "字符串标题"}, index = 0, converter = CustomStringConverter.class)
    private String string;

    // 占用2行
    @ContentLoopMerge(eachRow = 2)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ExcelProperty(value = {"主标题", "日期标题"}, index = 1)
    private Date date;

    @NumberFormat("#.##%")
    @ExcelProperty(value = "数字标题", index = 3)
    private Double doubleData;

    /**
     * 忽略这个字段
     */
    @ExcelIgnore
    private String ignore;

}
