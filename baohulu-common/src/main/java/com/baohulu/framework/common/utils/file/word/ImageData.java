package com.baohulu.framework.common.utils.file.word;

import lombok.Data;

/**
 * @author heqing
 * @date 2022/11/10 16:10
 */
@Data
public class ImageData {

    /**
     * 图片名
     */
    private String name;

    /**
     * 图片地址
     */
    private String path;

    /**
     * 宽
     */
    private Integer width;

    /**
     * 高
     */
    private Integer height;
}
