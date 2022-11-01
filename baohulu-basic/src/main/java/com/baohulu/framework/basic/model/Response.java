package com.baohulu.framework.basic.model;

/**
 * @author heqing
 * @date 2022/11/01 17:01
 */

import com.baohulu.framework.basic.enums.ReturnEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * 接口返回实体类
 *
 * @author heqing
 * @date 2022/10/11 16:07
 */
@Data
public class Response<T extends Serializable> implements Serializable {

    /**
     * 响应码
     */
    private Integer code;

    /**
     * 响应消息
     */
    private String msg;

    /**
     * 响应数据
     */
    private T data;

    public Response(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 接口返回
     * @param code 编号
     * @param msg 成功提示语
     * @param data 返回信息
     * @return 响应数据
     */
    public static <T extends Serializable>  Response build(Integer code, String msg, T data) {
        return new Response(code, msg, data);
    }

    /**
     * 接口成功
     * @return 响应数据
     */
    public static Response success() {
        return build(ReturnEnum.SUCCESS.getCode(), ReturnEnum.SUCCESS.getMsg(), null);
    }

    /**
     * 接口成功
     * @param msg 成功提示语
     * @return 响应数据
     */
    public static <T extends Serializable> Response<T> success(String msg) {
        return build(ReturnEnum.SUCCESS.getCode(), msg, null);
    }

    /**
     * 接口成功
     * @param data 返回信息
     * @return 响应数据
     */
    public static <T extends Serializable> Response<T> success(T data) {
        return build(ReturnEnum.SUCCESS.getCode(), ReturnEnum.SUCCESS.getMsg(), data);
    }

    /**
     * 接口成功
     * @param msg 成功提示语
     * @param data 返回信息
     * @return 响应数据
     */
    public static <T extends Serializable> Response<T> success(String msg, T data) {
        return build(ReturnEnum.SUCCESS.getCode(), msg, data);
    }

    /**
     * 接口失败
     * @return 响应数据
     */
    public static Response error() {
        return build(ReturnEnum.ERROR.getCode(), ReturnEnum.ERROR.getMsg(), null);
    }

    /**
     * 接口失败
     * @param msg 失败提示语
     * @return 响应数据
     */
    public static Response error(String msg) {
        return build(ReturnEnum.ERROR.getCode(), msg, null);
    }

    /**
     * 接口失败
     * @param code 失败编号
     * @param msg 失败提示语
     * @return 响应数据
     */
    public static Response error(Integer code, String msg) {
        return build(code, msg, null);
    }

    /**
     * 接口是否成功
     * @return 是否成功
     */
    public boolean isSuccess() {
        return ReturnEnum.SUCCESS.getCode().intValue() == this.code.intValue();
    }
}
