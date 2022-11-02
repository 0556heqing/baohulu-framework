package com.baohulu.framework.common;

import com.baohulu.framework.basic.enums.ReturnEnum;
import com.baohulu.framework.basic.model.ReturnStatus;
import com.baohulu.framework.common.domain.BaseDomain;
import com.baohulu.framework.common.domain.MyPageResp;
import com.baohulu.framework.common.domain.StudentBO;
import com.baohulu.framework.common.mapstruct.domain.Address;
import com.baohulu.framework.common.mapstruct.domain.PersonBO;
import com.baohulu.framework.common.utils.ReflectionUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author heqing
 * @date 2022/10/26 17:49
 */
public class TestReflection  extends BaseDomain {

    @Test
    public void getFieldValue() {
        PersonBO personBO = getPersonBO();
        System.out.println("--> " + personBO);

        Object obj = ReflectionUtils.getFieldValue(personBO, "id");
        System.out.println("id --> " + obj);
        obj = ReflectionUtils.getFieldValue(personBO, "remarkJson");
        System.out.println("remarkJson --> " + obj);
        obj = ReflectionUtils.getFieldValue(personBO, "date");
        System.out.println("date --> " + obj);
        obj = ReflectionUtils.getFieldValue(personBO, "nameParts");
        System.out.println("nameParts --> " + obj);
        obj = ReflectionUtils.getFieldValue(personBO, "remarkMap");
        System.out.println("remarkMap --> " + obj);
        obj = ReflectionUtils.getFieldValue(personBO, "address");
        System.out.println("address --> " + obj);
        obj = ReflectionUtils.getFieldValue(personBO, "type");
        System.out.println("type --> " + obj);
    }

    @Test
    public void setFieldValue() {
        PersonBO personBO = getPersonBO();
        System.out.println("设置前 --> " + personBO);

        ReflectionUtils.setFieldValue(personBO, "name", "小白");

        System.out.println("设置后 --> " + personBO);
    }

    @Test
    public void getDeclaredField() {
        PersonBO personBO = getPersonBO();
        System.out.println("--> " + personBO);

        Field field = ReflectionUtils.getDeclaredField(personBO, "address");
        System.out.println("field --> " + field);

        List<Field> fieldList = ReflectionUtils.getDeclaredFields(personBO.getClass());
        fieldList.forEach(System.out::println);
    }

    @Test
    public void getDeclaredMethod() {
        PersonBO personBO = getPersonBO();
        System.out.println("--> " + personBO);

        Method method = ReflectionUtils.getDeclaredMethod(personBO.getClass(), "setAddress");
        System.out.println("method --> " + method);

        Class<?>[] parameterTypes = new Class[1];
        parameterTypes[0] = Address.class;
        Method newMethod = ReflectionUtils.getDeclaredMethod(personBO.getClass(), "setAddress", parameterTypes);
        System.out.println("newMethod --> " + newMethod);

        List<Method> methodList = ReflectionUtils.getDeclaredMethods(personBO);
        methodList.forEach(System.out::println);
    }

    @Test
    public void invokeMethod() {
        PersonBO personBO = getPersonBO();
        System.out.println("--> " + personBO);

        Class<?>[] parameterTypes = new Class[1];
        parameterTypes[0] = String.class;
        Object[] parameters = new Object[1];
        parameters[0] = "小白";
        ReflectionUtils.invokeMethod(personBO, "setName", parameterTypes, parameters);

        System.out.println("--> " + personBO);
    }

    @Test
    public void getGenericType() {
        Field field = ReflectionUtils.getDeclaredField(PersonBO.class, "nameParts");
        Class collectionClass = ReflectionUtils.getCollectionGenericType(field);
        System.out.println("Collection中的类型 --> " + collectionClass);

        field = ReflectionUtils.getDeclaredField(PersonBO.class, "remarkMap");
        Class[] mapClass = ReflectionUtils.getMapGenericType(field);
        for(int i = 0; i < mapClass.length; i++) {
            System.out.println("Map中 "+i+" 的类型 --> " + mapClass[i]);
        }

        MyPageResp myPageResp = new MyPageResp();
        Class superClass =  ReflectionUtils.getSuperClassGenericType(myPageResp.getClass());
        System.out.println("父类 --> " + superClass);
    }

    @Test
    public void check() {
        boolean isSubClass = ReflectionUtils.isSubClass(StudentBO.class, PersonBO.class);
        System.out.println("是否是字类 --> " + isSubClass);

        boolean isInterfaceOf = ReflectionUtils.isInterfaceOf(ReturnEnum.class, ReturnStatus.class);
        System.out.println("是否是接口实现类 --> " + isInterfaceOf);
    }

    @Test
    public void buildMethodSignature() {
        Method method = ReflectionUtils.getDeclaredMethod(PersonBO.class, "setAddress");
        String signature = ReflectionUtils.buildMethodSignature(method);
        System.out.println("方法签名 --> " + signature);
    }
}
