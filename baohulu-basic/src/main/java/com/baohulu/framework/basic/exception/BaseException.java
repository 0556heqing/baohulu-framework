package com.baohulu.framework.basic.exception;

/**
 * 基础应用级异常类。禁止直接使用该异常，请以应用名称创建对应的异常类
 *
 * @author heqing
 * @date 2022/10/11 18:55
 */

import com.baohulu.framework.basic.enums.ReturnEnum;
import lombok.Data;

@Data
public abstract class BaseException extends RuntimeException {

    /**
     * 错误代码
     */
    protected Integer code;

    /**
     * 错误信息
     */
    protected String msg;

    public BaseException() {
        super();
    }

    public BaseException(String msg, Throwable cause) {
        super(cause);
        this.msg = msg;
        this.code = ReturnEnum.ERROR.getCode();
    }

    public BaseException(Throwable cause) {
        super(cause);
        if (cause instanceof BaseException) {
            BaseException commonException = (BaseException) cause;
            this.code = commonException.getCode();
            this.msg = commonException.getMessage();
        } else {
            this.code = ReturnEnum.ERROR.getCode();
        }
    }

    public BaseException(String msg) {
        super(msg);
        this.code = ReturnEnum.ERROR.getCode();
        this.msg = msg;
    }

    public BaseException(Integer code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public BaseException(ReturnEnum returnCode) {
        super(returnCode.getMsg());
        this.code = returnCode.getCode();
        this.msg = returnCode.getMsg();
    }
}
