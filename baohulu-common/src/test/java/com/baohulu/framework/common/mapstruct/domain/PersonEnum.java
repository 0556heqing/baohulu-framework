package com.baohulu.framework.common.mapstruct.domain;

import lombok.Getter;

/**
 * @author heqing
 */
@Getter
public enum PersonEnum {

    /* 大人 */
    ADULT(0,  "大人"),
    /* 小孩 */
    CHILD(1, "小孩");

    private final Integer value;
    private final String desc;

    PersonEnum(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }
}
