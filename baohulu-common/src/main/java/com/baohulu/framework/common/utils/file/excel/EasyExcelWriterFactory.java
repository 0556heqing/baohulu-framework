package com.baohulu.framework.common.utils.file.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;

import java.io.File;
import java.io.OutputStream;
import java.util.List;

/**
 * 链式导出多个sheet的Excel
 *
 * @author heqing
 * @date 2022/11/04 16:40
 */
public class EasyExcelWriterFactory {

    private int sheetNo = 0;
    private final ExcelWriter excelWriter;

    public EasyExcelWriterFactory(OutputStream outputStream) {
        excelWriter = EasyExcel.write(outputStream).build();
    }

    public EasyExcelWriterFactory(File file) {
        excelWriter = EasyExcel.write(file).build();
    }

    public EasyExcelWriterFactory(String filePath) {
        excelWriter = EasyExcel.write(filePath).build();
    }

    /**
     * 链式模板表头写入
     *
     * @param headClazz 表头格式
     * @param data      数据 List<ExcelModel> 或者List<List<Object>>
     * @return
     */
    public EasyExcelWriterFactory writeModel(String sheetName, Class<?> headClazz, List<?> data) {
        excelWriter.write(data, EasyExcel.writerSheet(this.sheetNo++, sheetName).head(headClazz).build());
        return this;
    }

    /**
     * 链式自定义表头写入
     *
     * @param head
     * @param data      数据 List<ExcelModel> 或者List<List<Object>>
     * @param sheetName
     * @return
     */
    public EasyExcelWriterFactory write(String sheetName, List<List<String>> head, List<?> data) {
        excelWriter.write(data, EasyExcel.writerSheet(this.sheetNo++, sheetName).head(head).build());
        return this;
    }

    /**
     * 使用此类结束后，一定要关闭流
     */
    public void finish() {
        excelWriter.finish();
    }

}
