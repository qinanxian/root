package com.vekai.dataform.handler.impl;

import com.vekai.dataform.BaseTest;
import com.vekai.dataform.TestData;
import com.vekai.dataform.mapper.DataFormMapperTest;
import com.vekai.dataform.model.DataForm;
import com.vekai.dataform.model.DataFormElement;
import com.vekai.dataform.model.types.ElementDataType;
import cn.fisok.raw.kit.JSONKit;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.PaginResult;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class MapDataListHandlerTest extends BaseTest {
    @Autowired
    protected MapDataListHandler handler;

    @Test
    public void queryTest(){
        DataForm dataForm = DataFormMapperTest.demoPersonInfo();
        dataForm.getQuery().setFrom("DEMO_PERSON ");
        dataForm.getQuery().setWhere("CODE = :code");
        //把code作一个替换处理
        DataFormElement element = dataForm.getElement("code");
        element.setCode("code1");
        dataForm.addElement(element);
        dataForm.removeElement("code");

        Map<String,?> paramMap = MapKit.mapOf("code","P1002");
        PaginResult<MapObject> ret = handler.query(dataForm,paramMap,null,null,15,0);

        Assert.assertTrue(ret.getDataList().size()>1);

        System.out.println(JSONKit.toJsonString(ret));

    }

    @Test
    public void saveTest(){
        DataForm dataForm = DataFormMapperTest.demoPersonInfo();
        dataForm.getQuery().setFrom("DEMO_PERSON ");
        dataForm.getQuery().setWhere("CODE = :code");
        dataForm.addElement(new DataFormElement("id","ID","唯一编号",null, ElementDataType.Integer).setPrimaryKey(true));
        dataForm.getElement("code").setPrimaryKey(false);


        //把code作一个替换处理
//        DataFormElement element = dataForm.getElement("code");
//        element.setCode("code1");
//        dataForm.addElement(element);
//        dataForm.removeElement("code");

        handler.delete(dataForm, TestData.dataList);
        handler.insert(dataForm,TestData.dataList);
        handler.update(dataForm,TestData.dataList);
        handler.save(dataForm,TestData.dataList);

    }
}
