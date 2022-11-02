package com.baohulu.framework.common;

import com.baohulu.framework.common.utils.StringUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author heqing
 * @date 2022/10/18 15:57
 */
public class TestString {

    @Test
    public void mapString() {
        Map<String, String> map = new HashMap<>(2);
        map.put("name", "贺小白");
        map.put("address", "246512");

        String str = StringUtils.mapToHttpString(map);
        System.out.println("--> " + str);

        Map<String, String> newMap = StringUtils.httpStringToMap(str);
        System.out.println("--> " + newMap);
    }

    @Test
    public void url() {
        String str = "name=贺小白&address=246512";

        String newStr = StringUtils.urlEncode(str);
        System.out.println("--> " + newStr);

        str = StringUtils.urlDecode(newStr);
        System.out.println("--> " + str);
    }

    @Test
    public void list() {
        String str = "贺小白,246512";

        List<String> list = StringUtils.stringToList(str);
        System.out.println("list --> " + list);

        str = StringUtils.listToString(list);
        System.out.println("str --> " + str);
    }

    @Test
    public void unicodeToString() {
        String unicode = "# \\u53C2\\u8003";
        String str = StringUtils.unicodeToString(unicode);
        System.out.println("--> " + str);
    }

    @Test
    public void filterEmoji() {
        String source = "你好😀！";
        String str = StringUtils.filterEmoji(source);
        System.out.println("--> " + str);
    }

    @Test
    public void parse() {
        String source = "第{}条{}，出现{}";
        String str = StringUtils.parse(source, 1, "数据", "解析错误!");
        System.out.println("--> " + str);
    }

    @Test
    public void handle() {
        String query = "HeQing，你還欠我 ^ 50.2元錢!";

        String result = StringUtils.extractChinese(query);
        System.out.println("提取中文 -->" + result);

        result = StringUtils.removeNotDigitAlphaChinese(query);
        System.out.println("剔除非(数字、字母和中文) -->" + result);

        result = StringUtils.replaceNotDigitAlphaChineseCharsWithSpace(query);
        System.out.println("剔除非(数字、字母和中文)，加空格 -->" + result);

        result = StringUtils.replaceNotWordWithStr(query, " ");
        System.out.println("符号用指定符号替换 -->" + result);

        result = StringUtils.removeSpace(query);
        System.out.println("删除空格 -->" + result);

        result = StringUtils.removePunctuation(query);
        System.out.println("删除符号 -->" + result);

        result = StringUtils.removePunctions(query);
        System.out.println("删除符号，保留小数点 -->" + result);

        result = StringUtils.replacePunctionsWithSpace(query);
        System.out.println("空格替换符号，保留小数点 -->" + result);

        result = StringUtils.removeSpaceNotWithinEnglishOrDigit(query);
        System.out.println("删除空格，保留小数点 -->" + result);

        int length = StringUtils.lengthOf(query);
        System.out.println("字符串长度 -->" + length);

        result = StringUtils.cutoff(query, 7, true);
        System.out.println("截断字符串 -->" + result);
    }

    /**
     * 进入搜索模块的query的最大长度，如果超过该长度，则进行截断
     */
    private static final int MAXLENGTH_QUERY = 50;

    @Test
    public void normalizeQuery() {
        String query = "HeQing，你還欠我^50.2元錢!";

        //繁体转简体，全角转半角，大写转小写，去掉符号
        String normalized = StringUtils.normalize(query, true);
        System.out.println("-->" + normalized);

        normalized = StringUtils.normalServerString(query);
        System.out.println("-->" + normalized);

        normalized = StringUtils.normalTrainString(query);
        System.out.println("-->" + normalized);

        //截断query，最长10个单词，一个中文字符为一个单词，一个数字串为一个单词，一个英文字符串为一个单词
        normalized = StringUtils.cutoff(normalized, MAXLENGTH_QUERY, true);

        System.out.println("-->" + normalized);
    }

    @Test
    public void firstChar() {
        String name = "贺小白";
        System.out.println("--> " + StringUtils.firstChar(name));
    }
}
