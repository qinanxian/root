package com.vekai.workflow.service.def;

import com.vekai.workflow.BaseTest;
import com.vekai.workflow.nopub.service.WorkflowDefinitionService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class WorkflowDefinitionServiceTest extends BaseTest {

    @Autowired
    private WorkflowDefinitionService workflowDefinitionService;

    @Test
    public void test01(){
        Long count = workflowDefinitionService.deployedCount();
        Assert.assertNotNull(count);
    }

    @Test
    public void test02(){
        String xmlStr = workflowDefinitionService.getDefinitionModelAsXml("17550");
        Assert.assertNotNull(xmlStr);
        Assert.assertTrue(xmlStr.contains("xmlns"));
    }

    @Test
    public void test03(){
        byte[] bytes = workflowDefinitionService.getDefinitionModelAsBytes("19108");
        Assert.assertNotNull(bytes);
    }

    @Test
    public void test04(){
        String str = workflowDefinitionService.deployDefinitionModel("19108");
        Assert.assertNotNull(str);
    }

    @Test
    public void test05(){
        //workflowDefinitionService.deleteDefinitionModel("17550");
    }
}
