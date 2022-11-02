package com.baohulu.framework.common.utils.validate.annotation;

import com.baohulu.framework.common.utils.validate.enums.RegexTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 限制String值
 *
 * @author heqing
 * @date 2022/10/26 17:33
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface StringValidation {

    // 是否允许为 null
    boolean nullAble() default true;

    // 是否允许为 空字符串。当不允许空字符串时，默认不允许null
    boolean blackAble() default true;

    // 如果为NUMBER，则允许的最大数，否则允许的最大长度
    double max() default 0;

    // 如果为NUMBER，则允许的最小数，否则允许的最小长度
    double min() default 0;

    // 该字段的类型
    RegexTypeEnum regexType() default RegexTypeEnum.NONE;

    // 自定义的正则表达式
    String regexExpression() default "";

    // 说明
    String description() default "";
}
