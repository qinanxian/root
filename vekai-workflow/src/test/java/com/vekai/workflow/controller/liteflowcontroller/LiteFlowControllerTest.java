package com.vekai.workflow.controller.liteflowcontroller;

import com.vekai.workflow.controller.LiteFlowController;
import com.vekai.workflow.BaseTest;
import com.vekai.workflow.liteflow.entity.LiteflowResource;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * @Author: qyyao
 * @Description:
 * @Date: Created in 14:59 23/03/2018
 */
public class LiteFlowControllerTest extends BaseTest{


    @Autowired
    private LiteFlowController liteFlowController;


    @Test
    public void testGetResourceList(){
        String procDefKey ="Simple";
        List<LiteflowResource> resourceList = liteFlowController.getResourceList(procDefKey);
        Assert.assertNotNull(resourceList);
    }

    @Test
    public void testGetResourceGroup(){
        String procDefKey ="Simple";
        Map<String, List<LiteflowResource>>  resourcesGroup = liteFlowController.getResourcesGroup(procDefKey);
        Assert.assertNotNull(resourcesGroup);
    }
}
