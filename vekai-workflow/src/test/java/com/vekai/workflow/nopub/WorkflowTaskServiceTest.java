package com.vekai.workflow.nopub;

import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.workflow.BaseTest;

import com.vekai.workflow.entity.WorkflowTask;
import com.vekai.workflow.nopub.service.WorkflowEntityService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class WorkflowTaskServiceTest extends BaseTest{

    @Autowired
    private BeanCruder beanCruder;
    @Autowired
    private WorkflowEntityService entityService;



    @Test
    public void testAddTaskService(){
        WorkflowTask workflowTask =new WorkflowTask();
        workflowTask.setTaskId("3");
        workflowTask.setProcId("processInstance_3");
        workflowTask.setRevision(1);
        workflowTask.setTaskName("taskName_2");
        workflowTask.setCreatedBy("alen");
        //workflowTask.setFinishTime(new Date(2017-12-29));
        entityService.addWorkflowTask(workflowTask);

        workflowTask =
            beanCruder.selectOne(WorkflowTask.class, "select * from from WKFL_TASK where TASK_ID='1'");
        Assert.assertEquals("taskName",workflowTask.getTaskName());

    }


    @Test
    public void testGetTaskService(){

        WorkflowTask taskService = entityService.getWorkflowTask("1");

        Assert.assertEquals("processInstance_1",taskService.getProcId());
    }



    @Test
    public void testGetTaskServiceByProcId(){
        List<WorkflowTask> workflowTasks = entityService.getWorkflowTaskByProcId("processInstance_3");
        Assert.assertNotNull(workflowTasks);
    }




}
