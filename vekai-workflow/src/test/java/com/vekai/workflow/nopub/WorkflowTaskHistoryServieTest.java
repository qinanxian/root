package com.vekai.workflow.nopub;

import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.workflow.BaseTest;
import com.vekai.workflow.entity.WorkflowTaskHistory;
import com.vekai.workflow.nopub.service.WorkflowEntityService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class WorkflowTaskHistoryServieTest extends BaseTest {

    @Autowired
    private BeanCruder beanCruder;
    @Autowired
    private WorkflowEntityService entityService;

    @Test
    public void testAddTaskHistory() {
        WorkflowTaskHistory workflowTaskHistory = new WorkflowTaskHistory();
        workflowTaskHistory.setTaskId("1");
        workflowTaskHistory.setProcId("ProcessInstance_1");
        workflowTaskHistory.setCreatedBy("alen");
        workflowTaskHistory.setRevision(1);
        workflowTaskHistory.setTaskName("taskName");

        entityService.addWorkflowTaskHistory(workflowTaskHistory);

        workflowTaskHistory = beanCruder
            .selectOne(WorkflowTaskHistory.class, "select * from WKFL_TASK_HIST where TASK_ID=1");

        Assert.assertEquals("ProcessInstance_1", workflowTaskHistory.getProcId());
    }

    @Test
    public void testGetTaskHistory() {
        WorkflowTaskHistory taskHistory = entityService.getWorkflowTaskHistory("1");
        Assert.assertEquals("taskName", taskHistory.getTaskName());

    }

    @Test
    public void testGetHistoryByProcId() {
        WorkflowTaskHistory taskHistory =
            entityService.getWorkflowTaskHistoryByProcId("ProcessInstance_1");

        Assert.assertEquals("alen", taskHistory.getCreatedBy());
    }

    @Test
    public void testUpdateTaskHistory() {
        WorkflowTaskHistory workflowTaskHistory = beanCruder
            .selectOne(WorkflowTaskHistory.class, "select * from WKFL_TASK_HIST where TASK_ID=1");
        workflowTaskHistory.setRevision(2);
        entityService.updateWorkflowTaskHistory(workflowTaskHistory);

        Assert.assertEquals(2,workflowTaskHistory.getRevision());
    }
}
