package com.vekai.workflow.nopub.service;

import cn.fisok.raw.kit.MapKit;
import cn.fisok.sqloy.core.BeanCruder;
import cn.fisok.sqloy.core.BeanQuery;
import com.vekai.workflow.entity.*;
import com.vekai.workflow.model.ProcComment;
import com.vekai.workflow.model.WorkflowConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * 流程实例类数据库读写服务类
 *
 * @auther 数科研发部流程研发小组
 * @date 2017-12-29
 */
@Service
public class WorkflowEntityService {
    @Autowired
    private BeanCruder beanCruder;
    @Autowired
    private BeanQuery dataQuery;


    public void addWorkflowProc(WorkflowProc workflowProc) {
        beanCruder.insert(workflowProc);
    }


    public void deleteWorkflowProc(String procId) {
        beanCruder
            .execute("delete from " + WorkflowConstant.WKFL_PROC_TABLE + " where PROC_ID=:id",
                MapKit.mapOf("id", procId));
    }

    public WorkflowProc getWorkflowProc(String procId) {
        Map<String, ?> vars = MapKit.mapOf("id", procId);
        return beanCruder
            .selectOne(WorkflowProc.class,
                "select * from " + WorkflowConstant.WKFL_PROC_TABLE + " where PROC_ID=:id", vars);
    }

    public WorkflowProc getWorkflowProcByProcInstId(String processInstanceId) {
        Map<String, ?> vars =
            MapKit.mapOf("processInstanceId", processInstanceId);
        return beanCruder
            .selectOne(WorkflowProc.class,
                "select * from " + WorkflowConstant.WKFL_PROC_TABLE + " where PROC_INST_ID=:processInstanceId",
                vars);
    }

    /**
     * 通过 objectType,objectId 组成的联合主键查询
     *
     * @param objectType
     * @param objectId
     * @return
     */

    public WorkflowProc getWorkflowProcByObjTypeAndObjId(String objectType, String objectId) {
        Map<String, String> stringMap =
            MapKit.mapOf("objectType", objectType, "objectId", objectId);
        return beanCruder.selectOne(WorkflowProc.class,
            "select * from " + WorkflowConstant.WKFL_PROC_TABLE
                + " where OBJECT_TYPE=:objectType and OBJECT_ID=:objectId",
            stringMap);
    }

    /**
     * 更新workflowCase表中数据
     *
     * @param workflowProc
     */

    public void updateWorkflowProc(WorkflowProc workflowProc) {
        beanCruder.update(workflowProc);
    }


    public void addWorkflowTask(WorkflowTask workflowTask) {
        beanCruder.insert(workflowTask);
    }


    public void deleteWorkflowTask(String taskId) {
        beanCruder.execute("delete from " + WorkflowConstant.WKFL_TASK_TABLE + " where TASK_ID=:taskId",
            MapKit.mapOf("taskId", taskId));
    }

    /**
     * 通过主键taskId 查找
     *
     * @param taskId
     * @return
     */
    public WorkflowTask getWorkflowTask(String taskId) {
        Map<String, ?> vars = MapKit.mapOf("taskId", taskId);
        return beanCruder
            .selectOne(WorkflowTask.class,
                "select * from " + WorkflowConstant.WKFL_TASK_TABLE + " where TASK_ID=:taskId ", vars);
    }

    /**
     * 通过 processInstanceId 查找
     *
     * @param processInstanceId
     * @return
     */
    public List<WorkflowTask> getWorkflowTaskByProcId(String processInstanceId) {
        Map<String, ?> vars = MapKit.mapOf("id", processInstanceId);
        return beanCruder
            .selectList(WorkflowTask.class,
                "select * from " + WorkflowConstant.WKFL_TASK_TABLE + " where PROC_ID=:id ", vars);
    }

    /**
     * 更新workflow_task表中数据
     *
     * @param workflowTask
     */

    public void updateWorkflowTask(WorkflowTask workflowTask) {
        beanCruder.update(workflowTask);
    }


    public void addWorkflowTaskHistory(WorkflowTaskHistory workflowTaskHistory) {
        beanCruder.insert(workflowTaskHistory);
    }


    public void deleteWorkflowTaskHistory(String taskId) {
        beanCruder.execute("delete from " + WorkflowConstant.WKFL_TASK_HIST_TABLE + " where TASK_ID=:id",
            MapKit.mapOf("id", taskId));
    }


    public void deleteTasks(String procInstId) {
        beanCruder.execute("delete from " + WorkflowConstant.WKFL_TASK_TABLE + " where PROC_ID=:id",
            MapKit.mapOf("id", procInstId));
    }


    public void deleteComments(String procInstId) {
        beanCruder.execute("delete from " + WorkflowConstant.WKFL_COMMENT_TABLE + " where PROC_ID=:id",
            MapKit.mapOf("id", procInstId));
    }

    public void deleteSolicits(String procInstId) {
        beanCruder.execute("delete from " + WorkflowConstant.WKFL_SOLICIT_TABLE + " where PROC_ID=:id",
                MapKit.mapOf("id", procInstId));
    }


    public void insertTasks2History(String procInstId) {
        beanCruder.execute("insert into " + WorkflowConstant.WKFL_TASK_HIST_TABLE + "("+ WorkflowConstant.WKFL_TASK_TABLE_COL +
                ") select "+WorkflowConstant.WKFL_TASK_TABLE_COL+" from " +
            WorkflowConstant.WKFL_TASK_TABLE + " where PROC_ID=:id", MapKit.mapOf("id", procInstId));
    }


    public void insertCommentsHistory(String procInstId) {
        beanCruder.execute("insert into " + WorkflowConstant.WKFL_COMMENT_HIST_TABLE + "(" + WorkflowConstant.WKFL_COMMENT_TABLE_COL+
                ") select " + WorkflowConstant.WKFL_COMMENT_TABLE_COL + " from " +
            WorkflowConstant.WKFL_COMMENT_TABLE + " where PROC_ID=:id", MapKit.mapOf("id", procInstId));
    }

    public void insertSolicitsHistory(String procInstId) {
        beanCruder.execute("insert into " + WorkflowConstant.WKFL_SOLICIT_HIST_TABLE + "(" + WorkflowConstant.WKFL_SOLICIT_TABLE_COL+
                ") select " + WorkflowConstant.WKFL_SOLICIT_TABLE_COL + " from " +
                WorkflowConstant.WKFL_SOLICIT_TABLE + " where PROC_ID=:id", MapKit.mapOf("id", procInstId));
    }

    /**
     * 通过主键 id 查找
     *
     * @param taskId
     * @return
     */
    public WorkflowTaskHistory getWorkflowTaskHistory(String taskId) {
        Map<String, ?> vars = MapKit.mapOf("id", taskId);
        return beanCruder
            .selectOne(WorkflowTaskHistory.class,
                "select * from " + WorkflowConstant.WKFL_TASK_HIST_TABLE + " where TASK_ID=:id",
                vars);

    }

    /**
     * 通过processInstanceId 查找
     *
     * @param processInstanceId
     * @return
     */

    public WorkflowTaskHistory getWorkflowTaskHistoryByProcId(String processInstanceId) {
        Map<String, ?> vars = MapKit.mapOf("id", processInstanceId);
        return beanCruder
            .selectOne(WorkflowTaskHistory.class,
                "select * from " + WorkflowConstant.WKFL_TASK_HIST_TABLE + " where PROC_ID =:id",
                vars);
    }

    /**
     * 更新 " + WorkflowConstant.WKFL_TASK_HIST_TABLE + "表中数据
     *
     * @param workflowTaskHistory
     */


    public void updateWorkflowTaskHistory(WorkflowTaskHistory workflowTaskHistory) {
        beanCruder.update(workflowTaskHistory);
    }


    public void addWorkflowCandidate(WorkflowCandidate workflowCandidate) {
        beanCruder.insert(workflowCandidate);
    }


    public void deleteWorkflowCandidate(String workflowCandidateId) {
        beanCruder.execute("delete from " + WorkflowConstant.WKFL_CANDIDATE_TABLE + " where CANDIDATE_ID=:id",
            MapKit.mapOf("id", workflowCandidateId));
    }


    public void deleteWorkflowCandidateByTask(String taskId) {
        beanCruder.execute("delete from " + WorkflowConstant.WKFL_CANDIDATE_TABLE +
            " where TASK_ID=:taskId", MapKit.mapOf("taskId", taskId));
    }


    public void deleteWorkflowCandidate(String taskId, String userId) {
        beanCruder.execute("delete from " + WorkflowConstant.WKFL_CANDIDATE_TABLE +
                " where TASK_ID=:taskId and USER_ID=:userId",
            MapKit.mapOf("taskId", taskId, "userId", userId));
    }


    public void deleteCandidateGroup(String taskId, String groupId) {
        beanCruder.execute("delete from " + WorkflowConstant.WKFL_CANDIDATE_TABLE +
                " where TASK_ID=:taskId and SCOPE_ROLE_ID=:groupId",
            MapKit.mapOf("taskId", taskId, "groupId", groupId));
    }

    public WorkflowCandidate getWorkflowCandidate(String candidateId) {
        Map<String, ?> vars = MapKit.mapOf("id", candidateId);
        return beanCruder.selectOne(WorkflowCandidate.class, "select * from " +
            WorkflowConstant.WKFL_CANDIDATE_TABLE + " where CANDIDATE_ID=:id", vars);
    }

    public WorkflowCandidate getWorkflowCandidateByTaskIdAndUserId(String taskId, String userId) {
        Map<String, ?> vars =
            MapKit.mapOf("taskId", taskId, "userId", userId);
        return beanCruder
            .selectOne(WorkflowCandidate.class,
                "select * from " + WorkflowConstant.WKFL_CANDIDATE_TABLE
                    + " where TASK_ID=:taskId and USER_ID=:userId",
                vars);
    }

    public WorkflowCandidate getWorkflowCandidateGroupByTaskIdAndGroupId(String taskId,
        String groupId) {
        Map<String, ?> vars =
            MapKit.mapOf("taskId", taskId, "groupId", groupId);
        return beanCruder
            .selectOne(WorkflowCandidate.class,
                "select * from " + WorkflowConstant.WKFL_CANDIDATE_TABLE
                    + " where TASK_ID=:taskId and SCOPE_ROLE_ID=:groupId",
                vars);
    }


    public WorkflowCandidate getCandidateByTaskIdAndUserId(String taskId, String userId) {
        Map<String, String> stringMap =
            MapKit.mapOf("taskId", taskId, "userId", userId);
        return beanCruder
            .selectOne(WorkflowCandidate.class,
                "select * from " + WorkflowConstant.WKFL_CANDIDATE_TABLE
                    + " where TASK_ID=:taskId and USER_ID=:userId",
                stringMap);
    }


    public void updateWorkflowCandidate(WorkflowCandidate workflowCandidate) {
        beanCruder.update(workflowCandidate);
    }


    public void addWorkflowComment(WorkflowComment workflowComment) {
        beanCruder.insert(workflowComment);
    }


    public void addWorkflowCommentHist(WorkflowCommentHistory workflowCommentHistory) {
        beanCruder.insert(workflowCommentHistory);
    }


    public void deleteWorkflowComment(String workflowCommentId) {
        beanCruder.execute("delete from " + WorkflowConstant.WKFL_COMMENT_TABLE +
            " where COMMENT_ID=:id", MapKit.mapOf("id", workflowCommentId));
    }


    public void deleteWorkflowCommentHist(String workflowCommentHistId) {
        beanCruder.execute("delete from " + WorkflowConstant.WKFL_COMMENT_HIST_TABLE +
            " where COMMENT_ID=:id", MapKit.mapOf("id", workflowCommentHistId));
    }

    public WorkflowComment getWorkflowComment(String commentId) {
        Map<String, ?> vars = MapKit.mapOf("id", commentId);
        return beanCruder.selectOne(WorkflowComment.class, "select * from " +
            WorkflowConstant.WKFL_COMMENT_TABLE + " where COMMENT_ID=:id", vars);
    }

    public WorkflowCommentHistory getWorkflowCommentHist(String commentId) {
        Map<String, ?> vars = MapKit.mapOf("id", commentId);
        return beanCruder.selectOne(WorkflowCommentHistory.class, "select * from " +
            WorkflowConstant.WKFL_COMMENT_HIST_TABLE + " where COMMENT_ID=:id", vars);
    }

    public List<WorkflowComment> getWorkflowCommentByTaskId(String taskId) {
        Map<String, ?> vars =
            MapKit.mapOf("taskId", taskId);
        return beanCruder
            .selectList(WorkflowComment.class,
                "select * from " + WorkflowConstant.WKFL_COMMENT_TABLE + " where TASK_ID=:taskId",
                vars);
    }

    public WorkflowCommentHistory getWorkflowCommentHistByTaskId(String taskId) {
        Map<String, ?> vars =
            MapKit.mapOf("taskId", taskId);
        return beanCruder
            .selectOne(WorkflowCommentHistory.class,
                "select * from " + WorkflowConstant.WKFL_COMMENT_HIST_TABLE + " where TASK_ID=:taskId",
                vars);
    }


    public void updateWorkflowComment(WorkflowComment workflowComment) {
        beanCruder.update(workflowComment);
    }


    public void updateWorkflowCommentHist(WorkflowCommentHistory workflowCommentHistory) {
        beanCruder.update(workflowCommentHistory);
    }

    public List<ProcComment> getWorkflowCommentsByProcId(String procId) {
        List<ProcComment> commentList = dataQuery.selectList(ProcComment.class,
            "select * from "+WorkflowConstant.WKFL_COMMENT_TABLE+" where task_id=(select task_id from "+WorkflowConstant.WKFL_PROC_TABLE+" where "
                + "proc_id=:procInstId)",
            MapKit.mapOf("procInstId", procId));
        return commentList;
    }

    public List<ProcComment> getWorkflowCommentHistsByProcId(String procId) {
        List<ProcComment> commentList = dataQuery.selectList(ProcComment.class,
            "select * from "+WorkflowConstant.WKFL_COMMENT_HIST_TABLE+" where task_id=(select task_id from "+WorkflowConstant.WKFL_PROC_TABLE+" where "
                + "proc_id=:procInstId)",
            MapKit.mapOf("procInstId", procId));
        return commentList;
    }

    public List<WorkflowSolicit> getWorkflowSolicitByTaskId(String taskId) {
        return beanCruder.selectList(WorkflowSolicit.class,
            "select * from " + WorkflowConstant.WKFL_SOLICIT_TABLE + " where TASK_ID=:taskId",
            MapKit.mapOf("taskId", taskId));
    }

    public WorkflowSolicit getWorkflowSolicitByTaskIdAndUserId(String taskId, String userId) {
       return beanCruder.selectOne(WorkflowSolicit.class,
            "select * from " + WorkflowConstant.WKFL_SOLICIT_TABLE + " where TASK_ID=:taskId AND ASK_FOR=:userId",
            MapKit.mapOf("taskId", taskId, "userId", userId));
    }

    /**
     * 将WKFL_TASK表中某个结束的流程复制到WKFL_TASK_HIST中，并删除WKFL_TASK相关记录
     *
     * @param procInstId
     */

    public void copyTasks2History(String procInstId) {
        this.insertTasks2History(procInstId);
        this.deleteTasks(procInstId);
    }

    @Transactional
    public void copyCommentsHistory(String procInstId) {
        this.insertCommentsHistory(procInstId);
        this.deleteComments(procInstId);
    }

    @Transactional
    public void copySolicitHistory(String procInstId) {
        this.insertSolicitsHistory(procInstId);
        this.deleteSolicits(procInstId);
    }
}
