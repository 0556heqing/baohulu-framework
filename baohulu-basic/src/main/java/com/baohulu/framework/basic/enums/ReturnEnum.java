package com.baohulu.framework.basic.enums;

import com.baohulu.framework.basic.model.ReturnStatus;
import lombok.Getter;

/**
 * 接口成功码与提示语
 * @author heqing
 * @date 2022/10/11 16:17
 */
@Getter
public enum ReturnEnum implements ReturnStatus {

    //接口内部错误
    ERROR(-1,"系统繁忙，请稍候再试"),
    //接口访问成功
    SUCCESS(0,"SUCCESS"),

    SYSTEM_EXCEPTION(1000, "系统异常"),
    PARAM_ERROR(2000, "参数异常"),
    BUSINESS_EXCEPTION(3000, "业务异常"),
    ;

    /**
     * 响应码
     */
    private final Integer code;

    /**
     * 响应消息
     */
    private final String msg;

    ReturnEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
