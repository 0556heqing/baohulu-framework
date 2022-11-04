package com.baohulu.framework.common.utils.file.enums;

/**
 * 图片合法格式
 *
 * @author heqing
 * @date 2022/11/02 13:46
 */
public enum ImageTypeEnum {
    /**
     * 图片审核合法格式
     */
    JPEG("image/jpeg","jpg"),
    PNG("image/png","png"),
    BMP("image/bmp","bmp"),
    ;

    private String type;
    private String name;

    ImageTypeEnum(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public static ImageTypeEnum getByType(String contentType) {
        for (ImageTypeEnum value : values()) {
            if (value.type.equals(contentType)){
                return value;
            }
        }
        return null;
    }

    public static boolean isLegalType(String type){
        for (ImageTypeEnum value : values()) {
            if (value.type.equals(type)){
                return true;
            }
        }
        return false;
    }

}
