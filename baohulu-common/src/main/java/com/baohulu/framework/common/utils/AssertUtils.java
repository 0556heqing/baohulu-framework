package com.baohulu.framework.common.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author heqing
 * @date 2022/11/18 11:41
 */
public class AssertUtils {

    public static void isNotTrue(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isNotFalse(boolean expression, String message) {
        if (expression) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isBlank(String text, String message) {
        if (StringUtils.isBlank(text)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isNotNumeric(String text, String message) {
        isBlank(text, message);
        if (!StringUtils.isNumeric(text)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isNotNull(Object object, String message) {
        if (object != null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isNull(Object object, String message) {
        if (ObjectUtils.isNull(object)) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isEmpty(Object[] array, String message) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void isInstanceOf(Class<?> type, Object obj, String message) {
        isNull(type, "Type to check against must not be null");
        if (!type.isInstance(obj)) {
            throw new IllegalArgumentException((message != null && message.length() > 0 ? message + " " : "") + "Object of class [" + (obj != null ? obj.getClass().getName() : "null") + "] must be an instance of " + type);
        }
    }

    public static void isAssignable(Class<?> superType, Class<?> subType, String message) {
        isNull(superType, "Type to check against must not be null");
        if (subType == null || !superType.isAssignableFrom(subType)) {
            throw new IllegalArgumentException(message + subType + " is not assignable to " + superType);
        }
    }

}
