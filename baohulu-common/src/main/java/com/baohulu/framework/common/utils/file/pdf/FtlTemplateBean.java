package com.baohulu.framework.common.utils.file.pdf;

import lombok.Data;

import java.io.Serializable;

/**
 * 模板相关
 *
 * @author heqing
 * @date 2022/09/19 10:56
 */
@Data
public class FtlTemplateBean implements Serializable {

    private String ftlTemplatePath;

    private String fontsPath;

    private String ftlTemplateName;

}