package com.baohulu.framework.common.utils.validate;

import com.baohulu.framework.basic.consts.Separators;
import com.baohulu.framework.basic.consts.Special;
import org.apache.commons.lang.time.DateUtils;

import java.text.ParseException;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * 入参工具检验类
 *
 * @author heqing
 * @date 2022/10/26 17:19
 */
public class ValidateUtils {

    private final static Pattern NUMBER_PATTERN = Pattern.compile("[0-9]+(.[0-9]+)?");
    private final static Pattern ALPHABET_PATTERN =  Pattern.compile("[a-zA-Z]+");

    public static boolean matches(String str, String regex) {
        if (null != str && !"".equals(str)) {
            return str.matches(regex);
        } else {
            return false;
        }
    }

    /**
     * 判断是否是手机号
     * @param phoneNumber 手机号
     * @return 是否是手机号
     */
    public static boolean isPhoneNumber(String phoneNumber) {
        String regex = "^1[3-9]\\d{9}$";
        return matches(phoneNumber, regex);
    }

    /**
     * 判断是否是电子邮件
     * @param email 电子邮件
     * @return 是否是电子邮件
     */
    public static boolean isEmail(String email) {
        String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        return matches(email, regex);
    }

    /**
     * 判断是否是网络链接
     * @param url 网络链接
     * @return 是否是网络链接
     */
    public static boolean isUrl(String url) {
        String regex = "^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+$";
        return matches(url, regex);
    }

    /**
     * 判断是否是网络ip地址
     * @param ip 网络ip地址
     * @return 是否是网络ip地址
     */
    public static boolean isIp(String ip) {
        String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
        return matches(ip, regex);
    }

    /**
     * 是否是身份证号码
     * @param cardNum 身份证号码
     * @return 是否是身份证号码
     */
    public static boolean isIdentityCard(String cardNum) {
        String regex = "(\\d{17}[0-9xX])|(\\d{15})";
        return matches(cardNum, regex);
    }

    private static String[] parsePatterns = {"yyyy-MM-dd","yyyy年MM月dd日","yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
            "yyyy/MM/dd","yyyy/MM/dd HH:mm:ss","yyyy/MM/dd HH:mm", "yyyyMMdd"};

    /**
     * 是否为日期
     * @param date 日期
     * @return 是否为日期
     */
    public static boolean isDate(String date) {
        if (null != date && !"".equals(date)) {
            try {
                Date d = DateUtils.parseDate(date, parsePatterns);
                return d != null;
            } catch (ParseException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    private static boolean isChineseByChar(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION;
    }

    /**
     * 是否都是汉字
     * @param strName 字符串
     * @return 是否是汉字
     */
    public static boolean isChinese(String strName) {
        boolean isChinese = true;
        char[] ch = strName.toCharArray();
        for (char c : ch) {
            if (!isChineseByChar(c)) {
                isChinese = false;
                break;
            }
        }
        return isChinese;
    }

    /**
     * 是否存在特殊字符
     * @param text 文本
     * @return 是否存在特殊字符
     */
    public static boolean isSpecialChar(String text) {
        boolean isSpecial = false;
        String[] ch = text.split("");
        for (String s : ch) {
            if (matches(s, Special.PUNCTUATIONS)) {
                isSpecial = true;
                break;
            }
        }
        return isSpecial;
    }

    /**
     * 判断字符是否是标点符号
     *
     * @param c 字母
     * @return 是否是标点符号
     */
    public static boolean isPunctuation(char c) {
        return Special.PUNCTUATION_SET.contains(c);
    }

    /**
     * 判断字符是否是空白字符
     *
     * @param c 字母
     * @return 是否是空白字符
     */
    public static boolean isSpace(char c) {
        return Character.isWhitespace(c);
    }

    /**
     * 是否是中文字符（汉字Unicode编码的区间为：0x4E00→0x9FA5(转)）
     * 参考：https://www.cnblogs.com/chenwenbiao/archive/2011/08/17/2142718.html
     *
     * @param c 字符
     * @return
     */
    public static boolean isChinese(char c) {
        int code = (int) c;
        int chineseUnicodeStart = 0x4E00, chineseUnicodeEnd = 0x9FA5;

        boolean beChinese = (code >= chineseUnicodeStart && code <= chineseUnicodeEnd) && Character.isLetter(c);
        if (beChinese) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符是否是数字
     *
     * @param c 字符
     * @return 是否是数字
     */
    public static boolean isDigital(char c) {
        return Character.isDigit(c);
    }

    /**
     * 判断是否是数字字符串。
     *
     * @param str 字符串
     * @return true 是 | false 否
     */
    public static boolean isNumberString(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }

        return NUMBER_PATTERN.matcher(str).matches();
    }

    /**
     * 判断字符是否是英文字母
     *
     * @param c 字符
     * @return 是否是英文字母
     */
    public static boolean isAlphabet(char c) {
        int code = (int) c;
        //小写英文字母，半角
        int lowerEnglishHalfStart = 97, lowerEnglishHalfEnd = 122;
        //大写英文字母，半角
        int upperEnglishHalfStart = 65, upperEnglishHalfEnd = 90;
        //小写英文字母，全角
        int lowerEnglishFullStart = 65345, lowerEnglishFullEnd = 65370;
        //大写英文字母，全角
        int upperEnglishFullStart = 65313, upperEnglishFullEnd = 65338;

        boolean beAlphabet = (code >= lowerEnglishHalfStart && code <= lowerEnglishHalfEnd) ||
                (code >= upperEnglishHalfStart && code <= upperEnglishHalfEnd) ||
                (code >= lowerEnglishFullStart && code <= lowerEnglishFullEnd) ||
                (code >= upperEnglishFullStart && code <= upperEnglishFullEnd);
        return beAlphabet;
    }

    /**
     * 判断是否是英文字母字符串。
     *
     * @param str 字符串
     * @return true 是 | false 否
     */
    public static boolean isAlphabetString(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        return ALPHABET_PATTERN.matcher(str).matches();
    }

    /**
     * 是否包含中文字符
     *
     * @param strName 字符串
     * @return 是否包含中文字符
     */
    public static boolean hasChinese(String strName) {
        char[] ch = strName.trim().toCharArray();

        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否全由中文字符组成
     *
     * @param strName 字符串
     * @return 是否全由中文字符组成
     */
    public static boolean isAllChinese(String strName) {
        boolean containChinese = false;
        char[] ch = strName.trim().toCharArray();

        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isPunctuation(c)) {
                continue;
            }
            if (!isChinese(c)) {
                return false;
            } else {
                containChinese = true;
            }
        }
        return containChinese;
    }

    /**
     * 判断字符串是否包含 emoji 或者 其他非文字类型的字符
     * @param source
     * @return
     */
    public static boolean isEmoji(String source) {
        int len = source.length();
        boolean isEmoji = false;
        for (int i = 0; i < len; i++) {
            char hs = source.charAt(i);
            if (0xd800 <= hs && hs <= 0xdbff) {
                if (source.length() > 1) {
                    char ls = source.charAt(i + 1);
                    int uc = ((hs - 0xd800) * 0x400) + (ls - 0xdc00) + 0x10000;
                    if (0x1d000 <= uc && uc <= 0x1f77f) {
                        return true;
                    }
                }
            } else {
                // non surrogate
                if (0x2100 <= hs && hs <= 0x27ff && hs != 0x263b) {
                    return true;
                } else if (0x2B05 <= hs && hs <= 0x2b07) {
                    return true;
                } else if (0x2934 <= hs && hs <= 0x2935) {
                    return true;
                } else if (0x3297 <= hs && hs <= 0x3299) {
                    return true;
                } else if (hs == 0xa9 || hs == 0xae || hs == 0x303d || hs == 0x3030 || hs == 0x2b55 || hs == 0x2b1c
                        || hs == 0x2b1b || hs == 0x2b50 || hs == 0x231a) {
                    return true;
                }
                if (!isEmoji && source.length() > 1 && i < source.length() - 1) {
                    char ls = source.charAt(i + 1);
                    if (ls == 0x20e3) {
                        return true;
                    }
                }
            }
        }
        return isEmoji;
    }

}