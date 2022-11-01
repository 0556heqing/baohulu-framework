package com.baohulu.framework.basic.exception;

import com.baohulu.framework.basic.model.ReturnStatus;

/**
 * 系统级异常
 *
 * @author heqing
 * @date 2022/11/01 17:17
 */
public class SysException extends BaseException {

    private static final long serialVersionUID = 3116483353040779859L;

    public SysException(Integer code, String msg, Throwable cause) {
        super(code + ":" + msg, cause);
        this.code = code;
        this.msg = msg;
    }

    public SysException(Integer code, String msg) {
        super(code + ":" + msg);
        this.code = code;
        this.msg = msg;
    }

    public SysException(ReturnStatus returnStatus, Throwable cause) {
        super(returnStatus.getCode() + ":" + returnStatus.getMsg(), cause);
        this.code = returnStatus.getCode();
        this.msg = returnStatus.getMsg();
    }

    public SysException(ReturnStatus returnStatus) {
        super(returnStatus.getCode() + ":" + returnStatus.getMsg());
        this.code = returnStatus.getCode();
        this.msg = returnStatus.getMsg();
    }

    public SysException(ReturnStatus exceptionCodeType, String msg) {
        super(exceptionCodeType.getCode() + ":" + msg);
        this.code = exceptionCodeType.getCode();
        this.msg = msg;
    }
}
