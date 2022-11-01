package com.baohulu.framework.basic.enums;

import lombok.Getter;

/**
 * 星期枚举
 *
 * @author heqing
 * @date 2022/11/01 16:10
 */
@Getter
public enum WeekEnum {

    SUNDAY(1, "星期日"),
    MONDAY(2, "星期一"),
    TUESDAY(3, "星期二"),
    WEDNESDAY(4, "星期三"),
    THURSDAY(5, "星期四"),
    FRIDAY(6, "星期五"),
    SATURDAY(7, "星期六")
    ;

    private final Integer code;
    private final String name;

    WeekEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static String getName(Integer code) {
        String name = "";
        switch(code) {
            case 1: name = SUNDAY.name; break;
            case 2: name = MONDAY.name; break;
            case 3: name = TUESDAY.name; break;
            case 4: name = WEDNESDAY.name; break;
            case 5: name = THURSDAY.name; break;
            case 6: name = FRIDAY.name; break;
            case 7: name = SATURDAY.name; break;
            default:
        }
        return name;
    }
}
