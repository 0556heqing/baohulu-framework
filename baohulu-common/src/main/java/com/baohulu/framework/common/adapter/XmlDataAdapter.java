package com.baohulu.framework.common.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * xml 文件转java属性的适配方法
 *
 * @author heqing
 * @date 2022/10/19 14:10
 */
public class XmlDataAdapter extends XmlAdapter<String, String> {

    @Override
    public String marshal(String str) {
        //从javabean到xml的适配方法
        // CDATA 指不由 XML 解析器进行解析的文本数据
        return "<![CDATA[" + str+ "]]>";
    }

    @Override
    public String unmarshal(String str) {
        //从xml到javabean的适配方法
        return str;
    }
}