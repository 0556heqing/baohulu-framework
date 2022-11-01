package com.baohulu.framework.basic.enums;

/**
 * 真假 / 是否
 *
 * @author heqing
 * @date 2022/11/01 16:16
 */
public enum YesOrNoEnum {

    /**
     * 0:否 1:是
     */
    NO(0),
    YES(1);

    private Integer code;

     YesOrNoEnum(Integer code) {
        this.code = code;
    }
}
