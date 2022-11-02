package com.baohulu.framework.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 反射工具类
 *
 * @author heqing
 * @date 2022/10/26 17:40
 */
public class ReflectionUtils {
    private final static Logger logger = LoggerFactory.getLogger(ReflectionUtils.class);

    private final static Map<Class, List<Field>> CLASS_FILED_MAP = new ConcurrentHashMap();

    /**
     * 获取对象中某个属性的值
     * @param object 对象
     * @param fieldName 属性名
     * @return 属性值
     */
    public static Object getFieldValue(Object object, String fieldName) {
        Field field = getDeclaredField(object, fieldName);
        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
        } else {
            makeAccessible(field);
            Object result = null;

            try {
                result = field.get(object);
            } catch (IllegalAccessException e) {
                logger.error("不可能抛出的异常{}", e);
            }

            return result;
        }
    }

    /**
     * 设置对象中某个属性的值
     * @param object 对象
     * @param fieldName 属性名
     * @param value 新值
     */
    public static void setFieldValue(Object object, String fieldName, Object value) {
        Field field = getDeclaredField(object, fieldName);
        if (field == null) {
            throw new IllegalArgumentException("Could not find field [" + fieldName + "] on target [" + object + "]");
        } else {
            makeAccessible(field);

            try {
                field.set(object, value);
            } catch (IllegalAccessException e) {
                logger.error("不可能抛出的异常:{}", e);
            }

        }
    }

    /**
     * 获取对象中所有字段信息
     * @param object 对象
     */
    public static List<Field> getDeclaredFields(Object object) {
        return getDeclaredFields(object.getClass());
    }

    /**
     * 获取对象中相同字段名的信息
     * @param object 对象
     * @param fieldName 字段名
     */
    public static Field getDeclaredField(Object object, String fieldName) {
        return getDeclaredField(object.getClass(), fieldName);
    }

    /**
     * 获取类信息中所有字段信息
     * @param clazz 类信息
     */
    public static List<Field> getDeclaredFields(Class clazz) {
        List<Field> classFieldList = CLASS_FILED_MAP.get(clazz);
        if (classFieldList != null) {
            return classFieldList;
        } else {
            List<Field> fieldList = new ArrayList();

            for(Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
                Field[] fields = superClass.getDeclaredFields();
                if (fields != null && fields.length > 0) {
                    Field[] tempFields = fields;
                    int fieldsSize = fields.length;

                    for(int index = 0; index < fieldsSize; ++index) {
                        Field field = tempFields[index];
                        boolean contains = false;
                        Iterator iterator = fieldList.iterator();

                        while(iterator.hasNext()) {
                            Field field1 = (Field)iterator.next();
                            if (field1.getName().equals(field.getName())) {
                                contains = true;
                                break;
                            }
                        }

                        if (!contains) {
                            fieldList.add(field);
                        }
                    }
                }
            }

            CLASS_FILED_MAP.put(clazz, fieldList);
            return fieldList;
        }
    }

    /**
     * 获取类中相同字段名的信息
     * @param clazz 类
     * @param fieldName 字段名
     */
    public static Field getDeclaredField(Class clazz, String fieldName) {
        List<Field> fields = getDeclaredFields(clazz);
        if (fields != null && fields.size() > 0) {
            Iterator iterator = fields.iterator();

            while(iterator.hasNext()) {
                Field field = (Field)iterator.next();
                if (field.getName().equals(fieldName)) {
                    return field;
                }
            }
        }

        return null;
    }

    /**
     * 设置属性可访问
     * @param field 属性
     */
    public static void makeAccessible(Field field) {
        if (!Modifier.isPublic(field.getModifiers()) || !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
            field.setAccessible(true);
        }

    }

    /**
     * 设置对象中某个方法信息
     * @param object 对象
     * @param methodName 方法名
     */
    public static Method getDeclaredMethod(Object object, String methodName) {
        return getDeclaredMethod(object.getClass(), methodName);
    }

    /**
     * 获取类中某个方法信息，如果有多个相同的方法名，返回第一个信息
     * @param clazz 类
     * @param methodName 方法名
     */
    public static Method getDeclaredMethod(Class<?> clazz, String methodName) {
        for(Class superClass = clazz; superClass != Object.class; superClass = superClass.getSuperclass()) {
            Method[] methods = superClass.getDeclaredMethods();
            Method[] tempMethods = methods;
            int methodsSize = methods.length;

            for(int i = 0; i < methodsSize; ++i) {
                Method method = tempMethods[i];
                if (method.getName().equals(methodName)) {
                    return method;
                }
            }
        }

        return null;
    }

    /**
     * 设置对象中所有方法
     * @param object 对象
     */
    public static List<Method> getDeclaredMethods(Object object) {
        return getDeclaredMethods(object.getClass());
    }

    /**
     * 设置类中某个方法信息
     * @param clazz 类
     */
    public static List<Method> getDeclaredMethods(Class<?> clazz) {
        List<Method> methodList = new ArrayList();

        for(Class superClass = clazz; superClass != Object.class && superClass != null; superClass = superClass.getSuperclass()) {
            Method[] methods = superClass.getDeclaredMethods();
            Collections.addAll(methodList, methods);
        }

        return methodList;
    }

    /**
     * 调用对象的某个方法
     * @param object 对象
     * @param methodName 方法名
     * @param parameterTypes 方法参数类型
     * @param parameters 方法参数值
     */
    public static Object invokeMethod(Object object, String methodName, Class<?>[] parameterTypes, Object[] parameters) {
        Method method = getDeclaredMethod(object, methodName, parameterTypes);
        if (method == null) {
            throw new IllegalArgumentException("Could not find method [" + methodName + "] on target [" + object + "]");
        } else {
            method.setAccessible(true);

            try {
                return method.invoke(object, parameters);
            } catch (Exception e) {
                throw convertReflectionExceptionToUnchecked(e);
            }
        }
    }

    /**
     * 获取对象中某个方法信息且方法的参数必须一致。
     * @param object 对象
     * @param methodName 方法名
     * @param parameterTypes 参数信息
     */
    public static Method getDeclaredMethod(Object object, String methodName, Class<?>[] parameterTypes) {
        return getDeclaredMethod(object.getClass(), methodName, parameterTypes);
    }

    /**
     * 获取类中某个方法信息且方法的参数必须一致。
     * @param clazz 类
     * @param methodName 方法名
     * @param parameterTypes 参数信息
     */
    public static Method getDeclaredMethod(Class<?> clazz, String methodName, Class<?>[] parameterTypes) {
        Class superClass = clazz;

        while(superClass != Object.class) {
            try {
                if (superClass == null) {
                    return null;
                }

                return superClass.getDeclaredMethod(methodName, parameterTypes);
            } catch (NoSuchMethodException e) {
                superClass = superClass.getSuperclass();
            }
        }
        return null;
    }

    /**
     * 获取父类中泛型的具体类型
     * @param clazz 类
     */
    public static <T> Class<T> getSuperClassGenericType(Class clazz) {
        return getSuperClassGenericType(clazz, 0);
    }

    /**
     * 获取类中泛型的具体类型
     * @param clazz 类
     * @param index 第几个泛型
     */
    public static Class getSuperClassGenericType(Class clazz, int index) {
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            logger.warn(clazz.getSimpleName() + "'s superclass not ParameterizedType");
            return Object.class;
        } else {
            Type[] params = ((ParameterizedType)genType).getActualTypeArguments();
            if (index < params.length && index >= 0) {
                if (!(params[index] instanceof Class)) {
                    logger.warn(clazz.getSimpleName() + " not set the actual class on superclass generic parameter");
                    return Object.class;
                } else {
                    return (Class)params[index];
                }
            } else {
                logger.warn("Index: " + index + ", Size of " + clazz.getSimpleName() + "'s Parameterized Type: " + params.length);
                return Object.class;
            }
        }
    }

    /**
     * 获取集合中的具体类型
     * @param field 属性名
     */
    public static Class getCollectionGenericType(Field field) {
        ParameterizedType pt = (ParameterizedType)field.getGenericType();
        return pt.getActualTypeArguments().length > 0 ? (Class)pt.getActualTypeArguments()[0] : null;
    }

    /**
     * 获取Map中的具体类型
     * @param field 属性名
     */
    public static Class[] getMapGenericType(Field field) {
        ParameterizedType pt = (ParameterizedType)field.getGenericType();
        if (pt.getActualTypeArguments().length > 1) {
            Class[] classes = new Class[]{(Class)pt.getActualTypeArguments()[0], (Class)pt.getActualTypeArguments()[1]};
            return classes;
        } else {
            return null;
        }
    }

    /**
     * clazz1是否是clazz2的子类
     * @param clazz1 比较类1
     * @param clazz2 比较类2
     */
    public static boolean isSubClass(Class clazz1, Class clazz2) {
        for(Class parent = clazz1.getSuperclass(); parent != null; parent = parent.getSuperclass()) {
            if (parent.getName().equals(clazz2.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * clazz1是否是clazz2的接口实现类
     * @param clazz1 比较类1
     * @param clazz2 比较类2
     */
    public static boolean isInterfaceOf(Class clazz1, Class clazz2) {
        Class[] interfaces = clazz1.getInterfaces();
        if (interfaces != null && interfaces.length != 0) {
            Class[] tempInterfaces = interfaces;
            int interfacesSize = interfaces.length;

            for(int i = 0; i < interfacesSize; ++i) {
                Class anInterface = tempInterfaces[i];
                if (anInterface.getName().equals(clazz2.getName())) {
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }

    /**
     * 生成方法签名
     * @param clazz 方法所在类
     * @param methodName 方法名
     * @param parameterTypes 方法参数
     */
    public static String buildMethodSignature(Class<?> clazz, String methodName, Class<?>[] parameterTypes) {
        StringBuilder builder = new StringBuilder();
        builder.append(clazz.getName());
        builder.append(".");
        builder.append(methodName);
        builder.append("(");

        for(int i = 0; i < parameterTypes.length; ++i) {
            builder.append(i > 0 ? "," : "");
            builder.append(parameterTypes[i].getName());
        }

        builder.append(")");
        return builder.toString();
    }

    /**
     * 生成方法签名
     * @param method 方法
     */
    public static String buildMethodSignature(Method method) {
        return buildMethodSignature(method.getDeclaringClass(), method.getName(), method.getParameterTypes());
    }

    /**
     * 反射异常
     */
    public static RuntimeException convertReflectionExceptionToUnchecked(Exception e) {
        if (!(e instanceof IllegalAccessException) && !(e instanceof IllegalArgumentException) && !(e instanceof NoSuchMethodException)) {
            if (e instanceof InvocationTargetException) {
                return new RuntimeException("Reflection Exception.", ((InvocationTargetException)e).getTargetException());
            } else {
                return e instanceof RuntimeException ? (RuntimeException)e : new RuntimeException("Unexpected Checked Exception.", e);
            }
        } else {
            return new IllegalArgumentException("Reflection Exception.", e);
        }
    }
}

