package com.baohulu.framework.basic.model;

/**
 * 返回状态
 *
 * @author heqing
 * @date 2022/10/11 17:02
 */
public interface ReturnStatus {

    /**
     * 获取状态码
     *
     * @return 状态码
     */
    Integer getCode();

    /**
     * 获取状态描述
     *
     * @return 状态描述
     */
    String getMsg();

}
