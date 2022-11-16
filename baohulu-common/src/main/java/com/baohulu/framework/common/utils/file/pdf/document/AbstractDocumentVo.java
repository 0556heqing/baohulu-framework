package com.baohulu.framework.common.utils.file.pdf.document;

import com.baohulu.framework.common.utils.BeanUtils;

import java.util.Map;

/**
 * @author heqing
 * @date 2022/11/15 15:53
 */
public abstract class AbstractDocumentVo implements DocumentVo {

    public AbstractDocumentVo() {
    }

    @Override
    public Map<String, Object> fillDataMap() {
        DocumentVo vo = this.getDocumentVo();
        Map<String, Object> map = BeanUtils.beanToMap(vo);
        return map;
    }

    private DocumentVo getDocumentVo() {
        return this;
    }

}
