package com.vekai.workflow.nopub;

import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.workflow.BaseTest;
import com.vekai.workflow.entity.WorkflowComment;
import com.vekai.workflow.nopub.service.WorkflowEntityService;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class WorkflowCommentTest extends BaseTest {
    @Autowired
    private BeanCruder beanCruder;
    @Autowired
    private WorkflowEntityService workflowEntityService;

    @Test
    public void test01AddWorkflowComment(){
        WorkflowComment workflowComment = new WorkflowComment();
        workflowComment.setCommentId("1");
        workflowComment.setTaskId("111");
        workflowComment.setCommentType("type1");
        workflowComment.setMessage("message1");
        workflowComment.setUserId("222");
        workflowComment.setAction("action1");
        workflowComment.setActionIntro("actionIntro1");
        workflowEntityService.addWorkflowComment(workflowComment);
        WorkflowComment workflowCommentTest = beanCruder
                .selectOne(WorkflowComment.class,"select * from WKFL_COMMENT where COMMENT_ID='1'");
        Assert.assertEquals("type1",workflowCommentTest.getCommentType());
    }

    @Test
    public void test02GetWorkflowComment(){
        WorkflowComment workflowComment = workflowEntityService.getWorkflowComment("1");
        Assert.assertNotNull(workflowComment);
        Assert.assertEquals("type1",workflowComment.getCommentType());
    }

//    @Test
//    public void test03GetWorkflowCommentByTaskId(){
//        WorkflowComment workflowComment = workflowEntityService.getWorkflowCommentByTaskId("111");
//        Assert.assertNotNull(workflowComment);
//        Assert.assertEquals("type1",workflowComment.getCommentType());
//    }

    @Test
    public void test04UpdateWorkflowComment(){
        WorkflowComment workflowComment = workflowEntityService.getWorkflowComment("1");
        workflowComment.setMessage("message2");
        workflowEntityService.updateWorkflowComment(workflowComment);

        WorkflowComment workflowCommentTest = workflowEntityService.getWorkflowComment("1");
        Assert.assertEquals("message2",workflowCommentTest.getMessage());
    }

    @Test
    public void test05DeleteWorkflowComment(){
        workflowEntityService.deleteWorkflowComment("1");
        WorkflowComment workflowCommentTest = beanCruder
                .selectOne(WorkflowComment.class,"select * from WKFL_COMMENT where COMMENT_ID='1'");
        Assert.assertNull(workflowCommentTest);
    }
}
