package com.baohulu.framework.common.utils.validate;

import com.baohulu.framework.basic.enums.ReturnEnum;
import com.baohulu.framework.basic.exception.AppException;
import com.baohulu.framework.common.utils.validate.annotation.StringValidation;
import com.baohulu.framework.common.utils.validate.enums.RegexTypeEnum;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author heqing
 * @date 2022/10/28 10:49
 */
public class StringAnnotation {

    private static final Logger logger = LoggerFactory.getLogger(ValidateUtils.class);

    /**
     * 校验指定对象的指定字段是否为空
     *
     * @param object
     * @param fields
     * @return
     * @throws Exception
     */
    public static void validate(Object object, String... fields) {
        if (object == null) {
            logger.warn(">>>入参对象为空!");
            throw new AppException(ReturnEnum.PARAM_ERROR, "入参对象不能为空!");
        }
        //校验指明字段
        validateFields(object, fields);
        //校验注解
        validateAnnotation(object);
    }

    private static void validateFields(Object object, String[] fields) {
        for (String name : fields) {
            Method method = getMethod(object.getClass(), name);
            Object value = null;
            try {
                value = method.invoke(object);
            } catch (Exception e) {
                logger.warn("反射调用get方法失败", e);
            }
            Boolean isStr = value instanceof String && StringUtils.isBlank(((String) value));
            if (value == null || isStr) {
                logger.warn(">>>入参字段{}为空!", name);
                throw new AppException(ReturnEnum.PARAM_ERROR, "入参字段[" + name + "]不能为空!");
            }
        }
    }

    /**
     * 获取指定字段的get方法
     *
     * @param clazz
     * @param field
     * @return
     */
    private static Method getMethod(Class<?> clazz, String field) {
        Method method;

        String methodName = "get" + field.substring(0, 1).toUpperCase()
                + field.substring(1);
        try {
            method = clazz.getMethod(methodName);
        } catch (Exception e) {
            logger.warn("getMethod failed", e);
            throw new AppException(ReturnEnum.PARAM_ERROR, "参数字段错误");
        }
        return method;
    }

    public static void validateAnnotation(Object object) {
        // 获取对象的基本信息
        Class<? extends Object> clazz = object.getClass();
        // 获取对象及父类的属性信息
        Field[] fields = new Field[]{};
        fields = recurseSuper(clazz, fields);

        Arrays.asList(fields).stream().distinct().forEach(field -> {
            // 允许访问属性私有变量
            field.setAccessible(true);
            // 验证属性值是否正常
            validate(object, field);
            // 设置不允许
            field.setAccessible(false);
        });
    }

    private static void validate(Object object, Field field) {
        // 判断哪个注解
        if(field.isAnnotationPresent(StringValidation.class)) {
            validationStringField(object, field);
        }
    }

    private  static void validationStringField(Object object, Field field) {
        String string  = "class java.lang.String";
        if (!string.equals(field.getGenericType().toString())) {
            logger.error("类：{} 属性：{} 不是String类型，不应该使用该注解 ", object.getClass().getName(), field.getName());
            throw new AppException(ReturnEnum.PARAM_ERROR, "参数字段错误");
        }

        // 获取属性值
        Object value = null;
        try {
            value = field.get(object);
        } catch (Exception e) {
            logger.error("reflect error:" + e.getMessage(), e);
        }
        // 获取类名
        String className = object.getClass().getName();
        // 获取字段名
        String fieldName = field.getName();

        // 获取注解属性
        StringValidation stringField = field.getDeclaredAnnotation(StringValidation.class);
        if(stringField != null) {
            boolean isSuccess = true;
            boolean isValue = (value == null || StringUtils.isBlank(value.toString()));
            if (!stringField.nullAble() && value == null) {
                isSuccess = false;
                logger.error("类：{} 属性：{} 参数未传入", className, fieldName);
            } else if (!stringField.blackAble() && isValue) {
                isSuccess = false;
                logger.error("类：{} 属性：{}  值不能为null或空字符串", className, fieldName);
            } else if(value !=null && StringUtils.isNotBlank(value.toString())) {
                if(stringField.regexType() == RegexTypeEnum.NUMBER) {
                    isSuccess = checkNumberField(stringField, className, fieldName, value);
                } else {
                    isSuccess = checkStringField(stringField, className, fieldName, value);
                }
            }
            if(!isSuccess) {
                throw new AppException(ReturnEnum.PARAM_ERROR, "参数字段("+fieldName+")错误");
            }
        }
    }

    private static boolean checkNumberField(StringValidation stringField, String className, String fieldName, Object value) {
        boolean isSuccess = true;
        if (!NumberUtils.isNumber(value.toString())) {
            isSuccess = false;
            logger.error("类：{} 属性：{}  不是数字，传入值：{}", className, fieldName, value);
        } else if (stringField.max() != 0 && Double.valueOf(value.toString()) > stringField.max()) {
            isSuccess = false;
            logger.error("类：{} 属性：{}  值不能大于 {}，传入值：{}", className, fieldName, stringField.max(), value);
        } else if (stringField.min() != 0 && Long.valueOf(value.toString()) < stringField.min()) {
            isSuccess = false;
            logger.error("类：{} 属性：{}  值不能小于 {}，传入值：{}", className, fieldName, stringField.min(), value);
        }
        return isSuccess;
    }

    private static boolean checkStringField(StringValidation stringField, String className, String fieldName, Object value) {
        boolean isSuccess = true;
        if (stringField.max() != 0 && value.toString().length() > stringField.max()) {
            isSuccess = false;
            logger.error("类：{} 属性：{}  长度不能超过 {}，传入值：{}", className, fieldName, stringField.max(), value);
        } else if (stringField.min() != 0 && value.toString().length() < stringField.min()) {
            isSuccess = false;
            logger.error("类：{} 属性：{}  长度不能小于 {}，传入值：{}", className, fieldName, stringField.min(), value);
        }
        switch (stringField.regexType()) {
            case IDENTITY_CARD :
                if (!ValidateUtils.isIdentityCard(value.toString())) {
                    isSuccess = false;
                    logger.error("类：{} 属性：{}  不是身份证号码，传入值：{}", className, fieldName, value);
                }
                break;
            case PHONE_NUMBER :
                if (!ValidateUtils.isPhoneNumber(value.toString())) {
                    isSuccess = false;
                    logger.error("类：{} 属性：{}  不是电话号码，传入值：{}", className, fieldName, value);
                }
                break;
            case EMAIL :
                if (!ValidateUtils.isEmail(value.toString())) {
                    isSuccess = false;
                    logger.error("类：{} 属性：{}  不是电子邮件，传入值：{}", className, fieldName, value);
                }
                break;
            case IP :
                if (!ValidateUtils.isIp(value.toString())) {
                    isSuccess = false;
                    logger.error("类：{} 属性：{}  不是网络IP，传入值：{}", className, fieldName, value);
                }
                break;
            case DATE :
                if (!ValidateUtils.isDate(value.toString())) {
                    isSuccess = false;
                    logger.error("类：{} 属性：{}  不是日期，传入值：{}", className, fieldName, value);
                }
                break;
            case CHINESE :
                if (!ValidateUtils.isChinese(value.toString())) {
                    isSuccess = false;
                    logger.error("类：{} 属性：{}  只能是中文字符，传入值：{}", className, fieldName, value);
                }
                break;
            case NO_SPECIAL_CHAR:
                if (ValidateUtils.isSpecialChar(value.toString())) {
                    isSuccess = false;
                    logger.error("类：{} 属性：{}  不能含有特殊字符，传入值：{}", className, fieldName, value);
                }
                break;
            case NO_EMOJI:
                if (ValidateUtils.isEmoji(value.toString())) {
                    isSuccess = false;
                    logger.error("类：{} 属性：{}  不能含有特殊表情，传入值：{}", className, fieldName, value);
                }
                break;
            default:
                break;
        }
        if (StringUtils.isNotBlank(stringField.regexExpression()) && !ValidateUtils.matches(value.toString(), stringField.regexExpression())) {
            isSuccess = false;
            logger.error("类：{} 属性：{}  格式不正确，传入值：{}， 正则格式为：{}", className, fieldName, value, stringField.regexExpression());
        }
        return isSuccess;
    }

    private static Field[] recurseSuper(Class<? extends Object> clazz,  Field[] fields) {
        if (clazz != null) {
            // 获取当前 对象的属性信息
            fields = ArrayUtils.addAll(fields, clazz.getDeclaredFields());
        }
        // 是否存在父对象
        if (clazz.getSuperclass() != null) {
            // 获取父对象的属性信息
            fields = recurseSuper(clazz.getSuperclass(), fields);
        }
        return fields;
    }
}
