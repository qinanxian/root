package com.vekai.workflow.service;

import cn.fisok.raw.kit.JSONKit;
import com.vekai.workflow.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkflowPageParamTest extends BaseTest{
    @Autowired
    private WorkflowPageParamService pageParamService;

    String procInstId ="1457513";

    @Test
    public void testGetParams(){
        Map<String, Object> pageParam = pageParamService.getPageParam(procInstId);
        System.out.println(pageParam);

    }

    @Test
    public void testUpdateParams(){
        Map<String, Object> pageParam = new HashMap<>();
        pageParam.put("userId","xmzuo");
        pageParamService.updatePageParam(procInstId, pageParam);

    }

    @Test
    public void testAppendParams(){
        Map<String, Object> pageParam = new HashMap<>();
        pageParam.put("userId","xxxx");
        pageParam.put("test1","xxxx");
        pageParamService.appendPageParam(procInstId, pageParam);

    }

    @Test
    public void test99(){
        Map<String, Object> res = new HashMap<>();
        List<String> users = new ArrayList<>();
        List<String> roles = new ArrayList<>();
        Integer status = new Integer(0);

        res.put("users", users);
        res.put("roles", roles);
        res.put("status", status);

        users.add("test01");
        users.add("test02");

        roles.add("role01");
        roles.add("role02");

        status = 1;

        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        System.out.println(JSONKit.toJsonString(res));

        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++");
    }

}
