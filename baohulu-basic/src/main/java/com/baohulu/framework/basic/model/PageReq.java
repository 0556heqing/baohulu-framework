package com.baohulu.framework.basic.model;

/**
 * @author heqing
 * @date 2022/11/01 16:57
 */

import lombok.Setter;

import java.io.Serializable;

/**
 * 分页请求
 *
 * @author heqing
 * @date 2022/10/11 17:16
 */
@Setter
public class PageReq implements Serializable {

    /**
     * 默认页码
     */
    private static final int DEFAULT_PAGE_NO = 1;

    /**
     * 默认单页记录数
     */
    private static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * 分页，第几页
     */
    private Integer pageNo;

    /**
     * 分页，每页数量
     */
    private Integer pageSize;

    public Integer getPageNo() {
        if(pageNo == null || pageNo < 1) {
            pageNo = DEFAULT_PAGE_NO;
        }
        return pageNo;
    }

    public Integer getPageSize() {
        if(pageSize == null || pageSize < 1) {
            pageSize = DEFAULT_PAGE_SIZE;
        }
        return pageSize;
    }
}