package com.baohulu.framework.common.utils;

import com.baohulu.framework.basic.consts.Network;
import com.baohulu.framework.basic.consts.Operation;
import com.baohulu.framework.basic.consts.Separators;
import com.baohulu.framework.basic.consts.Special;
import com.github.stuxuhai.jpinyin.ChineseHelper;

import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.baohulu.framework.common.utils.validate.ValidateUtils.*;

/**
 * 字符串工具类
 *
 * @author heqing
 * @date 2022/10/13 19:25
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    /**
     * 提取中文字符
     *
     * @param str
     * @return 中文字符组成的串
     */
    public static String extractChinese(String str) {
        StringBuffer sb = new StringBuffer();
        char[] ch = str.trim().toCharArray();

        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 将非数字、字母和中文的字符剔除
     * 符号只保留分割用的空格和数字中间的点号
     *
     * @param data 字符串
     * @return 由中文、英文字母、数字和空格组成的字符串
     */
    public static String replaceNotDigitAlphaChineseCharsWithSpace(String data) {
        if (null == data) {
            return "";
        }

        data = data.trim();
        if (data.isEmpty()) {
            return "";
        }

        char[] chars = data.toCharArray();
        StringBuilder sb = new StringBuilder();
        boolean preCharIsSpace = true;

        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];
            if (isDigital(ch) || isAlphabet(ch) || isChinese(ch)) {
                sb.append(ch);
                preCharIsSpace = false;
            } else {
                if (!preCharIsSpace) {
                    //数字中间的小数点不去掉
                    if (ch == Separators.CHAR_POINT
                            && i > 0 && i < chars.length - 1
                            && isDigital(chars[i - 1])
                            && isDigital(chars[i + 1])) {
                        sb.append(Separators.CHAR_POINT);
                    } else {
                        sb.append(Separators.SPACE);
                        preCharIsSpace = true;
                    }
                }
            }
        }

        if (0 == sb.length()) {
            return "";
        }
        return sb.toString().trim();
    }


    /**
     * 去除所有的非文字符号(除了",","."和空格保留)，并用replaceStr替代
     *
     * @param input
     * @param replaceStr
     * @return
     */
    public static String replaceNotWordWithStr(final String input, String replaceStr) {
        final StringBuilder builder = new StringBuilder();
        for (final char c : input.toCharArray()) {
            if (Character.isLetterOrDigit(c)) {
                builder.append(Character.isLowerCase(c) ? c : Character.toLowerCase(c));
            } else {
                if (c == Separators.CHAR_MIDDLE_LINE || c == Separators.CHAR_POINT || Character.isSpaceChar(c)) {
                    builder.append(c);
                } else {
                    builder.append(replaceStr);
                }
            }
        }

        String newStr = builder.toString();
        return newStr;
    }

    /**
     * 去除非中文、英文字母和数字的字符，返回剩余三者组成的字符串。
     *
     * @param data 字符串
     * @return 由中文、英文字母和数字的字符组成的字符串
     */
    public static String removeNotDigitAlphaChinese(String data) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(data)) {
            return "";
        }

        data = data.replaceAll(Separators.TAB, Separators.SPACE)
                .replaceAll(Separators.DOUBLE_SPACE, Separators.SPACE);
        data = data.trim();

        char[] chars = data.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];

            if (isDigital(ch) || isAlphabet(ch) || isChinese(ch)) {
                sb.append(ch);
            } else if (ch == Separators.CHAR_POINT) {
                //数字中间的小数点不去掉
                if (i > 0 && i < chars.length - 1 && isDigital(chars[i - 1]) && isDigital(chars[i + 1])) {
                    sb.append(Separators.CHAR_POINT);
                }
            } else if (ch == Separators.CHAR_SPACE) {
                //数字中间的小数点不去掉
                if (i > 0 && i < chars.length - 1 && isAlphabet(chars[i - 1]) && isAlphabet(chars[i + 1])) {
                    sb.append(Separators.SPACE);
                }
            }
        }

        if (0 == sb.length()) {
            return "";
        }
        return sb.toString();
    }

    /**
     * 删除字符串中的空白字符。
     *
     * @param data
     * @return
     */
    public static String removeSpace(String data) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(data)) {
            return "";
        }

        data = data.replaceAll("\\p{Space}", "");
        return data;
    }

    /**
     * 删除标点符号
     *
     * @param data
     * @return
     */
    public static String removePunctuation(String data) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(data)) {
            return "";
        }

        Pattern p = Pattern.compile(Special.REG_EX_PUNCTUATION);
        Matcher m = p.matcher(data);
        return m.replaceAll("").trim();
    }

    /**
     * 去掉标点符号，小数点保留。
     *
     * @param data 字符串
     * @return
     */
    public static String removePunctions(String data) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(data)) {
            return "";
        }

        data = data.trim();
        if (org.apache.commons.lang3.StringUtils.isEmpty(data)) {
            return "";
        }

        char[] chars = data.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];

            if (!isPunctuation(ch) && (ch != ' ')) {
                sb.append(ch);
            } else if (ch == '.') {
                //数字中间的小数点不去掉
                if (i > 0 && i < chars.length - 1 && isDigital(chars[i - 1]) && isDigital(chars[i + 1])) {
                    sb.append('.');
                }
            }
        }

        if (0 == sb.length()) {
            return "";
        }
        return sb.toString();
    }

    /**
     * 用空格替换标点符号，小数点保留。
     *
     * @param data 字符串
     * @return
     */
    public static String replacePunctionsWithSpace(String data) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(data)) {
            return "";
        }

        data = data.trim();
        if (org.apache.commons.lang3.StringUtils.isEmpty(data)) {
            return "";
        }

        char[] chars = data.toCharArray();
        StringBuilder sb = new StringBuilder();
        boolean preCharIsSpace = true;

        for (int i = 0; i < chars.length; i++) {
            char ch = chars[i];
            if (!isPunctuation(ch) && (ch != ' ')) {
                sb.append(ch);
                preCharIsSpace = false;
            } else {
                if (!preCharIsSpace) {
                    //数字中间的小数点不去掉
                    if (ch == '.' && i > 0 && i < chars.length - 1 && isDigital(chars[i - 1]) && isDigital(chars[i + 1])) {
                        sb.append('.');
                    } else {
                        sb.append(' ');
                        preCharIsSpace = true;
                    }
                }
            }
        }

        if (0 == sb.length()) {
            return "";
        }
        return sb.toString().trim();
    }

    /**
     * 判断是否仅包含中文、英文、和数字字符（可以包含标点符号，但不能仅含有标点符号）。
     *
     * @param str                字符串
     * @param mustContainChinese 是否必须包含中文
     * @return true | false
     */
    public static boolean isAllCdaString(String str, boolean mustContainChinese) {
        boolean containChinese = false;
        boolean containAlphabetDigital = false;

        if (str == null || str.isEmpty()) {
            return false;
        }

        char[] ch = str.trim().toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];

            if (isPunctuation(c) || isSpace(c)) {
                continue;
            }
            if (isDigital(c) || isAlphabet(c)) {
                containAlphabetDigital = true;
            } else if (!isChinese(c)) {
                return false;
            } else {
                containChinese = true;
            }
        }

        //如果必须包含中文，但没有包含中文，则返回false
        if (mustContainChinese && !containChinese) {
            return false;
        }
        return containAlphabetDigital || containChinese;
    }

    /**
     * 对空格进行删除，仅保留数字之间或字母之间的空格
     *
     * @param data 字符串
     * @return 删除空格之后的字符串
     */
    public static String removeSpaceNotWithinEnglishOrDigit(String data) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(data)) {
            return "";
        }

        data = data.trim();
        if (org.apache.commons.lang3.StringUtils.isEmpty(data)) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();
        char[] chars = data.toCharArray();
        stringBuilder.append(chars[0]);

        for (int i = 1; i < chars.length - 1; i++) {
            char pre = chars[i-1];
            char cur = chars[i];
            char next = chars[i+1];
            if (isSpace(cur)) {
                //仅保留数字之间或字母之间的空格
                boolean beKeep = (isDigital(pre) && isDigital(next)) || (isAlphabet(pre) && isAlphabet(next));
                if (beKeep) {
                    stringBuilder.append(cur);
                }
            } else {
                stringBuilder.append(cur);
            }
        }

        stringBuilder.append(chars[chars.length-1]);
        String dealt = stringBuilder.toString();
        if (!data.equals(dealt)) {
            System.out.println(String.format("修改， %s vs %s", data, dealt));
        }
        return dealt;
    }

    /**
     * 计算字符串的长度，连续的字母为一个单词，连续的数字为一个，单个汉字为一个，空格不算长度。
     *
     * @param query 字符串
     * @return 字符串长度
     */
    public static int lengthOf(String query) {
        if (null == query) {
            return 0;
        }
        if (query.length() <= 1) {
            return query.trim().length();
        }

        query += " ";
        char[] chars = query.toCharArray();
        int lenght = 0;
        for (int i = 0; i < chars.length - 1; i++) {
            if (isSpace(chars[i])) {
                continue;
            }

            if (isChinese(chars[i])) {
                lenght++;
            } else if (isDigital(chars[i]) && !isDigital(chars[i + 1])) {
                lenght++;
            } else if (isAlphabet(chars[i]) && !isAlphabet(chars[i + 1])) {
                lenght++;
            }
        }
        return lenght;
    }

    /**
     * 计算字符串的长度，连续的字母为一个单词，连续的数字为一个，单个汉字为一个，空格不算长度。
     *
     * @param query   字符串
     * @param limit   截断长度
     * @param forWord true：按单词长度截断 | false：按字符长度截断
     * @return 字符串长度
     */
    public static String cutoff(String query, int limit, boolean forWord) {
        if (null == query || limit <= 0) {
            return query;
        }

        query = query.trim();
        if (!forWord) {
            //截断query
            if (query.length() > limit) {
                query = query.substring(0, limit);
            }
        } else {
            query += " ";
            char[] chars = query.toCharArray();
            StringBuilder sb = new StringBuilder();

            int length = 0;
            for (int i = 0; i < chars.length - 1; i++) {
                if (length >= limit) {
                    break;
                }
                sb.append(chars[i]);
                if (isSpace(chars[i])) {
                    continue;
                }
                if (isChinese(chars[i])) {
                    length++;
                } else if (isDigital(chars[i]) && !isDigital(chars[i + 1])) {
                    length++;
                } else if (isAlphabet(chars[i]) && !isAlphabet(chars[i + 1])) {
                    length++;
                }
            }
            query = sb.toString().trim();
        }
        return query;
    }

    /**
     * 全角转半角
     *
     * @param inputStr 输入字符串
     * @return 半角字符串
     */
    public static String full2Half(String inputStr) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(inputStr)) {
            return inputStr;
        }

        //编码集
        String charsetName = "unicode";
        //全角字符的标志
        int fullSign = -1;
        //全角转半角的因子
        int fullHalfDiff = 32;

        StringBuffer outSb = new StringBuffer("");
        String tmpStr = "";

        byte[] b;
        for (int i = 0; i < inputStr.length(); i++) {
            tmpStr = inputStr.substring(i, i + 1);
            if (Separators.SPACE.equals(tmpStr)) {
                outSb.append(Separators.SPACE);
                continue;
            }
            try {
                b = tmpStr.getBytes(charsetName);
                if (b[2] == fullSign) {
                    //表示全角
                    b[3] = (byte) (b[3] + fullHalfDiff);
                    b[2] = 0;
                    outSb.append(new String(b, charsetName));
                } else {
                    outSb.append(tmpStr);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return outSb.toString();
    }

    /**
     * 繁体转简体
     * @param tradStr
     * @return
     */
    private static String traditionalToSimple(String tradStr) {
        String simplifiedStr = ChineseHelper.convertToSimplifiedChinese(tradStr);
        return simplifiedStr;
    }

    /**
     * 对字符串进行规则化，大写转小写，繁体转简体，全角转半角。如果replaceWithWhiteSpace指定为true，则非中文、英文字母和数字的字符串替换
     * 为空格，返回剩余三者和空格组成的字符串。如果出现连续多个空格，则仅保留一个。如果replaceWithWhiteSpace指定为false，则非中文、
     * 英文字母和数字的字符串被删除，返回剩余三者组成的字符串。不能以空格开头和结尾。
     * 1. 非数字、字母和中文的符号进行处理   剔除或空格替换
     * 2. 全角转半角
     * 3. 大写转小写
     * 4. 繁体转简体
     *
     * @param inputStr              字符串
     * @param replaceWithWhiteSpace 非中文、英文字母和数字的字符串替换为空格
     * @return 规则化后的字符串
     */
    public static String normalize(String inputStr, boolean replaceWithWhiteSpace) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(inputStr)) {
            return "";
        }

        inputStr = inputStr.trim();
        if (org.apache.commons.lang3.StringUtils.isEmpty(inputStr)) {
            return "";
        }

        if (replaceWithWhiteSpace) {
            // 由中文、英文字母、数字和空格组成的字符串
            inputStr = replaceNotDigitAlphaChineseCharsWithSpace(inputStr);
        } else {
            // 返回中文、英文字母和数字的字符组成的字符串
            inputStr = removeNotDigitAlphaChinese(inputStr);
        }

        if (org.apache.commons.lang3.StringUtils.isEmpty(inputStr)) {
            return "";
        }
        // 全角转半角
        inputStr = full2Half(inputStr);
        // 繁体转简体
        inputStr = traditionalToSimple(inputStr);
        // 大写转小写
        inputStr = inputStr.toLowerCase();
        return inputStr;
    }


    public static String normalServerString(String str) {
        String normalStr = str;
        normalStr = full2Half(normalStr);
        normalStr = traditionalToSimple(normalStr);
        normalStr = normalStr.toLowerCase();
        normalStr = replaceNotWordWithStr(normalStr, Separators.SPACE);
        return normalStr;
    }

    public static String normalTrainString(String str) {
        String normalStr = str;
        normalStr = full2Half(normalStr);
        normalStr = traditionalToSimple(normalStr);
        normalStr = normalStr.toLowerCase();
        normalStr = replaceNotWordWithStr(normalStr, Separators.TAB);
        //这条语句保证标点符号保留下来
        normalStr = normalStr.replaceAll("\t\t", Separators.TAB);
        //任何连续两个空白字符，都替换为空格
        normalStr = normalStr.replaceAll("[\\s]{2,}", Separators.SPACE);
        normalStr = normalStr.replaceAll("  ", Separators.SPACE);
        normalStr = normalStr.trim();
        return normalStr;
    }

    /**
     * 将map集合的键值对转化成：key1=value1&key2=value2 的形式
     * @param parameterMap 需要转化的键值对集合
     * @return 字符串
     */
    public static String mapToHttpString(Map<String, String> parameterMap) {
        StringBuilder parameterBuffer = new StringBuilder();
        if (parameterMap != null) {
            Iterator<String> iterator = parameterMap.keySet().iterator();
            String key;
            String value;
            while (iterator.hasNext()) {
                key = (String) iterator.next();
                if (parameterMap.get(key) != null) {
                    value = parameterMap.get(key);
                } else {
                    value = "";
                }
                parameterBuffer.append(key).append(Operation.EQUAL).append(value);
                if (iterator.hasNext()) {
                    parameterBuffer.append(Network.LINK);
                }
            }
        }
        return parameterBuffer.toString();
    }

    /**
     * 将url参数转换成map
     *
     * @param param aa=11&bb=22&cc=33
     * @return map数据
     */
    public static Map<String, String> httpStringToMap(String param) {
        Map<String, String> map = new HashMap<>(2);
        if (org.apache.commons.lang3.StringUtils.isBlank(param)) {
            return map;
        }
        String[] params = param.split(Network.LINK);
        for (String s : params) {
            String[] p = s.split(Operation.EQUAL);
            if (p.length == 2) {
                map.put(p[0], p[1]);
            }
        }
        return map;
    }

    /**
     * 将流中的数据转换为字符串
     * @param in 数据流
     * @return 流中的数据
     */
    public static String inputSteamToString(InputStream in){
        String str = "";
        try {
            //最好在将字节流转换为字符流的时候 进行转码
            BufferedReader bf = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            StringBuilder buffer = new StringBuilder();
            String line;
            while ((line = bf.readLine()) != null) {
                buffer.append(line);
            }
            str = buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 将字符转换为url连接中的字符
     * @param str 数据流
     */
    public static String urlEncode(String str){
        try {
            return URLEncoder.encode(str, Network.UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 将url连接中的字符换为正常字符转
     * @param str 数据流
     */
    public static String urlDecode(String str){
        try {
            return URLDecoder.decode(str, Network.UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 使用英文逗号将列表join到一起。
     *
     * @param list 字符串列表
     * @return 以英文逗号分隔列表元素的字符串
     */
    public static String listToString(List<String> list) {
        return listToString(list, Separators.COMMA, Integer.MAX_VALUE);
    }

    /**
     * 使用自定义分隔符将列表join到一起。
     *
     * @param list      字符串列表
     * @param separator 分隔符
     * @return 以英文逗号分隔列表元素的字符串
     */
    public static String listToString(List<String> list, String separator) {
        return listToString(list, separator, Integer.MAX_VALUE);
    }

    /**
     * 使用指定分隔符将列表join到一起。
     *
     * @param list      字符串列表
     * @param separator 分隔符
     * @param limit     长度限制
     * @return 以英文逗号分隔列表元素的字符串
     */
    public static String listToString(List<String> list, String separator, int limit) {
        StringBuilder sb = new StringBuilder();
        if (null == list || list.isEmpty()) {
            return "";
        }

        int size = list.size();
        int charLength = 0;
        int separatorCharSize = separator.toCharArray().length;
        for (int i = 0; i < size; i++) {
            int curElementCharLength = list.get(i).toCharArray().length;
            if (charLength + separatorCharSize + curElementCharLength > limit) {
                break;
            }
            if (i != 0) {
                sb.append(separator);
                charLength += separatorCharSize;
            }
            sb.append(list.get(i));
            charLength += curElementCharLength;
        }
        return sb.toString();
    }

    /**
     * 使用英文逗号分割字符串，并将trim后非空的元素放入返回结果集中
     *
     * @param value 字符串
     * @return 列表
     */
    public static List<String> stringToList(String value) {
        return stringToList(value, Separators.COMMA);
    }

    /**
     * 使用分隔符分割字符串，并将trim后非空的元素放入返回结果集中
     *
     * @param value     字符串
     * @param separator 分隔符
     * @return 列表
     */
    public static List<String> stringToList(String value, String separator) {
        List<String> elements = new ArrayList<>();
        if (null != value) {
            String[] split = value.split(separator);
            for (String str : split) {
                str = str.trim();
                if (!str.isEmpty()) {
                    elements.add(str);
                }
            }
        }
        return elements;
    }

    /**
     * Unicode转换成utf8
     *
     * @param theString 字符串
     * @return utf8字符串
     */
    public static String unicodeToString(String theString) {
        char aChar;
        int len = theString.length(), charLength = 4;
        StringBuilder outBuffer = new StringBuilder(len);
        for (int x = 0; x < len; ) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    int value = 0;
                    for (int i = 0; i < charLength; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException("Malformed encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't') {
                        aChar = '\t';
                    } else if (aChar == 'r') {
                        aChar = '\r';
                    } else if (aChar == 'n') {
                        aChar = '\n';
                    } else if (aChar == 'f') {
                        aChar = '\f';
                    }
                    outBuffer.append(aChar);
                }
            } else {
                outBuffer.append(aChar);
            }
        }
        return outBuffer.toString();
    }

    /**
     * 过滤emoji 或者 其他非文字类型的字符
     *
     * @param source 字符串
     * @return 过滤后的字符串
     */
    public static String filterEmoji(String source) {
        if (org.apache.commons.lang3.StringUtils.isBlank(source)) {
            return source;
        }
        StringBuilder buf = null;
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (isEmojiCharacter(codePoint)) {
                if (buf == null) {
                    buf = new StringBuilder(source.length());
                }
                buf.append(codePoint);
            }
        }
        if (buf == null) {
            return source;
        } else {
            if (buf.length() == len) {
                buf = null;
                return source;
            } else {
                return buf.toString();
            }
        }
    }

    /**
     * 字符中是否存在表情
     *
     * @param codePoint 字符串
     */
    private static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD)
                || ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD))
                || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }


    /**
     * 将字符串text中由openToken和closeToken组成的占位符依次替换为args数组中的值
     * @param openToken 前占位符
     * @param closeToken 后占位符
     * @param text 字符串内容
     * @param args 替换值
     * @return
     */
    public static String parse(String openToken, String closeToken, String text, Object... args) {
        if (args == null || args.length <= 0) {
            return text;
        }
        int argsIndex = 0;
        if (text == null || text.isEmpty()) {
            return "";
        }
        char[] src = text.toCharArray();
        int offset = 0;
        // search open token
        int start = text.indexOf(openToken, offset);
        if (start == -1) {
            return text;
        }
        final StringBuilder builder = new StringBuilder();
        StringBuilder expression = null;
        while (start > -1) {
            if (start > 0 && src[start - 1] == '\\') {
                // this open token is escaped. remove the backslash and continue.
                builder.append(src, offset, start - offset - 1).append(openToken);
                offset = start + openToken.length();
            } else {
                // found open token. let's search close token.
                if (expression == null) {
                    expression = new StringBuilder();
                } else {
                    expression.setLength(0);
                }
                builder.append(src, offset, start - offset);
                offset = start + openToken.length();
                int end = text.indexOf(closeToken, offset);
                while (end > -1) {
                    if (end > offset && src[end - 1] == '\\') {
                        // this close token is escaped. remove the backslash and continue.
                        expression.append(src, offset, end - offset - 1).append(closeToken);
                        offset = end + closeToken.length();
                        end = text.indexOf(closeToken, offset);
                    } else {
                        expression.append(src, offset, end - offset);
                        offset = end + closeToken.length();
                        break;
                    }
                }
                if (end == -1) {
                    // close token was not found.
                    builder.append(src, start, src.length - start);
                    offset = src.length;
                } else {
                    String value = (argsIndex <= args.length - 1) ?
                            (args[argsIndex] == null ? "" : args[argsIndex].toString()) : expression.toString();
                    builder.append(value);
                    offset = end + closeToken.length();
                    argsIndex++;

                }
            }
            start = text.indexOf(openToken, offset);
        }
        if (offset < src.length) {
            builder.append(src, offset, src.length - offset);
        }
        return builder.toString();
    }

    /**
     * 将字符串text中由openToken和closeToken组成的占位符依次替换为args数组中的值
     * @param text 字符串内容
     * @param args 替换值
     * @return
     */
    public static String parse(String text, Object... args) {
        return parse("{", "}", text, args);
    }

    /**
     * 获取第一个字符
     *
     * @param string
     * @return
     * @see
     */
    public static String firstChar(String string) {
        if(isEmpty(string)) {
            return "";
        }
        return (new StringBuilder().append(string.charAt(0))).toString();
    }

}
