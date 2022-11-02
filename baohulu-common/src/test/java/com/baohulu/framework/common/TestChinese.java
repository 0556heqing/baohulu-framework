package com.baohulu.framework.common;

import com.baohulu.framework.common.utils.chinese.TokenizerUtils;
import com.baohulu.framework.common.utils.chinese.enums.AnalysisEnum;
import com.github.stuxuhai.jpinyin.ChineseHelper;
import com.github.stuxuhai.jpinyin.PinyinException;
import com.github.stuxuhai.jpinyin.PinyinHelper;
import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.ansj.domain.Result;
import org.ansj.library.AmbiguityLibrary;
import org.ansj.library.DicLibrary;
import org.ansj.library.StopLibrary;
import org.ansj.library.SynonymsLibrary;
import org.ansj.recognition.impl.BookRecognition;
import org.ansj.recognition.impl.EmailRecognition;
import org.ansj.recognition.impl.StopRecognition;
import org.ansj.recognition.impl.URLRecognition;
import org.junit.Test;
import org.nlpcn.commons.lang.tire.domain.Value;
import org.nlpcn.commons.lang.tire.library.Library;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.stuxuhai.jpinyin.PinyinFormat.*;

/**
 * @author heqing
 * @date 2022/10/28 15:54
 */
public class TestChinese {


    @Test
    public void pinyin() throws PinyinException, FileNotFoundException {
        // 添加自定义的 繁->简 字典
        PinyinHelper.addPinyinDict("src/test/resources/chinese/add_pinyin.dict");
        PinyinHelper.addMutilPinyinDict("src/test/resources/chinese/add_mutil_pinyin.dict");

        String str = "贺小白，的，目的，的确，好的，宝葫芦";

        String shortPy = PinyinHelper.getShortPinyin(str);
        System.out.println("首字母 --> " + shortPy);

        String tone = PinyinHelper.convertToPinyinString(str, "", WITHOUT_TONE);
        System.out.println("没有音调 --> " + tone);

        String mark = PinyinHelper.convertToPinyinString(str, "", WITH_TONE_MARK);
        System.out.println("带有音调标记 --> " + mark);

        String number = PinyinHelper.convertToPinyinString(str, "", WITH_TONE_NUMBER);
        System.out.println("带有音调号 --> " + number);
    }

    @Test
    public void chinese() throws FileNotFoundException {
        // 添加自定义的 繁->简 字典
        ChineseHelper.addChineseDict("src/test/resources/chinese/traditional.dict");

        String str = ChineseHelper.convertToTraditionalChinese("美宝湿润烧伤膏");
        System.out.println("转繁体 -->" + str);

        str = ChineseHelper.convertToSimplifiedChinese("美寳湿润烧伤膏");
        System.out.println("转简体 -->" + str);
    }

    String source = "毛泽东主席对小学生说: '小朋友们，今天天气真好啊。不过还是要多喝白云山板蓝根颗粒预防感冒！'";
    boolean isStop = true, isSynonyms = false;

    @Test
    public void loadUserDict() {
        /**
         *  加载自定义字典，两种方式
         *      1. 配置 library.properties 文件，并正确放到 resources 中
         *      2. (不建议)如下代码中加载:MyStaticValue.ENV.put(DicLibrary.DEFAULT,"library/default.dic");
         */
        List<String> words = TokenizerUtils.getWord(source, AnalysisEnum.BASE_ANALYSIS, isStop, isSynonyms);
        System.out.println("base 分词结果： --> " + words);

        words = TokenizerUtils.getWord(source, AnalysisEnum.TO_ANALYSIS, isStop, isSynonyms);
        System.out.println("to 分词结果： --> " + words);

        words = TokenizerUtils.getWord(source, AnalysisEnum.DIC_ANALYSIS, isStop, isSynonyms);
        System.out.println("dic 分词结果： --> " + words);

        words = TokenizerUtils.getWord(source, AnalysisEnum.INDEX_ANALYSIS, isStop, isSynonyms);
        System.out.println("index 分词结果： --> " + words);

        words = TokenizerUtils.getWord(source, AnalysisEnum.NLP_ANALYSIS, isStop, isSynonyms);
        System.out.println("nlp 分词结果： --> " + words);
    }

    @Test
    public void dynamicWord() {
        /**
         *  将 词、歧义词、停用词、同义词写入内存（建议不要这么做，一旦写入内存。服务重启，写入的内存会丢失。）
         *  注意：不提供动态处理词的工具方法，否则多台服务器会给大家留坑。
         *       建议将字典放入数据库/远程文件/apollo中，更新词后mq通知用到的服务重新加载。
         *
         */
        
        List<String> words = TokenizerUtils.getWord(source);
        System.out.println("分词结果： --> " + words);

        // 添加单个字至自定义词典
        Library.insertWord(DicLibrary.get(), new Value("白云山板蓝根", "n", "112"));
        // 添加歧义词
        AmbiguityLibrary.insert(AmbiguityLibrary.DEFAULT, new Value("小朋友们", "小朋友们，", "n"));
        // 添加停用词
        StopLibrary.insertStopWords(StopLibrary.DEFAULT, "颗粒");

        words = TokenizerUtils.getWord(source);
        System.out.println("加入新词 分词结果： --> " + words);

        // 从自定义词典删除单个
        Library.removeWord(DicLibrary.get(), "白云山板蓝根");
        // 删除整个歧义词典
        AmbiguityLibrary.remove(AmbiguityLibrary.DEFAULT);
        // 删除整个停用词典
        StopLibrary.reload(StopLibrary.DEFAULT);

        words = TokenizerUtils.getWord(source);
        System.out.println("去掉新词 分词结果： --> " + words);
    }

    @Test
    public void testStop() {
        StopRecognition filter = new StopRecognition();
        filter.insertStopNatures("y"); //过滤词性
        filter.insertStopWords("白云山"); //过滤单词
        filter.insertStopRegexes("小.*?"); //支持正则表达

        Result result = TokenizerUtils.getWordResult(source, AnalysisEnum.TO_ANALYSIS, isStop, isSynonyms);
        System.out.println("过滤前 --> " + result);

        result = result.recognition(filter);
        System.out.println("过滤后 --> " + result);
    }

    @Test
    public void testSynonyms() {
        String source = "中国是中华人民共和国,也是华夏";

        Result result = TokenizerUtils.getWordResult(source, AnalysisEnum.TO_ANALYSIS, isStop, true);
        System.out.println("前 --> " + result.getTerms().stream().filter(term -> term!=null && term.getSynonyms()!=null).map(term -> term.getSynonyms()).collect(Collectors.toList()));

        SynonymsLibrary.insert(SynonymsLibrary.DEFAULT, new String[] { "中国", "华夏" });
        result = TokenizerUtils.getWordResult(source, AnalysisEnum.TO_ANALYSIS, isStop, true);
        System.out.println("前 --> " + result.getTerms().stream().filter(term -> term!=null && term.getSynonyms()!=null).map(term -> term.getSynonyms()).collect(Collectors.toList()));

        SynonymsLibrary.append(SynonymsLibrary.DEFAULT, new String[] { "中国", "华夏", "中华人民共和国" });
        result = TokenizerUtils.getWordResult(source, AnalysisEnum.TO_ANALYSIS, isStop, true);
        System.out.println("前 --> " + result.getTerms().stream().filter(term -> term!=null && term.getSynonyms()!=null).map(term -> term.getSynonyms()).collect(Collectors.toList()));
    }


    @Test
    public void testFind() {
        /**
         * 诸如电子邮件等不是很好用，建议自己实现（去掉最后的标点试试）
         */
        String sentence = "我2018年在 https://www.jd.com 买的《由浅入深学JAVA》不是很好。联系: xiaobai@qq.com或13061750001";
        Result result = TokenizerUtils.getWordResult(sentence, AnalysisEnum.TO_ANALYSIS, isStop, isSynonyms);
        System.out.println("区分前" + result.getTerms().toString());

        result = result.recognition(new URLRecognition()).recognition(new BookRecognition()).recognition(new EmailRecognition());
        System.out.println("区分后" + result.getTerms().toString());
    }

    @Test
    public void test() {
        KeyWordComputer kwc = new KeyWordComputer(5);
        String title = "毛泽东讲话";
        // 关键词抽取
        Collection<Keyword> result = kwc.computeArticleTfidf(title, source);
        System.out.println(result);
    }

}
