package com.vekai.workflow.service.proc;

import com.vekai.workflow.BaseTest;
import com.vekai.workflow.model.ProcInstance;
import com.vekai.workflow.service.WorkflowProcService;
import com.vekai.workflow.service.WorkflowTaskService;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.task.TaskDefinition;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class WorkflowProcServiceTest extends BaseTest {
    @Autowired
    private WorkflowProcService workflowProcService;
    @Autowired
    private WorkflowTaskService workflowTaskService;

    @Test
    public void test01() {
        Map<String, Object> vars = new HashMap<String, Object>();
        String uid=UUID.randomUUID().toString().substring(0,10);
        vars.put("ObjectId", uid);
        vars.put("ObjectType","Test");
        vars.put("Summary","xx客户-简单串行流程-"+uid);
        /**
         * 流程启动
         */
        ProcInstance procInstance = workflowProcService.start("Simple", vars, "cs11");
    }


    @Test
    public void test02() {
        for (TaskDefinition taskDefinition : workflowProcService.getTaskDefinitions("1455001")) {
            System.out.println(taskDefinition);
        }
    }

    @Test
    public void test03() {
        List<TaskDefinition> taskDefinitions = new ArrayList<>();
        Set<PvmActivity> nextUserActivities = workflowTaskService.getNextUserActivities("2782619");
        for (PvmActivity pvmActivity : nextUserActivities) {
            System.out.println(pvmActivity);
            TaskDefinition taskDefinition = (TaskDefinition) pvmActivity.getProperty("taskDefinition");
            taskDefinitions.add(taskDefinition);
        }
        Set<PvmActivity> ns = workflowTaskService.getNextUserActivities("2782520");
        for (PvmActivity pvmActivity : ns) {
            System.out.println(ns);
            TaskDefinition taskDefinition = (TaskDefinition) pvmActivity.getProperty("taskDefinition");
            taskDefinitions.add(taskDefinition);
        }
        TaskDefinition taskDefinition0 = taskDefinitions.get(0);
        TaskDefinition taskDefinition1 = taskDefinitions.get(1);

    }

    @Test
    public void getActivityProcess(){
        String procInstId ="3357580";
        String taskId ="3482505";
        List<String> nodePath = workflowProcService.getWorkflowProcessDepth(procInstId,taskId);
        Assert.assertNotNull(nodePath);
        System.out.println("所有流程节点最深路径长度为: "+nodePath.get(0));
        System.out.println("当前流程节点最深路径长度为: "+nodePath.get(1));
    }
}
