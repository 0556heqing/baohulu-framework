package com.baohulu.framework.common.utils.chinese;

import com.baohulu.framework.common.utils.chinese.enums.AnalysisEnum;
import org.ansj.domain.Result;
import org.ansj.library.StopLibrary;
import org.ansj.library.SynonymsLibrary;
import org.ansj.recognition.impl.SynonymsRecgnition;
import org.ansj.splitWord.analysis.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 分词工具类
 *
 * @author heqing
 * @date 2022/10/28 15:44
 */
public class TokenizerUtils {

    /**
     * 将段落切分为词
     *
     * @param source 原始段落
     * @return 分词结果
     */
    public static List<String> getWord(String source) {
        return getWord(source, AnalysisEnum.TO_ANALYSIS, true, false);
    }

    /**
     * 将段落切分为词
     *
     * @param source 原始段落
     * @param analysisEnum 分词器
     * @param isStop 是否使用过滤词
     * @param isSynonyms 是否需要返回 同义词
     * @return 分词结果
     */
    public static List<String> getWord(String source, AnalysisEnum analysisEnum, Boolean isStop, Boolean isSynonyms) {
        List<String> words = new ArrayList<>();
        Result result = getWordResult(source, analysisEnum, isStop, isSynonyms);
        if(result != null) {
            words = result.getTerms().stream().map(term -> term.getName()).collect(Collectors.toList());
        }
        return words;
    }

    /**
     * 将段落切分为词(词性)
     *
     * @param source 原始段落
     * @param analysisEnum 分词器
     * @param isStop 是否使用过滤词
     * @param isSynonyms 是否需要返回 同义词
     * @return 分词(词性)结果
     */
    public static Result getWordResult(String source, AnalysisEnum analysisEnum, Boolean isStop, Boolean isSynonyms) {
        Result result = null;
        switch (analysisEnum) {
            case BASE_ANALYSIS:
                result = BaseAnalysis.parse(source);
                break;
            case TO_ANALYSIS:
                result = ToAnalysis.parse(source);
                break;
            case DIC_ANALYSIS:
                result = DicAnalysis.parse(source);
                break;
            case INDEX_ANALYSIS:
                result = IndexAnalysis.parse(source);
                break;
            case NLP_ANALYSIS:
                result = NlpAnalysis.parse(source);
                break;
            default:
                break;
        }
        if(isStop && StopLibrary.get() != null) {
            result = result.recognition(StopLibrary.get());
        }
        if(isSynonyms && SynonymsLibrary.get() != null) {
            result = result.recognition(new SynonymsRecgnition());
        }
        return result;
    }

}
