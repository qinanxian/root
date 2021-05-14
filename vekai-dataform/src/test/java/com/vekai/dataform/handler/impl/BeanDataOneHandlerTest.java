package com.vekai.dataform.handler.impl;

import com.vekai.dataform.model.DataForm;
import com.vekai.dataform.BaseTest;
import com.vekai.dataform.TestData;
import com.vekai.dataform.entity.Person;
import com.vekai.dataform.mapper.DataFormMapperTest;
import cn.fisok.raw.kit.BeanKit;
import cn.fisok.raw.kit.JSONKit;
import cn.fisok.raw.kit.MapKit;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class BeanDataOneHandlerTest extends BaseTest {

    @Autowired
    protected BeanDataOneHandler<Object> handler;

    @Test
    public void queryTest(){
        DataForm dataForm = DataFormMapperTest.demoPersonInfo();
        dataForm.getQuery().setFrom("DEMO_PERSON ");
        dataForm.getQuery().setWhere("CODE = :code");
        dataForm.setDataModel(Person.class.getName());

        Map<String,?> param = MapKit.mapOf("code","P701");
        Object ret = handler.query(dataForm,param);
        System.out.println(JSONKit.toJsonString(ret));



    }

    @Test
    public void saveTest() throws ClassNotFoundException {
        DataForm dataForm = DataFormMapperTest.demoPersonInfo();
        dataForm.setDataModel(Person.class.getName());

        Object object = BeanKit.map2Bean(TestData.dataList.get(0),Class.forName(Person.class.getName()));
        handler.delete(dataForm,object);
        handler.insert(dataForm,object);
        handler.update(dataForm,object);
        handler.save(dataForm,object);
    }
}
