package com.baohulu.framework.common;

import com.baohulu.framework.common.domain.BaseDomain;
import com.baohulu.framework.common.mapstruct.PersonMapstructConverter;
import com.baohulu.framework.common.mapstruct.domain.Address;
import com.baohulu.framework.common.mapstruct.domain.PersonBO;
import com.baohulu.framework.common.mapstruct.domain.PersonDTO;
import com.baohulu.framework.common.utils.AssertUtils;
import com.baohulu.framework.common.utils.ObjectUtils;
import com.baohulu.framework.common.utils.ListUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author heqing
 * @date 2022/10/19 10:32
 */
public class TestObject extends BaseDomain {

    @Test
    public void testBean() {
        PersonBO personBO = getPersonBO();
        System.out.println("转换前 --> " + personBO);

        PersonDTO personDTO = PersonMapstructConverter.INSTANCE.boToDto(personBO);
        System.out.println("boToDto --> "+personDTO);

        personBO = PersonMapstructConverter.INSTANCE.dtoToBo(personDTO);
        System.out.println("dtoToBo --> "+personBO);
    }

    @Test
    public void distinctByKey() {
        List<Address> addList = getAddList();
        System.out.println("过滤前 -->" + addList);
        List<Address> newAddList = addList.stream().filter(ObjectUtils.distinctByKey(d -> d.getProvince())).collect(Collectors.toList());
        System.out.println("过滤后 -->" + newAddList);
    }


    @Test
    public void getProp() {
        PersonBO personBO = getPersonBO();
        Address address = (Address) ObjectUtils.getProp(personBO, "address");
        System.out.println("--> " + address);
    }

    @Test
    public void testMap() {
        PersonBO personBO = getPersonBO();
        System.out.println("转换前 --> " + personBO);

        Map<String, Object> map = ObjectUtils.objectToMap(personBO);
        System.out.println("beanToMap --> " + map);

        personBO = ObjectUtils.mapToObject(map, PersonBO.class);
        System.out.println("mapToBean --> " + personBO);
    }

    @Test
    public void testXml() {
        PersonBO personBO = getPersonBO();
        System.out.println("转换前 --> " + personBO);

        String xml = ObjectUtils.objectToXml(personBO);
        System.out.println("beanToXml --> ");
        System.out.println(xml);

        personBO = ObjectUtils.xmlToObject(xml, PersonBO.class);
        System.out.println("xmlToBean --> " + personBO);
    }

    List<String> l1 = Arrays.asList("a", "b", "c", "d");
    List<String> l2 = Arrays.asList("a", "b", "e");

    @Test
    public void diff() {
        List<String> diff = ListUtils.diff(l1, l2);
        System.out.println("diff -> " + diff);

        List<String> intersect = ListUtils.intersect(l1, l2);
        System.out.println("intersect -> " + intersect);

        List<String> union = ListUtils.union(l1, l2);
        System.out.println("union -> " + union);
    }

    @Test
    public void arrayToList() {
        String[] array = {"a","b"};
        List<String> list = ListUtils.arrayToList(array);
        System.out.println("arrayToList -> " + list);

        list.add("c");
        System.out.println("arrayToList -> " + list);

        Integer[] array1 = {1, 2};
        List<Integer> list1 = ListUtils.arrayToList(array1);
        System.out.println("arrayToList -> " + list1);

        list1.add(3);
        System.out.println("arrayToList -> " + list1);
    }

    @Test
    public void listToArray () {
        List<String> list1 = Arrays.asList("a", "b", "c");
        String[] array1 = ListUtils.listToArray(list1);
        for(String a : array1) {
            System.out.println("array1 -> " + a);
        }


        List<Integer> list2 = Arrays.asList(1, 2);
        Integer[] array2 = ListUtils.listToArray(list2);
        for(Integer a : array2) {
            System.out.println("array2 -> " + a);
        }
    }

    @Test
    public void listToSet() {
        List<String> list1 = Arrays.asList("a", "a", "c");
        Set<String> set1 = ListUtils.listToSet(list1);
        System.out.println("listToSet -> " + set1);

        list1 = ListUtils.setToList(set1);
        System.out.println("setToList -> " + list1);
    }

    @Test
    public void testAssert() {
        String str = "1";
        AssertUtils.isNotNumeric(str, "不能为空，且必须为数字");
    }
}
