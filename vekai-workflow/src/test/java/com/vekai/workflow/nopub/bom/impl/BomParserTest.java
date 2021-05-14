package com.vekai.workflow.nopub.bom.impl;

import com.vekai.workflow.service.WorkflowProcService;
import com.vekai.workflow.BaseTest;
import com.vekai.workflow.nopub.bom.BomContext;
import com.vekai.workflow.nopub.bom.BomLoader;
import com.vekai.workflow.nopub.bom.BomObject;
import com.vekai.workflow.nopub.bom.BomParser;
import com.vekai.workflow.nopub.service.WorkflowDefinitionService;
import com.vekai.workflow.service.WorkflowTaskService;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BomParserTest extends BaseTest{
    @Autowired
    private BomLoader bomLoader;
    @Autowired
    private BomParser bomParser;
    @Autowired
    private WorkflowProcService workflowProcService;
    @Autowired
    private WorkflowTaskService workflowTaskService;
    @Autowired
    private WorkflowDefinitionService definitionService;

    @Test
    public void test01(){
        //1.加载BOM模型
        BomObject bom = bomLoader.load("judgeNetWork");
        //2.设置上下文原始业务参数
        BomContext ctx = new BomContext();

        ctx.putValue("user.id",new UserEntity("1"));
        ctx.putValue("judge.money",500);
        ctx.putValue("judge.size",60);

        //3.解析规则
        bomParser.parse(bom,ctx);
        //4.查看结果
        Map<String, Object> stringObjectMap = bom.flatValue();
        System.out.println(stringObjectMap.toString());
    }

    @Test
    public void testt(){
        definitionService.deployDefinitionModel("1070001");
    }

    @Test
    public void test02(){
        Map<String, Object> vars = new HashMap<String, Object>();
        String uid= UUID.randomUUID().toString().substring(0,10);
        vars.put("ObjectId", uid);
        vars.put("ObjectType","Test");
        vars.put("Summary","xx客户-简单串行流程-"+uid);
        workflowProcService.start("judgeNetWork",vars,"0001");
    }

    @Test
    public void test03(){
        Map<String, Object> vars = new HashMap<String, Object>();
        String uid= UUID.randomUUID().toString().substring(0,10);
        vars.put("ObjectId", uid);
        vars.put("ObjectType","Test");
        vars.put("Summary","xx客户-简单串行流程-"+uid);
        workflowTaskService.commit("1385007",vars,"0001","测试参数");
    }
}
