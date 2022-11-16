package com.baohulu.framework.common.utils.file.word;

import com.baohulu.framework.common.utils.file.word.enums.ChartTypeEnum;
import lombok.Data;

import java.util.LinkedHashMap;

/**
 * word文档图形数据实体类
 *
 * @author heqing
 * @date 2022/11/10 10:50
 */
@Data
public class ChartData {

    /**
     * 图形标题
     */
    private String title;

    /**
     * x轴标题
     */
    private String titleX;

    /**
     * y轴标题
     */
    private String titleY;

    /**
     * 图表中 x，y 轴对应的值
     */
    private LinkedHashMap<String, Integer> values;

    /**
     * 图表类型
     */
    private ChartTypeEnum chartType;
}
