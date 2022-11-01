package com.baohulu.framework.basic.enums;

import lombok.Getter;

/**
 * 性别枚举
 *
 * @author heqing
 * @date 2022/11/01 16:09
 */
@Getter
public enum SexEnum {

    MALE(1, "男"),
    FEMALE(2, "女"),
    UNKNOWN(3, "未知"),
    ;

    private final Integer code;
    private final String name;

    SexEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}