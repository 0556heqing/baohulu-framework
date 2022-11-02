package com.baohulu.framework.common.utils.validate.enums;

/**
 * @author heqing
 * @date 2022/10/27 14:03
 */
public enum RegexTypeEnum {

    // 无格式
    NONE,

    // 必须为数字
    NUMBER,

    // 必须为身份证
    IDENTITY_CARD,

    // 必须为电话号码
    PHONE_NUMBER,

    // 必须为邮件地址格式
    EMAIL,

    // 必须为网络地址格式
    IP,

    // 必须为日期
    DATE,

    // 只能是有中文字符
    CHINESE,

    // 不能含有特殊字符
    NO_SPECIAL_CHAR,

    // 不能含有特殊字符
    NO_EMOJI;

}
