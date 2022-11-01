package com.baohulu.framework.basic.consts;

import java.util.HashSet;

/**
 * 特殊字符
 *
 * @author heqing
 * @date 2022/11/01 16:46
 */
public class Special {

    public final static String PUNCTUATIONS = "[\"`~!@#$%^&*()+=|{}':;',.·<>/?~！@#￥%……&*（）_《》——+|{}【】‘；：”“’。，、？\\-]";
    public final static String REG_EX_PUNCTUATION = "[\"`~!@#$%^&*()+=|{}':;',\\[\\].·<>/?~！@#￥%……&*（）_《》——+|{}【】‘；：”“’。，、？\\\\-]";

    public final static HashSet<Character> PUNCTUATION_SET = new HashSet<>();
    static {
        char[] chars = PUNCTUATIONS.toCharArray();

        for (char c : chars) {
            PUNCTUATION_SET.add(c);
        }
    }
}
