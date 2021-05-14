package com.vekai.workflow.controller.workflowProcessController;

import com.vekai.workflow.controller.WorkflowProcessController;
import com.vekai.workflow.controller.model.TaskNode;
import com.vekai.workflow.BaseTest;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Author: qyyao
 * @Description:
 * @Date: Created in 11:45 21/05/2018
 */
public class getProcInstCommetsTest extends BaseTest{

    @Autowired
    private WorkflowProcessController processController;
    @Autowired
    private HistoryService historyService;




    @Test
    public void getProcInstComments(){
        //3732541
        String procInstId ="4007536";
        TaskNode taskNode = processController.procInstComments(procInstId);
        Assert.assertNotNull(taskNode);
    }


    @Test
    public void testAllowTaskBackNode(){
        String procInstId ="4257593";
        List<HistoricActivityInstance> historicActivityInstances = processController.allowBackActivitys(procInstId);
        Assert.assertNotNull(historicActivityInstances);
    }
}
