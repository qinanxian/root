package com.vekai.workflow.service.identity;

import com.vekai.workflow.BaseTest;
import com.vekai.workflow.nopub.service.WorkflowIdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class WorkflowIdentityServiceTest extends BaseTest {

    @Autowired
    private WorkflowIdentityService workflowIdentityService;

    @Test
    public void test01(){
        User user = workflowIdentityService.getUser("kermit");
        Assert.assertEquals("Kermit",user.getFirstName());
    }

    @Test
    public void test02(){
        List<String> userIds = new ArrayList<>();
        userIds.add("fozzie");
        userIds.add("gonzo");
        List<User> userList = workflowIdentityService.getUsers(userIds);
        Assert.assertEquals(2,userList.size());
    }

    @Test
    public void test03(){
        Group group = workflowIdentityService.getGroup("engineering");
        Assert.assertEquals("Engineering",group.getName());
    }

    @Test
    public void test04(){
        List<User> users = workflowIdentityService.getGroupUsers("engineering");
        Assert.assertEquals(2,users.size());
    }
}
