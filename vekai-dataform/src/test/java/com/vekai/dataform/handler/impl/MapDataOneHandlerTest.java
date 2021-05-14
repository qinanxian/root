package com.vekai.dataform.handler.impl;

import com.vekai.dataform.BaseTest;
import com.vekai.dataform.TestData;
import com.vekai.dataform.mapper.DataFormMapperTest;
import com.vekai.dataform.model.DataForm;
import com.vekai.dataform.model.DataFormElement;
import com.vekai.dataform.model.types.ElementDataType;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.lang.MapObject;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MapDataOneHandlerTest extends BaseTest {

    @Autowired
    protected MapDataOneHandler handler;

    @Test
    public void saveTest(){
        DataForm dataForm = DataFormMapperTest.demoPersonInfo();
        dataForm.addElement(new DataFormElement("id","ID","唯一编号",null, ElementDataType.Integer));
        MapObject dataObject = TestData.dataList.get(0);
        handler.delete(dataForm,dataObject);
        handler.save(dataForm,dataObject);
        MapObject row = handler.query(dataForm, MapKit.mapOf("code","jt"));
        Assert.assertNotNull(row);
        handler.delete(dataForm,dataObject);
        handler.insert(dataForm,dataObject);
        handler.update(dataForm,dataObject);
//        handler.delete(dataForm,dataObject);
    }
}
