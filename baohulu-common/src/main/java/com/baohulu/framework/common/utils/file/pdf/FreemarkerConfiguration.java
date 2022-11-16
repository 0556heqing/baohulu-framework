package com.baohulu.framework.common.utils.file.pdf;

import freemarker.template.Configuration;
import java.io.File;
import java.io.IOException;

/**
 * 单例模式获取配置
 *
 * @author heqing
 * @date 2022/09/19 11:00
 */
public class FreemarkerConfiguration {

    private static Configuration config = null;

    public FreemarkerConfiguration() {
    }

    public static synchronized Configuration getConfiguration(String ftlTemplatePath) {
        if (config == null) {
            setConfiguration(ftlTemplatePath);
        }
        return config;
    }

    private static void setConfiguration(String ftlTemplatePath) {
        config = new Configuration();
        try {
            config.setDefaultEncoding("UTF-8");
            config.setDirectoryForTemplateLoading(new File(ftlTemplatePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
