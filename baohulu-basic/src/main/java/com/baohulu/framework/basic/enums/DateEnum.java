package com.baohulu.framework.basic.enums;

import lombok.Getter;

/**
 * 时间类型枚举
 *
 * @author heqing
 * @date 2022/11/01 16:15
 */
@Getter
public enum DateEnum {

    // 年
    YEAR(1),
    // 月
    MONTH(2),
    // 周
    WEEK(3),
    // 日
    DAYS(4),
    // 时
    HOUR(5),
    // 分
    MINUTE(6),
    // 秒
    SECOND(7),
    ;

    /**
     * 编码
     */
    private final Integer code;

    DateEnum(Integer code) {
        this.code = code;
    }
}
