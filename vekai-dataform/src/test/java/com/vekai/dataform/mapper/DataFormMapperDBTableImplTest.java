package com.vekai.dataform.mapper;

import com.vekai.dataform.model.DataForm;
import com.vekai.dataform.BaseTest;
import cn.fisok.raw.kit.JSONKit;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class DataFormMapperDBTableImplTest extends BaseTest {
    @Autowired
    private DataFormMapper dataFormMapper;

    public DataForm getDataForm(){
        return DataFormMapperTest.demoPersonInfo();
    }


    @Test
    public void testSave(){
        DataForm dataForm = getDataForm();
        dataForm.setPack("demo");
        dataForm.setCode("ExamplePerson");
        dataFormMapper.save(dataForm);
    }

    @Test
    public void testGetDataForm(){
        DataForm dataForm = dataFormMapper.getDataForm("demo","ExamplePerson");
        System.out.print(JSONKit.toJsonString(dataForm));
    }


    @Test
    public void testGetDataFormsByPack(){
        List<DataForm> dataForms = dataFormMapper.getDataFormsByPack("demo");
        System.out.print(JSONKit.toJsonString(dataForms));
    }


    @Test
    public void testGetAllDataForms(){
        List<DataForm> dataForms = dataFormMapper.getAllDataForms();
        System.out.print(JSONKit.toJsonString(dataForms));
    }

    @Test
    public void testDeleteDataForm(){
        int result = dataFormMapper.delete("demo-ExamplePerson");
        Assert.assertNotEquals(0,result);
    }
}
