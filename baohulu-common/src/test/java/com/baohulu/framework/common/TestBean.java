package com.baohulu.framework.common;

import com.baohulu.framework.common.domain.BaseDomain;
import com.baohulu.framework.common.mapstruct.PersonMapstructConverter;
import com.baohulu.framework.common.mapstruct.domain.Address;
import com.baohulu.framework.common.mapstruct.domain.PersonBO;
import com.baohulu.framework.common.mapstruct.domain.PersonDTO;
import com.baohulu.framework.common.utils.BeanUtils;
import com.baohulu.framework.common.utils.ListUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author heqing
 * @date 2022/10/19 10:32
 */
public class TestBean extends BaseDomain {

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
        List<Address> newAddList = addList.stream().filter(BeanUtils.distinctByKey(d -> d.getProvince())).collect(Collectors.toList());
        System.out.println("过滤后 -->" + newAddList);
    }


    @Test
    public void getProp() {
        PersonBO personBO = getPersonBO();
        Address address = (Address) BeanUtils.getProp(personBO, "address");
        System.out.println("--> " + address);
    }

    @Test
    public void testMap() {
        PersonBO personBO = getPersonBO();
        System.out.println("转换前 --> " + personBO);

        Map<String, Object> map = BeanUtils.beanToMap(personBO);
        System.out.println("beanToMap --> " + map);

        personBO = BeanUtils.mapToBean(map, PersonBO.class);
        System.out.println("mapToBean --> " + personBO);
    }

    @Test
    public void testXml() {
        PersonBO personBO = getPersonBO();
        System.out.println("转换前 --> " + personBO);

        String xml = BeanUtils.beanToXml(personBO);
        System.out.println("beanToXml --> ");
        System.out.println(xml);

        personBO = BeanUtils.xmlToBean(xml, PersonBO.class);
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
}
