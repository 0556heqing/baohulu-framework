package com.baohulu.framework.common.utils.file.excel;

import lombok.Data;

import java.util.List;

/**
 * @author heqing
 * @date 2022/11/08 17:14
 */
@Data
public class SheetData {

    /**
     * 表单名
     */
    private String sheetName;

    /**
     * 表单类型
     */
    private Class<?> clazz;

    /**
     * 表单数据
     */
    private List<?> data;

}
