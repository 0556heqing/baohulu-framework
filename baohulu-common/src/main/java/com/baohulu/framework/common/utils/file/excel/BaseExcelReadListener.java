package com.baohulu.framework.common.utils.file.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * excel读取监听器
 *
 * @author heqing
 * @date 2022/11/04 16:39
 */
@Slf4j
public abstract class BaseExcelReadListener<T> extends AnalysisEventListener<T> {

    /**
     * 读取到的excel行数
     */
    protected int rowCount = 0;
    /**
     * 读取到的excel行数据列表
     */
    protected final List<T> rows = new ArrayList<>();

    /**
     * 读取excel数据前操作(只有不读取表头数据时才会触发此方法)
     */
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        log.info("======================================================");
        log.info("解析第一行数据:{}" + JSON.toJSONString(headMap));
        log.info("======================================================");
    }

    /**
     * 每读取完一行数据调用
     *
     * @param object
     * @param context
     */
    @Override
    public void invoke(T object, AnalysisContext context) {
        rowCount++;
        rows.add(object);

        // 获取每次最多处理多少行
        int maxRowSize = getMaxRowSize();
        if (maxRowSize > 0 && rows.size() >= maxRowSize) {
            processRows(rows);
            // 清理现存数据
            rows.clear();
        }
    }

    /**
     * 读取完excel数据后的操作
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        processRows(rows);
        // 清理现存数据
        rows.clear();
        log.info("成功读取【" + rowCount + "】条数据");
    }

    /**
     * @return 返回读取excel总数据
     */
    public List<T> getRows() {
        return rows;
    }

    /**
     * 处理现存行数据
     *
     * @param rows
     */
    protected abstract void processRows(List<T> rows);

    /**
     * 获取单次处理最大的长度
     * 默认值: -1 表示不做限制
     *
     * @return
     */
    protected int getMaxRowSize() {
        return -1;
    }
}
