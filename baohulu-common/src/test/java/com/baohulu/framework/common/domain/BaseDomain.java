package com.baohulu.framework.common.domain;

import com.alibaba.fastjson2.JSONObject;
import com.baohulu.framework.common.mapstruct.domain.Address;
import com.baohulu.framework.common.mapstruct.domain.PersonBO;
import com.baohulu.framework.common.mapstruct.domain.PersonEnum;

import java.util.*;

/**
 * @author heqing
 * @date 2022/10/21 11:43
 */
public class BaseDomain {

    protected PersonBO getPersonBO() {
        PersonBO personBO = new PersonBO();
        JSONObject remark = new JSONObject();
        remark.put("number", 1);
        remark.put("string", "test");
        Address address = new Address();
        address.setProvince("安徽");
        address.setCity("安庆");
        address.setArea("宿松");
        personBO.setId(1L);
        personBO.setName("贺小白");
        personBO.setSex(1);
        personBO.setDate(new Date());
        List<String> nameParts = new ArrayList<>();
        nameParts.add("贺");
        nameParts.add("小白");
        personBO.setNameParts(nameParts);
        Map<String, Object> map = new HashMap<>();
        map.put("number", 1);
        map.put("string", "test");
        personBO.setRemarkMap(map);
        personBO.setAddress(address);
        personBO.setType(PersonEnum.ADULT);
        personBO.setRemarkJson(remark.toJSONString());
        return personBO;
    }

    protected List<Address> getAddList() {
        Address add1 = new Address();
        add1.setProvince("安徽");
        add1.setCity("安庆");
        Address add2 = new Address();
        add2.setProvince("安徽");
        add2.setCity("合肥");
        Address add3 = new Address();
        add3.setCity("上海");
        Address add4 = new Address();
        add4.setCity("长沙");
        List<Address> addList = new ArrayList<>();
        addList.add(add1);
        addList.add(add2);
        addList.add(add3);
        addList.add(add4);
        return addList;
    }

    public RecipeVo getFrameworkDeta() {
        RecipeVo recipeVo = new RecipeVo();
        recipeVo.setRemark("this is remark");
        recipeVo.setRecipeCode("246512");
        recipeVo.setRecipeTime("2022-05-01 07:15:00");
        recipeVo.setPatientName("贺小白");
        recipeVo.setPatientSex("男");
        recipeVo.setAge("18");
        recipeVo.setAgeType("岁");
        recipeVo.setArchiveId("11111111");
        recipeVo.setPayType("自费");
        recipeVo.setAddress("安徽安庆市");
        recipeVo.setDepartmentName("全科");
        recipeVo.setDoctorDesc("测试");
        return recipeVo;
    }
}
