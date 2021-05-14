package com.vekai.workflow.liteflow.service.impl;

import com.vekai.workflow.liteflow.service.LiteFlowService;
import com.vekai.workflow.BaseTest;
import com.vekai.workflow.entity.WorkflowProc;
import com.vekai.workflow.entity.WorkflowTask;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: qyyao
 * @Description:
 * @Date: Created in 17:12 13/03/2018
 */
public class LiteFlowServiceImplTest extends BaseTest {

    @Autowired
    private LiteFlowService liteFlowService;

    private String expr = "xmzuo→syang|r:001→xmzuo&r:001→syang|r:001";

    @Test
    public void testStart() {
        String userId = "qyyao";
        String procDefKey ="简式流程测试";
        Map<String, Object> variables = new HashMap<>();
        variables.put("ObjectId", "123");
        variables.put("ObjectType", "typeTest");
        variables.put("Summary", "summary");
        WorkflowProc workflowProc = liteFlowService.start(expr,procDefKey, variables, userId);

        Assert.assertNotNull(workflowProc);
    }

    @Test
    public void testCommitWithComment() {
        String taskId = "T000000402";
        String userId = "syang";
        List<WorkflowTask> workflowTaskList = liteFlowService.commit(taskId, null, userId, "简式流程审核");
        Assert.assertNotNull(workflowTaskList);
    }


    @Test
    public void testBackOrigin(){
        String taskId = "T000000397";
        String userId ="syang";
        WorkflowTask workflowTask = liteFlowService.backOrigin(taskId, "T1", userId, "流程驳回测试");
        Assert.assertNotNull(workflowTask);
    }
}
