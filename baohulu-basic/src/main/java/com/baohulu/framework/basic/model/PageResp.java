package com.baohulu.framework.basic.model;

/**
 * @author heqing
 * @date 2022/11/01 17:00
 */

import lombok.Data;

import java.io.Serializable;

/**
 * 分页返回
 *
 * @author heqing
 * @date 2022/10/11 17:21
 */
@Data
public class PageResp<T extends Serializable> implements Serializable {

    /**
     * 当前页号
     */
    private Integer pageNo;

    /**
     * 每页记录数
     */
    private Integer pageSize;

    /**
     * 总页数
     */
    private Long pageTotal;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 实际记录
     */
    private T list;

    /**
     * 获取总页数
     * @return 总页数
     */
    public Long getPageTotal() {
        if (total == 0 || pageSize == 0) {
            return 0L;
        }
        if (total <= pageSize) {
            return 1L;
        }
        if (total % pageSize == 0) {
            return total / pageSize;
        } else {
            return total / pageSize + 1;
        }
    }
}