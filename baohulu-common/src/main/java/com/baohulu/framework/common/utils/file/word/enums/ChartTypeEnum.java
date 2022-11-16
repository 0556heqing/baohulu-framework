package com.baohulu.framework.common.utils.file.word.enums;

import lombok.Getter;

/**
 * word文档图表基本类型
 *
 * @author heqing
 * @date 2022/11/10 10:41
 */
@Getter
public enum ChartTypeEnum {

    // 性别枚举
    LINE(1, "线性图"),
    BAR(2, "条形图"),
    PIE(3, "饼行图"),
    ;

    private final Integer code;
    private final String name;

    ChartTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }
}
