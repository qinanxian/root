package com.vekai.dataform.handler.impl;

import com.vekai.dataform.model.DataForm;
import com.vekai.dataform.BaseTest;
import com.vekai.dataform.TestData;
import com.vekai.dataform.entity.Person;
import com.vekai.dataform.mapper.DataFormMapperTest;
import cn.fisok.raw.kit.JSONKit;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.sqloy.core.PaginResult;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BeanDataListHandlerTest extends BaseTest {

    @Autowired
    protected BeanDataListHandler<Object> handler;

    @Test
    public void queryTest(){
        DataForm dataForm = DataFormMapperTest.demoPersonInfo();
        dataForm.getQuery().setFrom("DEMO_PERSON ");
        dataForm.getQuery().setWhere("CODE > :code");
        dataForm.setDataModel(Person.class.getName());

        Map<String,?> param = MapKit.mapOf("code","P102");
        PaginResult<Object> ret = handler.query(dataForm,param,null,null,15,0);
        System.out.println(JSONKit.toJsonString(ret));
    }

    @Test
    public void saveTest(){
        DataForm dataForm = DataFormMapperTest.demoPersonInfo();
        dataForm.getQuery().setFrom("DEMO_PERSON ");
        dataForm.getQuery().setWhere("CODE = :code");
        dataForm.setDataModel(Person.class.getName());

        List<Object> persons = new ArrayList<Object>();
        persons.addAll(TestData.personList);
//        TestData.personList;
        handler.delete(dataForm, persons);
        handler.insert(dataForm,persons);
        handler.update(dataForm,persons);
        handler.save(dataForm,persons);
    }
}
