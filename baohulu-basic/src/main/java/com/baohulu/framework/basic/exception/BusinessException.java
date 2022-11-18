package com.baohulu.framework.basic.exception;

import com.baohulu.framework.basic.model.ReturnStatus;

/**
 * 业务运行时异常
 *
 * @author heqing
 * @date 2022/10/26 17:25
 */
public class BusinessException extends BaseException {

    private static final long serialVersionUID = 4432409120546865429L;

    public BusinessException(Integer code, String msg, Throwable cause) {
        super(code + ":" + msg, cause);
        this.code = code;
        this.msg = msg;
    }

    public BusinessException(Integer code, String msg) {
        super(code + ":" + msg);
        this.code = code;
        this.msg = msg;
    }

    public BusinessException(ReturnStatus returnStatus, Throwable cause) {
        super(returnStatus.getCode() + ":" + returnStatus.getMsg(), cause);
        this.code = returnStatus.getCode();
        this.msg = returnStatus.getMsg();
    }

    public BusinessException(ReturnStatus returnStatus) {
        super(returnStatus.getCode() + ":" + returnStatus.getMsg());
        this.code = returnStatus.getCode();
        this.msg = returnStatus.getMsg();
    }

    public BusinessException(ReturnStatus exceptionCodeType, String msg) {
        super(exceptionCodeType.getCode() + ":" + msg);
        this.code = exceptionCodeType.getCode();
        this.msg = msg;
    }

}
