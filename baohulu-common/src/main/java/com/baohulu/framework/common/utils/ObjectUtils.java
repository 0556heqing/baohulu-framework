package com.baohulu.framework.common.utils;

import com.baohulu.framework.basic.consts.Network;
import com.baohulu.framework.basic.consts.Operation;
import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Java对象类转换工具，提供 map/xml与Java对象的转换
 *
 * @author heqing
 * @date 2022/10/18 19:36
 */
public class ObjectUtils {

    /**
     * 对象非空判断
     */
    @SuppressWarnings("rawtypes")
    public static Boolean isNull(Object t) {
        if (t == null) {
            return true;
        }
        // String
        if (t instanceof String) {
            return StringUtils.isBlank((String) t);
        }
        // List
        if (t instanceof List) {
            return ((List) t).size() == 0;
        }
        // Map
        if (t instanceof Map) {
            return ((Map) t).size() == 0;
        }
        // Set
        if (t instanceof Set) {
            return ((Set) t).size() == 0;
        }
        // 数组
        if (t instanceof Object[]) {
            return ((Object[]) t).length == 0;
        }
        return false;
    }

    /**
     * 自定义函数去重
     *
     * @param keyExtractor
     * @param <T>
     * @return
     */
    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object,Boolean> seen = new ConcurrentHashMap<>(4);
        return t -> seen.putIfAbsent(keyExtractor.apply(t) != null ? keyExtractor.apply(t) : "", Boolean.TRUE) == null;
    }

    /**
     * 通过对象属性名获取对象中的属性值
     */
    public static Object getProp(Object object, String name) {
        if (object == null || name == null || "".equals(name.trim())) {
            return null;
        }
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(object.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String tempName = property.getName();
                if (name.equals(tempName)) {
                    Method getter = property.getReadMethod();
                    Object value = null;
                    if (getter != null) {
                        value = getter.invoke(object);
                    }
                    return value;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对象 转 Map
     * @param object java对象
     * @return map
     */
    public static Map<String, Object> objectToMap(Object object)  {
        Map<String, Object> map = new HashMap<>(4);
        Field[] fields = object.getClass().getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                map.put(field.getName(), field.get(object));
            }
        }  catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * Map 转 对象
     * @param map map数据
     * @param beanClass 需要转换的对象类型
     * @param <T> 对象
     * @return 对象
     * @throws Exception
     */
    public static <T> T mapToObject(Map map, Class<T> beanClass) {
        try {
            T object = beanClass.newInstance();
            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }
                field.setAccessible(true);
                if (map.containsKey(field.getName())) {
                    field.set(object, map.get(field.getName()));
                }
            }
            return object;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对象 转 xml字符串
     *
     * @param obj 对象
     * @return xml字符串
     */
    public static String objectToXml(Object obj)  {
        // 创建输出流
        StringWriter sw = new StringWriter();
        try {
            // 利用jdk中自带的转换类实现
            JAXBContext context = JAXBContext.newInstance(obj.getClass());

            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_ENCODING, Network.UTF_8);
            // 格式化xml输出的格式
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            // 去掉生成xml的默认报文头。
            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

            // 将对象转换成输出流形式的xml
            marshaller.marshal(obj, sw);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        // 使用CharacterEscapeHandler打包失败，不得已这样做。有空研究CharacterEscapeHandler后再改正
        return sw.toString().replaceAll(Operation.LT, Operation.LESS_THAN).replaceAll(Operation.GT, Operation.GREATER_THAN);
    }

    /**
     * xml字符串 转 对象
     * @param xmlStr xml字符串
     * @param clazz 对象类型
     * @return java对象
     */
    public static <T> T xmlToObject(String xmlStr, Class<T> clazz) {
        try {
            T obj = clazz.newInstance();
            JAXBContext context = JAXBContext.newInstance(clazz);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            obj = (T)unmarshaller.unmarshal(new StringReader(xmlStr));
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}

