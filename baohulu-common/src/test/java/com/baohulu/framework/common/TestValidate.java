package com.baohulu.framework.common;

import com.baohulu.framework.common.domain.StudentBO;
import com.baohulu.framework.common.utils.validate.StringAnnotation;
import com.baohulu.framework.common.utils.validate.ValidateUtils;
import org.junit.Test;

/**
 * @author heqing
 * @date 2022/10/26 16:52
 */
public class TestValidate {

    @Test
    public void isPhoneNumber() {
        String str = "1306175000";
        boolean result = ValidateUtils.isPhoneNumber(str);
        System.out.println(result);
    }

    @Test
    public void isEmail() {
        String str = "111@qq.com";
        boolean result = ValidateUtils.isEmail(str);
        System.out.println(result);
    }

    @Test
    public void isUrl() {
        String str = "https://www.baidu.com";
        boolean result = ValidateUtils.isUrl(str);
        System.out.println(result);
    }

    @Test
    public void isIp() {
        String str = "127.0.0.1";
        boolean result = ValidateUtils.isIp(str);
        System.out.println(result);
    }

    @Test
    public void isIdentityCard() {
        String str = "240826202205010001";
        boolean result = ValidateUtils.isIdentityCard(str);
        System.out.println(result);
    }

    @Test
    public void isDate() {
        String str = "2022-05-01";
        boolean result = ValidateUtils.isDate(str);
        System.out.println(result);
    }

    @Test
    public void isChinese() {
        String str = "你好";
        boolean result = ValidateUtils.isChinese(str);
        System.out.println(result);
    }

    @Test
    public void isSpecialChar() {
        String str = "你#好";
        boolean result = ValidateUtils.isSpecialChar(str);
        System.out.println(result);
    }

    @Test
    public void isEmoji() {
        String str = "😀";
        boolean result = ValidateUtils.isEmoji(str);
        System.out.println(result);
    }

    @Test
    public void testValidate() {
        StudentBO student = new StudentBO();
        student.setId(1L);
        student.setName("小白");
        student.setNo("151");
        StringAnnotation.validate(student, "id", "name");
    }

    @Test
    public void isCheck() {
        boolean check = ValidateUtils.isPunctuation('，');
        System.out.println("是否标点符号--> " + check);

        check = ValidateUtils.isSpace(' ');
        System.out.println("是否空白字符--> " + check);

        check = ValidateUtils.isChinese('贺');
        System.out.println("是否中文字符--> " + check);

        check = ValidateUtils.isDigital('1');
        System.out.println("是否数字字符--> " + check);

        check = ValidateUtils.isNumberString("246512");
        System.out.println("是否数字--> " + check);

        check = ValidateUtils.isAlphabet('a');
        System.out.println("是否英文字符--> " + check);

        check = ValidateUtils.isAlphabetString("abc");
        System.out.println("是否英文--> " + check);

        check = ValidateUtils.hasChinese("0556贺小白");
        System.out.println("是否包含中文字符--> " + check);

        check = ValidateUtils.isAllChinese("贺小白");
        System.out.println("是否全由中文字符组成--> " + check);
    }
}
