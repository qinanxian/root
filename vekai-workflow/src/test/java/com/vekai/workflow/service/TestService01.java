package com.vekai.workflow.service;

import com.vekai.workflow.exception.WorkflowException;
import com.vekai.workflow.BaseTest;
import com.vekai.workflow.model.ProcInstance;
import com.vekai.workflow.model.ProcTask;
import com.vekai.workflow.model.enums.TaskStatusEnum;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestService01 extends BaseTest {
    @Autowired
    private WorkflowProcService workflowProcService;
    @Autowired
    private WorkflowTaskService workflowTaskService;
    @Autowired
    private HistoryService historyService;

    /**
     * 测试点:流程提交
     */
    @Test
    public void test01() {
        Map<String, Object> vars = new HashMap<String, Object>();
        /**
         * 流程启动
         */
        vars.put("ObjectId","1010");
        vars.put("ObjectType","oooo");
        vars.put("Summary","haha");
        ProcInstance procInstance = workflowProcService
                .start("parallelgatewayTest01", vars, "kermit");
        Assert.assertNotNull(procInstance);
//        String procInstId = procInstance.getId();
//
//        List<ProcTask> taskList = workflowTaskService.getLatestTasks(procInstId);
//        while (null != taskList && !taskList.isEmpty()) {
//            taskList = workflowTaskService.getLatestTasks(procInstId);
//            taskList.stream().forEach(procTask ->
//                    workflowTaskService.commit(procTask.getId(), vars, "gonzo","意见")
//            );
//        }
//
//        boolean isFinished = workflowProcService.isFinished(procInstId);
//        Assert.assertEquals(true,isFinished);
    }

    /**
     * 测试点:流程带参数(判断网关)提交
     */
    @Test
    public void test02() {
        Map<String, Object> vars = new HashMap<String, Object>();
        vars.put("money", 1000);
        vars.put("size", 20);
        String procInstId = "160005";

        List<ProcTask> taskList = workflowTaskService.getLatestTasks(procInstId);
        while (null != taskList && !taskList.isEmpty()) {
            taskList = workflowTaskService.getLatestTasks(procInstId);
            taskList.stream().forEach(procTask ->
                    workflowTaskService.commit(procTask.getId(), vars, "gonzo", "意见")
            );
        }

        boolean isFinished = workflowProcService.isFinished(procInstId);
        Assert.assertEquals(true,isFinished);
    }

    /**
     * 测试点:流程暂停和激活
     */
    @Test
    public void test03() {
        Map<String,Object> vars = new HashMap<>();
        ProcInstance procInstance = workflowProcService.start("parallelgatewayTest01",vars,"kermit");
        Assert.assertNotNull(procInstance);
        String procInstId = procInstance.getId();

        workflowProcService.suspend(procInstId);
        boolean isSuspended = workflowProcService.isSuspended(procInstId);
        Assert.assertTrue(isSuspended);

        workflowProcService.activate(procInstId);
        boolean isActive = workflowProcService.isSuspended(procInstId);
        Assert.assertFalse(isActive);
    }

    /**
     * 测试点:流程作废
     */
    @Test(expected = WorkflowException.class)
    public void test04() {
        Map<String,Object> vars = new HashMap<>();
        ProcInstance procInstance = workflowProcService.start("parallelgatewayTest01",vars,"kermit");
        Assert.assertNotNull(procInstance);
        String procInstId = procInstance.getId();
        workflowTaskService.abolish(procInstId,"fozzie", "意见");
    }

    /**
     * 测试点:流程驳回,退回修改
     */
    @Test
    public void test05() {
        Map<String,Object> vars = new HashMap<>();
        ProcInstance procInstance = workflowProcService.start("parallelgatewayTest01",vars,"kermit");
        Assert.assertNotNull(procInstance);
        String procInstId = procInstance.getId();

        ProcTask firstTask = workflowTaskService.getLatestTask(procInstId);
        workflowTaskService.commit(firstTask.getId(),vars,"gonzo", "意见");
        ProcTask secondTask = workflowTaskService.getLatestTask(procInstId);
        //驳回
        workflowTaskService.jumpBack(secondTask.getId(),firstTask.getTaskDefinitionKey(),"cs11", TaskStatusEnum.BACKORIGIN, "意见");
        ProcTask latestTask = workflowTaskService.getLatestTask(procInstId);
        Assert.assertEquals(firstTask.getTaskDefinitionKey(),latestTask.getTaskDefinitionKey());

        //退回
        ProcTask thirdTask = workflowTaskService.getLatestTask(procInstId);
        workflowTaskService.commit(thirdTask.getId(),vars,"gonzo", "意见");
        ProcTask fourthTask = workflowTaskService.getLatestTask(procInstId);
        workflowTaskService.jumpBack(fourthTask.getId(),firstTask.getTaskDefinitionKey(),"cs11", TaskStatusEnum.BACKTRACK,"意见");
        ProcTask currentTask = workflowTaskService.getLatestTask(procInstId);
        Assert.assertEquals(firstTask.getTaskDefinitionKey(),currentTask.getTaskDefinitionKey());
    }
    /**
     * 测试点：流程否决
     */
    @Test
    public void test06(){
        Map<String,Object> vars = new HashMap<>();
        ProcInstance procInstance = workflowProcService.start("parallelgatewayTest01",vars,"kermit");
        Assert.assertNotNull(procInstance);
        String procInstId = procInstance.getId();

        ProcTask firstTask = workflowTaskService.getLatestTask(procInstId);
        workflowTaskService.commit(firstTask.getId(),vars,"gonzo", "意见");

        ProcTask secondTask = workflowTaskService.getLatestTask(procInstId);
        workflowTaskService.reject(secondTask.getId(),"gonzo", "意见");
        boolean isFinished = workflowProcService.isFinished(procInstId);
        Assert.assertTrue(isFinished);
        HistoricTaskInstance taskInstance = historyService.createHistoricTaskInstanceQuery().
                processInstanceId(procInstId).taskDefinitionKey(secondTask.getTaskDefinitionKey()).singleResult();
        Assert.assertEquals("reject",taskInstance.getDeleteReason());

    }


    @Test
    public void test16(){
        System.out.println(TaskStatusEnum.COMPLETED.toString());
        System.out.println(TaskStatusEnum.COMPLETED.getName());

    }
}



