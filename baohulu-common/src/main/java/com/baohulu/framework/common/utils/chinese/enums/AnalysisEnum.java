package com.baohulu.framework.common.utils.chinese.enums;

/**
 * 分词方式
 * 名称	        用户自定义词典   数字识别  人名识别    机构名识别   新词发现
 * BaseAnalysis	    X	        X	    X	        X	        X
 * ToAnalysis	    √	        √	    √	        X	        X
 * DicAnalysis	    √	        √	    √	        X	        X
 * IndexAnalysis	√	        √	    √	        X	        X
 * NlpAnalysis	    √	        √	    √	        √	        √
 *
 * @author heqing
 * @date 2022/10/28 15:40
 */
public enum AnalysisEnum {

    /**
     * BaseAnalysis 最小颗粒度的分词
     * 基本就是保证了最基本的分词.词语颗粒度最非常小的..所涉及到的词大约是10万左右.
     * 基本分词速度非常快.在macAir上.能到每秒300w字每秒.同时准确率也很高.但是对于新词他的功能十分有限.
     */
    BASE_ANALYSIS,

    /**
     * ToAnalysis 精准分词
     * 精准分词是Ansj分词的店长推荐款
     * 它在易用性,稳定性.准确性.以及分词效率上.都取得了一个不错的平衡.如果你初次尝试Ansj如果你想开箱即用.那么就用这个分词方式是不会错的.
     */
    TO_ANALYSIS,

    /**
     * DicAnalysis 用户自定义词典优先策略的分词
     * 用户自定义词典优先策略的分词,如果你的用户自定义词典足够好,或者你的需求对用户自定义词典的要求比较高,那么强烈建议你使用DicAnalysis的分词方式.
     * 可以说在很多方面Dic优于ToAnalysis的结果
     */
    DIC_ANALYSIS,

    /**
     * IndexAnalysis 面向索引的分词
     * 面向索引的分词。顾名思义就是适合在lucene等文本检索中用到的分词。 主要考虑以下两点
     * 召回率 * 召回率是对分词结果尽可能的涵盖。比如对“上海虹桥机场南路” 召回结果是[上海/ns, 上海虹桥机场/nt, 虹桥/ns, 虹桥机场/nz, 机场/n, 南路/nr]
     * 准确率 * 其实这和召回本身是具有一定矛盾性的Ansj的强大之处是很巧妙的避开了这两个的冲突 。比如我们常见的歧义句“旅游和服务”->对于一般保证召回 。大家会给出的结果是“旅游 和服 服务” 对于ansj不存在跨term的分词。意思就是。召回的词只是针对精准分词之后的结果的一个细分。比较好的解决了这个问题
     */
    INDEX_ANALYSIS,

    /**
     * NlpAnalysis 带有新词发现功能的分词
     * nlp分词是总能给你惊喜的一种分词方式.
     * 它可以识别出未登录词.但是它也有它的缺点.速度比较慢.稳定性差.ps:我这里说的慢仅仅是和自己的其他方式比较.应该是40w字每秒的速度吧.
     * 个人觉得nlp的适用方式.1.语法实体名抽取.未登录词整理.主要是对文本进行发现分析等工作
     */
    NLP_ANALYSIS,
    ;
}
