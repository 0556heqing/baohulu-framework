package com.baohulu.framework.common.utils.file.pdf.document;

import java.util.Map;

/**
 * @author heqing
 * @date 2022/11/15 15:52
 */
public interface DocumentVo {

    /**
     * 获取实体主键id
     *
     * @return 主键id
     */
    String findPrimaryKey();

    /**
     * 获取实体对象对应的map
     *
     * @return 主键id
     */
    Map<String, Object> fillDataMap();
}
