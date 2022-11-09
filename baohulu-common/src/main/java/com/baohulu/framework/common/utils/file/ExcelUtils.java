package com.baohulu.framework.common.utils.file;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.baohulu.framework.basic.consts.Separators;
import com.baohulu.framework.common.utils.file.excel.EasyExcelWriterFactory;
import com.baohulu.framework.common.utils.file.excel.SheetData;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * EasyExcel 工具类(无JAVA模型读取excel数据)
 * 参考链接：https://easyexcel.opensource.alibaba.com/docs/current/
 *
 * @author heqing
 * @date 2022/11/04 16:41
 */
public class ExcelUtils extends FileUtils {

    /**
     * 同步无模型读（默认读取sheet0,从第2行开始读）
     *
     * @param filePath excel文件的绝对路径
     */
    public static List<Map<Integer, String>> syncRead(String filePath) {
        return EasyExcelFactory.read(filePath).sheet().doReadSync();
    }

    /**
     * 同步无模型读（自定义读取sheetX，从第2行开始读）
     *
     * @param filePath excel文件的绝对路径
     * @param sheetNo  sheet页号，从0开始
     */
    public static List<Map<Integer, String>> syncRead(String filePath, Integer sheetNo) {
        return EasyExcelFactory.read(filePath).sheet(sheetNo).doReadSync();
    }

    /**
     * 同步无模型读（指定sheet和表头占的行数）
     *
     * @param filePath
     * @param sheetNo    sheet页号，从0开始
     * @param headRowNum 表头占的行数，从0开始（如果要连表头一起读出来则传0）
     */
    public static List<Map<Integer, String>> syncRead(String filePath, Integer sheetNo, Integer headRowNum) {
        return EasyExcelFactory.read(filePath).sheet(sheetNo).headRowNumber(headRowNum).doReadSync();
    }

    /**
     * 同步无模型读（指定sheet和表头占的行数）
     *
     * @param inputStream
     * @param sheetNo     sheet页号，从0开始
     * @param headRowNum  表头占的行数，从0开始（如果要连表头一起读出来则传0）
     */
    public static List<Map<Integer, String>> syncRead(InputStream inputStream, Integer sheetNo, Integer headRowNum) {
        return EasyExcelFactory.read(inputStream).sheet(sheetNo).headRowNumber(headRowNum).doReadSync();
    }

    /**
     * 同步无模型读（指定sheet和表头占的行数）
     *
     * @param file
     * @param sheetNo    sheet页号，从0开始
     * @param headRowNum 表头占的行数，从0开始（如果要连表头一起读出来则传0）
     */
    public static List<Map<Integer, String>> syncRead(File file, Integer sheetNo, Integer headRowNum) {
        return EasyExcelFactory.read(file).sheet(sheetNo).headRowNumber(headRowNum).doReadSync();
    }

    //====================================================将excel数据同步到JAVA模型属性里===============================================================

    /**
     * 同步按模型读（默认读取sheet0,从第2行开始读）
     *
     * @param filePath
     * @param clazz    模型的类类型（excel数据会按该类型转换成对象）
     */
    public static <T> List<T> syncReadModel(String filePath, Class<T> clazz) {
        return EasyExcelFactory.read(filePath).sheet().head(clazz).doReadSync();
    }

    /**
     * 同步按模型读（默认表头占一行，从第2行开始读）
     *
     * @param filePath
     * @param clazz    模型的类类型（excel数据会按该类型转换成对象）
     * @param sheetNo  sheet页号，从0开始
     */
    public static <T> List<T> syncReadModel(String filePath, Class<T> clazz, Integer sheetNo) {
        return EasyExcelFactory.read(filePath).sheet(sheetNo).head(clazz).doReadSync();
    }

    /**
     * 同步按模型读（指定sheet和表头占的行数）
     *
     * @param inputStream
     * @param clazz       模型的类类型（excel数据会按该类型转换成对象）
     * @param sheetNo     sheet页号，从0开始
     * @param headRowNum  表头占的行数，从0开始（如果要连表头一起读出来则传0）
     */
    public static <T> List<T> syncReadModel(InputStream inputStream, Class<T> clazz, Integer sheetNo, Integer headRowNum) {
        return EasyExcelFactory.read(inputStream).sheet(sheetNo).headRowNumber(headRowNum).head(clazz).doReadSync();
    }

    /**
     * 同步按模型读（指定sheet和表头占的行数）
     *
     * @param file
     * @param clazz      模型的类类型（excel数据会按该类型转换成对象）
     * @param sheetNo    sheet页号，从0开始
     * @param headRowNum 表头占的行数，从0开始（如果要连表头一起读出来则传0）
     */
    public static <T> List<T> syncReadModel(File file, Class<T> clazz, Integer sheetNo, Integer headRowNum) {
        return EasyExcelFactory.read(file).sheet(sheetNo).headRowNumber(headRowNum).head(clazz).doReadSync();
    }

    /**
     * 同步按模型读（指定sheet和表头占的行数）
     *
     * @param filePath
     * @param clazz      模型的类类型（excel数据会按该类型转换成对象）
     * @param sheetNo    sheet页号，从0开始
     * @param headRowNum 表头占的行数，从0开始（如果要连表头一起读出来则传0）
     */
    public static <T> List<T> syncReadModel(String filePath, Class<T> clazz, Integer sheetNo, Integer headRowNum) {
        return EasyExcelFactory.read(filePath).sheet(sheetNo).headRowNumber(headRowNum).head(clazz).doReadSync();
    }

    /**
     * 异步无模型读（默认读取sheet0,从第2行开始读）
     * 由于默认一行行的读取excel，所以需要创建excel一行一行的回调监听器
     *
     * @param excelListener 监听器，在监听器中可以处理行数据LinkedHashMap，表头数据，异常处理等
     * @param filePath      表头占的行数，从0开始（如果要连表头一起读出来则传0）
     */
    public static void asyncRead(String filePath, AnalysisEventListener excelListener) {
        EasyExcelFactory.read(filePath, excelListener).sheet().doRead();
    }

    /**
     * 异步无模型读（默认表头占一行，从第2行开始读）
     *
     * @param filePath      表头占的行数，从0开始（如果要连表头一起读出来则传0）
     * @param excelListener 监听器，在监听器中可以处理行数据LinkedHashMap，表头数据，异常处理等
     * @param sheetNo       sheet页号，从0开始
     */
    public static void asyncRead(String filePath, AnalysisEventListener excelListener, Integer sheetNo) {
        EasyExcelFactory.read(filePath, excelListener).sheet(sheetNo).doRead();
    }

    /**
     * 异步无模型读（指定sheet和表头占的行数）
     *
     * @param inputStream
     * @param excelListener 监听器，在监听器中可以处理行数据LinkedHashMap，表头数据，异常处理等
     * @param sheetNo       sheet页号，从0开始
     * @param headRowNum    表头占的行数，从0开始（如果要连表头一起读出来则传0）
     */
    public static void asyncRead(InputStream inputStream, AnalysisEventListener excelListener, Integer sheetNo, Integer headRowNum) {
        EasyExcelFactory.read(inputStream, excelListener).sheet(sheetNo).headRowNumber(headRowNum).doRead();
    }

    /**
     * 异步无模型读（指定sheet和表头占的行数）
     *
     * @param file
     * @param excelListener 监听器，在监听器中可以处理行数据LinkedHashMap，表头数据，异常处理等
     * @param sheetNo       sheet页号，从0开始
     * @param headRowNum    表头占的行数，从0开始（如果要连表头一起读出来则传0）
     */
    public static void asyncRead(File file, AnalysisEventListener excelListener, Integer sheetNo, Integer headRowNum) {
        EasyExcelFactory.read(file, excelListener).sheet(sheetNo).headRowNumber(headRowNum).doRead();
    }

    /**
     * 异步无模型读（指定sheet和表头占的行数）
     * 指定模型会报错 castException
     *
     * @param filePath
     * @param excelListener 监听器，在监听器中可以处理行数据LinkedHashMap，表头数据，异常处理等
     * @param sheetNo       sheet页号，从0开始
     * @param headRowNum    表头占的行数，从0开始（如果要连表头一起读出来则传0）
     * @return
     */
    public static void asyncRead(String filePath, AnalysisEventListener<?> excelListener, Integer sheetNo, Integer headRowNum) {
        EasyExcelFactory.read(filePath, excelListener).sheet(sheetNo).headRowNumber(headRowNum).doRead();
    }

    /**
     * 异步按模型读取（默认读取sheet0,从第2行开始读）
     *
     * @param filePath
     * @param excelListener 监听器，在监听器中可以处理行数据LinkedHashMap，表头数据，异常处理等
     * @param clazz         模型的类类型（excel数据会按该类型转换成对象）
     */
    public static void asyncReadModel(String filePath, AnalysisEventListener excelListener, Class clazz) {
        EasyExcelFactory.read(filePath, clazz, excelListener).sheet().doRead();
    }

    /**
     * 异步按模型读取（默认表头占一行，从第2行开始读）
     *
     * @param filePath
     * @param excelListener 监听器，在监听器中可以处理行数据LinkedHashMap，表头数据，异常处理等
     * @param clazz         模型的类类型（excel数据会按该类型转换成对象）
     * @param sheetNo       sheet页号，从0开始
     */
    public static void asyncReadModel(String filePath, AnalysisEventListener excelListener, Class clazz, Integer sheetNo) {
        EasyExcelFactory.read(filePath, clazz, excelListener).sheet(sheetNo).doRead();
    }

    /**
     * 异步按模型读取
     *
     * @param inputStream
     * @param excelListener 监听器，在监听器中可以处理行数据LinkedHashMap，表头数据，异常处理等
     * @param clazz         模型的类类型（excel数据会按该类型转换成对象）
     * @param sheetNo       sheet页号，从0开始
     * @param headRowNum    表头占的行数，从0开始（如果要连表头一起读出来则传0）
     */
    public static void asyncReadModel(InputStream inputStream, AnalysisEventListener excelListener, Class clazz, Integer sheetNo, Integer headRowNum) {
        EasyExcelFactory.read(inputStream, clazz, excelListener).sheet(sheetNo).headRowNumber(headRowNum).doRead();
    }

    /**
     * 异步按模型读取
     *
     * @param file
     * @param excelListener 监听器，在监听器中可以处理行数据LinkedHashMap，表头数据，异常处理等
     * @param clazz         模型的类类型（excel数据会按该类型转换成对象）
     * @param sheetNo       sheet页号，从0开始
     * @param headRowNum    表头占的行数，从0开始（如果要连表头一起读出来则传0）
     */
    public static void asyncReadModel(File file, AnalysisEventListener excelListener, Class clazz, Integer sheetNo, Integer headRowNum) {
        EasyExcelFactory.read(file, clazz, excelListener).sheet(sheetNo).headRowNumber(headRowNum).doRead();
    }

    /**
     * 异步按模型读取
     *
     * @param filePath
     * @param excelListener 监听器，在监听器中可以处理行数据LinkedHashMap，表头数据，异常处理等
     * @param clazz         模型的类类型（excel数据会按该类型转换成对象）
     * @param sheetNo       sheet页号，从0开始
     * @param headRowNum    表头占的行数，从0开始（如果要连表头一起读出来则传0）
     */
    public static void asyncReadModel(String filePath, AnalysisEventListener excelListener, Class clazz, Integer sheetNo, Integer headRowNum) {
        EasyExcelFactory.read(filePath, clazz, excelListener).sheet(sheetNo).headRowNumber(headRowNum).doRead();
    }

    /**
     * 根据excel模板文件写入文件
     *
     * @param filePath 写入文件地址
     * @param sheetName 表单名
     * @param clazz 数据对象模型(使用注解 @ExcelProperty)
     * @param data 数据
     */
    public static void write(String filePath, String sheetName, Class<?> clazz, List<?> data) {
        EasyExcel.write(filePath, clazz).sheet(sheetName).doWrite(data);
    }

    /**
     * (不建议直接使用本方法)
     * 分批次写入数据。 例如有1000万数据，写入List对内存造成影响。建议每次1000分次写入
     *  在数据量不大的情况下可以使用（5000以内，具体也要看实际情况），数据量大参照 重复多次写入
     *
     * @param filePath 写入文件地址
     * @param sheetName 表单名
     * @param clazz 数据对象模型(使用注解 @ExcelProperty)
     * @param data 数据
     */
    @Deprecated
    public static void writeAppend(String filePath, String sheetName, Class<?> clazz, List<?> data) {
        File file = new File(filePath);
        String temp = file.getParent() + "\\temp_" + getFileName(filePath) + Separators.POINT + getSuffixName(filePath);
        File tempFile = new File(temp);
        if (file.exists()){
            // 第二次按照原有格式，不需要表头，追加写入
            EasyExcel.write(file, clazz).needHead(false).withTemplate(file).file(tempFile).sheet().doWrite(data);
        }else {
            // 第一次写入需要表头
            EasyExcel.write(file,clazz).sheet(sheetName).doWrite(data);
        }

        if (tempFile.exists()){
            file.delete();
            tempFile.renameTo(file);
        }
    }

    /**
     * 不创建对象的写
     *
     * @param filePath  写入文件弟子
     * @param sheetName sheet名称
     * @param head      表头数据
     * @param data      表内容数据
     */
    public static void write(String filePath, String sheetName, List<List<String>> head, List<List<Object>> data) {
        EasyExcel.write(filePath).head(head).sheet(sheetName).doWrite(data);
    }

    /**
     * 根据excel模板文件写入文件
     *
     * @param templateFileName 模板名
     * @param filePath 写入文件地址
     * @param sheetName 表单名
     * @param clazz 数据对象模型(使用注解 @ExcelProperty)
     * @param data 数据
     */
    public static void write(String templateFileName, String filePath, String sheetName, Class<?> clazz, List<?> data) {
        EasyExcel.write(filePath, clazz).withTemplate(templateFileName).sheet(sheetName).doWrite(data);
    }

    /**
     * 根据excel模板文件写入文件(适用 同一个对象/不同对象，写到不同的sheet)
     *
     * @param filePath 写入文件地址
     * @param sheetDataList 数据
     */
    public static void write(String filePath, List<SheetData> sheetDataList) {
        try (ExcelWriter excelWriter = EasyExcel.write(filePath).build()) {
            int index = 0;
            for (SheetData sheetData : sheetDataList) {
                WriteSheet writeSheet = EasyExcel.writerSheet(index++, sheetData.getSheetName()).head(sheetData.getClazz()).build();
                excelWriter.write(sheetData.getData(), writeSheet);
            }
        }
    }

    /**
     * 带特殊处理的写入
     *
     * @param filePath 写入文件地址
     * @param sheetName 表单名
     * @param clazz 数据对象模型(使用注解 @ExcelProperty)
     * @param data 数据
     * @param writeHandler 拦截器
     */
    public static void write(String filePath, String sheetName, Class<?> clazz, List<?> data, WriteHandler writeHandler) {
        EasyExcel.write(filePath, clazz).registerWriteHandler(writeHandler).sheet(sheetName).doWrite(data);
    }

    /**
     * 文件写入文件(忽略的列)
     *
     * @param filePath 写入文件地址
     * @param sheetName 表单名
     * @param clazz 数据对象模型(使用注解 @ExcelProperty)
     * @param data 数据
     * @param excludeColumnFiledNames 忽略的列
     */
    public static void writeExcludeColumn(String filePath, String sheetName, Class<?> clazz, List<?> data, Set<String> excludeColumnFiledNames) {
        EasyExcel.write(filePath, clazz).excludeColumnFieldNames(excludeColumnFiledNames).sheet(sheetName).doWrite(data);
    }

    /**
     * 写入文件(导入的列)
     *
     * @param filePath 写入文件地址
     * @param sheetName 表单名
     * @param clazz 数据对象模型(使用注解 @ExcelProperty)
     * @param data 数据
     *  @param includeColumnFieldNames 导入的列
     */
    public static void writeIncludeColumn(String filePath, String sheetName, Class<?> clazz, List<?> data, Set<String> includeColumnFieldNames) {
        EasyExcel.write(filePath, clazz).includeColumnFieldNames(includeColumnFieldNames).sheet(sheetName).doWrite(data);
    }

    /**
     * 多个sheet页的数据链式写入
     * ExcelUtil.writeWithSheets(outputStream)
     * .writeModel(ExcelModel.class, excelModelList, "sheetName1")
     * .write(headData, data,"sheetName2")
     * .finish();
     *
     * @param outputStream
     */
    public static EasyExcelWriterFactory writeWithSheets(OutputStream outputStream) {
        return new EasyExcelWriterFactory(outputStream);
    }

    /**
     * 多个sheet页的数据链式写入
     * ExcelUtil.writeWithSheets(file)
     * .writeModel(ExcelModel.class, excelModelList, "sheetName1")
     * .write(headData, data,"sheetName2")
     * .finish();
     *
     * @param file
     */
    public static EasyExcelWriterFactory writeWithSheets(File file) {
        return new EasyExcelWriterFactory(file);
    }

    /**
     * 多个sheet页的数据链式写入
     * ExcelUtil.writeWithSheets(filePath)
     * .writeModel(ExcelModel.class, excelModelList, "sheetName1")
     * .write(headData, data,"sheetName2")
     * .finish();
     *
     * @param filePath
     */
    public static EasyExcelWriterFactory writeWithSheets(String filePath) {
        return new EasyExcelWriterFactory(filePath);
    }

}
