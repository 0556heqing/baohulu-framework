package com.baohulu.framework.basic.enums;

import lombok.Getter;

/**
 * 月份枚举
 *
 * @author heqing
 * @date 2022/11/01 16:13
 */
@Getter
public enum MonthEnum {

    JANUARY(0, "一月"),
    FEBRUARY(1, "二月"),
    MARCH(2, "三月"),
    APRIL(3, "四月"),
    MAY(4, "五月"),
    JUNE(5, "六月"),
    JULY(6, "七月"),
    AUGUST(7, "八月"),
    SEPTEMBER(8, "九月"),
    OCTOBER(9, "十月"),
    NOVEMBER(10, "十一月"),
    DECEMBER(11, "十二月"),
    ;

    private final Integer code;
    private final String name;

    MonthEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getName(Integer code) {
        String name = "";
        switch(code) {
            case 0: name = JANUARY.name; break;
            case 1: name = FEBRUARY.name; break;
            case 2: name = MARCH.name; break;
            case 3: name = APRIL.name; break;
            case 4: name = MAY.name; break;
            case 5: name = JUNE.name; break;
            case 6: name = JULY.name; break;
            case 7: name = AUGUST.name; break;
            case 8: name = SEPTEMBER.name; break;
            case 9: name = OCTOBER.name; break;
            case 10: name = NOVEMBER.name; break;
            case 11: name = DECEMBER.name; break;
            default:
        }
        return name;
    }
}
