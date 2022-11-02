package com.baohulu.framework.common.utils.chinese.enums;

/**
 * 支持5种词典
 * 注意：词典各字段之间使用tab(\t)分割，而不是空格。
 *
 * @author heqing
 * @date 2022/10/28 16:09
 */
public enum LibraryEnum {

    /**
     *  用户自定义词典,格式 词语 词性 词频
     *      重要	a	37557
     */
    DIC_LIBRARY,

    /**
     * 停用词词典,格式 词语 停用词类型[可以为空]
     *      is
     *      .*了	regex
     */
    STOP_LIBRARY,

    /**
     * crf模型,格式 二进制格式
     */
    CRF_LIBRARY,

    /**
     * 歧义词典, 格式:'词语0 词性0 词语1 词性1.....'
     *      习近平	nr
     *      动漫	n	游戏	n
     */
    AMBIGUITY_LIBRARY,

    /**
     * 同义词词典,格式'词语0 词语1 词语2 词语3',采用幂等策略,w1=w2 w2=w3 w1=w3
     *      每天	天天	每一天
     */
    SYNONYMS_LIBRARY,
    ;
}
