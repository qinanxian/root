package com.vekai.workflow.service.task;

import cn.fisok.raw.kit.MapKit;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.workflow.BaseTest;
import com.vekai.workflow.model.enums.TaskStatusEnum;
import com.vekai.workflow.service.WorkflowTaskService;
import org.activiti.engine.impl.persistence.entity.CommentEntity;
import org.activiti.engine.impl.persistence.entity.CommentEntityManager;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.task.Comment;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class WorkflowTaskServiceTest  extends BaseTest{
    @Autowired
    private WorkflowTaskService workflowTaskService;
    @Autowired
    private BeanCruder beanCruder;

    private static String taskId = "1805012";

    /**
     * 任务签收
     */
    @Test
    public void test01() {
        workflowTaskService.claimTask(taskId, "001");
    }

    /**
     * 任务取消签收
     */
    @Test
    public void test11() {
        workflowTaskService.unClaimTask(taskId);
    }

    /**
     * 添加意见
     */
    @Test
    public void test12() {
        workflowTaskService.addComment(taskId, "001", TaskStatusEnum.TAKEADVICE, "请被征求人提供意见");
        workflowTaskService.addComment(taskId, "002", TaskStatusEnum.FEEDBACK, "意见反馈");
    }

    /**
     * 删除意见
     */
    @Test
    public void test02() {
        workflowTaskService.deleteComment("495001");
        workflowTaskService.deleteComment("495002");
    }

    /**
     * 更新意见
     */
    @Test
    public void test03() {
        workflowTaskService.updateComment("497502", "测试更新");
    }

    /**
     * 添加候选人
     */
    @Test
    public void test04() {
        workflowTaskService.addCandidateUser(taskId, "002");
    }

    /**
     * 删除候选人
     */
    @Test
    public void test05() {
        workflowTaskService.deleteCandidateUser(taskId, "002");
    }

    /**
     * 添加候选组
     */
    @Test
    public void test06() {
        workflowTaskService.addCandidateGroup("1112528", "300");
    }

    /**
     * 删除候选组
     */
    @Test
    public void test07() {
        workflowTaskService.deleteCandidateGroup(taskId, "300");
    }

    /**
     * 重设受理人
     */
    @Test
    public void test08() {
        workflowTaskService.changeAssignee(taskId, "003");
    }

    /**
     * 流程提交
     */
    @Test
    public void test09() {
        Map<String, Object> vars = new HashMap<String, Object>();
        String uid = UUID.randomUUID().toString().substring(0, 10);
        vars.put("ObjectId", uid);
        vars.put("ObjectType", "Test");
        vars.put("Summary", "xx客户-简单串行流程-" + uid);
        workflowTaskService.commit(taskId, vars, "005", "任务提交");
    }

    @Test
    public void testProcAbolish() {
        workflowTaskService.abolish(taskId, "180103", "流程作废测试");

    }

    @Test
    public void testProcReject() {
        workflowTaskService.reject(taskId, "667504", "流程否决测试");
    }

    @Test
    public void test20() {
        workflowTaskService.backFront("675004", "Task-1", "cs11", "驳回");
    }

    @Test
    public void test21() {
        workflowTaskService.backTrack("682504", "Task-1", "cs11", "退回修改");
    }


    @Test
    public void test30() {
        workflowTaskService.retrieve("2410010", "xmzuo", "测试任务撤回");
    }



    @Test
    public void testGetAllComments(){
        String taskId ="3172544";

        String sql ="SELECT ID_ as id,TYPE_ as type,TIME_ as time,USER_ID_ as userId,TASK_ID_ as taskId,PROC_INST_ID_ as processInstanceId,ACTION_ as action,MESSAGE_ as message,FULL_MSG_  as fullMessage" +
            " FROM ACT_HI_COMMENT WHERE ACTION_ = 'AddComment' AND TASK_ID_ =:taskId ORDER BY TIME_" + " ASC";
        List<CommentEntity> commentEntityList =
            beanCruder.selectList(CommentEntity.class, sql, MapKit.mapOf("taskId", taskId));
        Assert.assertNotNull(commentEntityList);

        CommentEntityManager manager = new CommentEntityManager();

        List<Comment> commentList = manager.findCommentsByTaskIdAndType(taskId, "AddComment");
        Assert.assertNotNull(commentList);
    }


    @Test
    public void testGetAllForwardTaskNode(){
        String procInstId ="4257577";
        List<ActivityImpl> allForwardActivities =
            workflowTaskService.getAllForwardActivities(procInstId);

        Assert.assertNotNull(allForwardActivities);
    }
}
