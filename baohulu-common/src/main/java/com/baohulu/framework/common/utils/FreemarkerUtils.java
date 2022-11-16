package com.baohulu.framework.common.utils;

import com.baohulu.framework.common.utils.file.pdf.FreemarkerConfiguration;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * @author heqing
 * @date 2022/11/15 19:38
 */
public class FreemarkerUtils {

    /**
     * 将数据填充入模板，然后返回模板内容
     *
     * @param ftlTemplatePath 模板地址
     * @param ftlTemplateName 模板名
     * @param variables 填充数据
     * @return
     */
    public static String generate(String ftlTemplatePath, String ftlTemplateName, Map<String, Object> variables) throws IOException, TemplateException {
        String htmlContent = "";

        BufferedWriter writer = null;
        try {
            Configuration config = FreemarkerConfiguration.getConfiguration(ftlTemplatePath);
            Template tp = config.getTemplate(ftlTemplateName, "UTF-8");
            StringWriter stringWriter = new StringWriter();
            writer = new BufferedWriter(stringWriter);
            tp.process(variables, writer);
            htmlContent = stringWriter.toString();
            writer.flush();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return htmlContent;
    }
}
