package com.baohulu.framework.common.file;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.metadata.CellExtra;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.fastjson2.JSON;
import com.baohulu.framework.common.utils.file.excel.BaseExcelReadListener;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author heqing
 * @date 2022/11/04 19:45
 */
@Slf4j
public class CustomExcelReadListener<Object> extends BaseExcelReadListener<Object> implements ReadListener<Object> {

    @Override
    protected void processRows(List<Object> rows) {
        log.info("--------------------------------");
        rows.stream().forEach(System.out::println);
    }

    @Override
    protected int getMaxRowSize() {
        return 10;
    }

    @Override
    public void extra(CellExtra extra, AnalysisContext context) {
        log.info("读取到了一条额外信息:{}", JSON.toJSONString(extra));
        switch (extra.getType()) {
            case COMMENT:
                log.info("额外信息是批注,在rowIndex:{},columnIndex;{},内容是:{}", extra.getRowIndex(), extra.getColumnIndex(),
                        extra.getText());
                break;
            case HYPERLINK:
                log.info("额外信息是超链接,在rowIndex:{},columnIndex;{},内容是:{}", extra.getRowIndex(),
                        extra.getColumnIndex(), extra.getText());
                break;
            case MERGE:
                log.info(
                        "额外信息是超链接,而且覆盖了一个区间,在firstRowIndex:{},firstColumnIndex;{},lastRowIndex:{},lastColumnIndex:{}",
                        extra.getFirstRowIndex(), extra.getFirstColumnIndex(), extra.getLastRowIndex(),
                        extra.getLastColumnIndex());
                break;
            default:
        }
    }

    /**
     * 在转换异常 获取其他异常下会调用本接口。抛出异常则停止读取。如果这里不抛出异常则 继续读取下一行。
     *
     * @param exception
     * @param context
     * @throws Exception
     */
    @Override
    public void onException(Exception exception, AnalysisContext context) {
        log.error("解析失败，但是继续解析下一行:{}", exception.getMessage());
        // 如果是某一个单元格的转换异常 能获取到具体行号
        // 如果要获取头的信息 配合invokeHeadMap使用
        if (exception instanceof ExcelDataConvertException) {
            ExcelDataConvertException excelDataConvertException = (ExcelDataConvertException)exception;
            log.error("第{}行，第{}列解析异常，数据为:{}", excelDataConvertException.getRowIndex(),
                    excelDataConvertException.getColumnIndex(), excelDataConvertException.getCellData());
        }
    }
}
