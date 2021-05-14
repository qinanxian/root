package com.vekai.workflow.handler.impl;

import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.auth.service.AuthService;
import cn.fisok.raw.kit.JSONKit;
import com.vekai.workflow.handler.model.TaskJSON;
import com.vekai.workflow.service.WorkflowProcService;
import com.vekai.workflow.entity.WorkflowCandidate;
import com.vekai.workflow.entity.WorkflowComment;
import com.vekai.workflow.entity.WorkflowProc;
import com.vekai.workflow.entity.WorkflowTask;
import com.vekai.workflow.handler.ProcHandlerAdapter;
import com.vekai.workflow.model.ProcTask;
import com.vekai.workflow.model.enums.CommentActionEnum;
import com.vekai.workflow.model.enums.ProcFinishEnum;
import com.vekai.workflow.model.enums.TaskAuthChangeEnum;
import com.vekai.workflow.model.enums.TaskJumpEnum;
import com.vekai.workflow.nopub.resource.model.enums.YesOrNo;
import com.vekai.workflow.nopub.service.WorkflowEntityService;
import com.vekai.workflow.service.WorkflowPageParamService;
import com.vekai.workflow.service.WorkflowTaskService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.IdentityLink;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程不同动作后执行的操作
 * @author 左晓敏 <xmzuo@amarsoft.com>
 * @author 陈文楷 <wkchen@amarsoft.com>
 * @author 姚启扬 <qyyao@amarsoft.com>
 * @author 郑汉   <hzheng2@amarsoft.com>
 * @date 2018-01-02
 */
@Component
public class WorkflowDBProcHandler extends ProcHandlerAdapter {

    @Autowired
    private WorkflowEntityService workflowEntityService;
    @Autowired
    private WorkflowTaskService workflowTaskService;
    @Autowired
    private WorkflowProcService workflowProcService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private WorkflowPageParamService workflowPageParamService;
    @Autowired
    protected AuthService authService;
    @Autowired
    private BeanCruder beanCruder;

    private boolean isParallel =false;

    @Override
    public void afterTaskCommit(WorkflowProc proc, WorkflowTask srcTask,
        List<WorkflowTask> dscTasks) {
        super.afterTaskCommit(proc, srcTask, dscTasks);
        for (WorkflowTask workflowTask:dscTasks){
            if (null == workflowEntityService.getWorkflowTask(workflowTask.getTaskId())){
                workflowEntityService.addWorkflowTask(workflowTask);
            }
            if (YesOrNo.Y.toString().equals(workflowTask.getIsConcurrent())&&YesOrNo.Y.toString().equals(srcTask.getIsConcurrent())){
                isParallel=true;
            }
        }
        //过滤并行任务
        List<ProcTask> latestTask = workflowTaskService.getLatestTasks(proc.getProcId());
        if (!isParallel){
            for (ProcTask procTask : latestTask) {
                List<IdentityLink> identityLinks = taskService.getIdentityLinksForTask(procTask.getId());
                for(IdentityLink identityLink : identityLinks){
                    WorkflowCandidate workflowCandidate = new WorkflowCandidate();
                    workflowCandidate.setTaskId(procTask.getId());
                    workflowCandidate.setUserId(identityLink.getUserId());
                    workflowCandidate.setScopeRoleId(identityLink.getGroupId());
                    workflowCandidate.setCandidateType("candidate");
                    workflowEntityService.addWorkflowCandidate(workflowCandidate);
                }
            }
        }
        procInstTaskDesc(proc, latestTask);
        if (latestTask.size()!=0&&!latestTask.isEmpty()){
            List<String> processDepth =
                workflowProcService.getWorkflowProcessDepth(proc.getProcId(), latestTask.get(0).getId());
            proc.setCurMaxDepth(processDepth.get(1));
        }
        //重置 isParallel value
        isParallel=false;
        workflowEntityService.updateWorkflowProc(proc);
    }

    @Override
    public void afterTaskJump(TaskJumpEnum jumpType, WorkflowProc proc, WorkflowTask srcTask,
        WorkflowTask dscTask) {
        super.afterTaskJump(jumpType, proc, srcTask, dscTask);

        List<ProcTask> latestTask = workflowTaskService.getLatestTasks(proc.getProcId());
        procInstTaskDesc(proc, latestTask);
        workflowEntityService.updateWorkflowProc(proc);
        workflowEntityService.updateWorkflowTask(srcTask);


        if(latestTask!=null&&!latestTask.isEmpty()){
            if(latestTask.size()>1){
                List<WorkflowTask> latestWKTaskList = new ArrayList<>();
                latestTask.forEach(task -> {
                    WorkflowTask wkTask = new WorkflowTask();
                    wkTask.setProcId(task.getProcessInstanceId());
                    wkTask.setTaskId(task.getId());
                    wkTask.setTaskInstId(task.getId());
                    wkTask.setTaskDefKey(task.getTaskDefinitionKey());
                    wkTask.setTaskName(task.getName());
                    wkTask.setOwner(task.getOwner());
                    wkTask.setAssignee(task.getAssignee());
                    wkTask.setArrivalTime(task.getCreateTime());
                    wkTask.setIsConcurrent("Y");
                    latestWKTaskList.add(wkTask);
                });
                beanCruder.save(latestWKTaskList);
            } else {
                if(dscTask!=null)  workflowEntityService.addWorkflowTask(dscTask);
            }
        }

    }

    @Override
    public void afterProcStart(WorkflowProc proc, List<WorkflowTask> tasks) {
        /**
         * 业务逻辑
         * 流程实例ID等等---------->WorkflowProc
         */
        super.afterProcStart(proc, tasks);

        List<ProcTask> latestTask = workflowTaskService.getLatestTasks(proc.getProcId());
        procInstTaskDesc(proc, latestTask);
        workflowEntityService.addWorkflowProc(proc);
        for (WorkflowTask task : tasks) {
            workflowEntityService.addWorkflowTask(task);
            List<String> workflowProcessDepth =
                workflowProcService.getWorkflowProcessDepth(proc.getProcId(), task.getTaskId());
            proc.setCurMaxDepth(workflowProcessDepth.get(1));
            proc.setMaxDepth(workflowProcessDepth.get(0));
            workflowEntityService.updateWorkflowProc(proc);
        }
        String procId = proc.getProcId();
        String procInstId = proc.getProcInstId();
        Map<String, Object> params = new HashMap<>();
        params.put("procId", procId);
        workflowPageParamService.appendPageParam(procInstId, params);
    }

    @Override
    public void afterProcFinished(ProcFinishEnum finishType, WorkflowProc proc, WorkflowTask task) {
        String procId = proc.getProcId();
        workflowEntityService.updateWorkflowProc(proc);
        workflowEntityService.updateWorkflowTask(task);
        copy2History(procId);
    }

    @Override
    public void afterCommentAction(CommentActionEnum commentActionEnum,
        WorkflowComment workflowComment) {
        switch (commentActionEnum) {
            case ADD:
                workflowEntityService.addWorkflowComment(workflowComment);
                return;
            case DELETE:
                workflowEntityService.deleteWorkflowComment(workflowComment.getCommentId());
                return;
            case UPDATE:
                workflowEntityService.updateWorkflowComment(workflowComment);
                return;
        }
        workflowEntityService.updateWorkflowComment(workflowComment);
    }

    @Override
    public void afterTaskAuthChange(TaskAuthChangeEnum taskAuthChangeEnum,
        WorkflowTask workflowTask,
        WorkflowCandidate workflowCandidate) {
        super.afterTaskAuthChange(taskAuthChangeEnum, workflowTask, workflowCandidate);

        WorkflowProc workflowProc = workflowEntityService.getWorkflowProc(workflowTask.getProcId());
        List<ProcTask> latestTask = workflowTaskService.getLatestTasks(workflowTask.getProcId());
        procInstTaskDesc(workflowProc, latestTask);
        workflowEntityService.updateWorkflowProc(workflowProc);

        switch (taskAuthChangeEnum) {
            case CLAIMTASK:
                // 清空候选用户
                workflowEntityService.deleteWorkflowCandidateByTask(workflowTask.getTaskId());
                // 设置所有者和候选人
                workflowEntityService.updateWorkflowTask(workflowTask);
                return;
            case UNCLAIMTASK:
                // 设置所有者和候选人
                workflowEntityService.updateWorkflowTask(workflowTask);
                return;
            case CHANGEASSIGNEE:
                workflowEntityService.updateWorkflowTask(workflowTask);
                return;
            case ADDCANDIDATEUSER:
                workflowEntityService.addWorkflowCandidate(workflowCandidate);
                return;
            case DELETECANDIDATEUSER:
                workflowEntityService.deleteWorkflowCandidate(workflowCandidate.getCandidateId());
                return;
            case ADDCANDIDATEGROUP:
                workflowEntityService.addWorkflowCandidate(workflowCandidate);
                return;
            case DELETECANDIDATEGROUP:
                workflowEntityService
                    .deleteCandidateGroup(workflowCandidate.getTaskId(), workflowCandidate.getScopeRoleId());
                return;
        }
        workflowEntityService.updateWorkflowCandidate(workflowCandidate);
        workflowEntityService.updateWorkflowTask(workflowTask);
    }

    private void copy2History(String procId){
        workflowEntityService.copyTasks2History(procId);
        workflowEntityService.copyCommentsHistory(procId);
        workflowEntityService.copySolicitHistory(procId);
    }

    /**
     * 流程实例表
     * 冗余字段LAST_TASK_JSON,LAST_TASK_DESC,LAST_ASSIGNEE_DESC
     * 三个字段的组装
     */
    private WorkflowProc procInstTaskDesc(WorkflowProc proc, List<ProcTask> latestTask){
        List<TaskJSON> taskJSONS = new ArrayList<>();
        List<String> taskDesc = new ArrayList<>();
        List<String> assgineeDesc = new ArrayList<>();
        for (ProcTask procTask : latestTask) {
            // 当前任务简述JSON存入数据库
            taskJSONS.add(new TaskJSON(procTask.getTask()));
            taskDesc.add(procTask.getName());
            String assignee = procTask.getAssignee();
            String userName = (null==assignee||"".equals(assignee))?"<空>":authService.getUserNameById(assignee);
            assgineeDesc.add(userName);
        }
        proc.setLastTaskJson(JSONKit.toJsonString(taskJSONS));
        /**
         * 此处的,分割 暂时使用StringUtils
         */
        proc.setLastTaskDesc(StringUtils.join(taskDesc.toArray(), ","));
        proc.setLastAssigneeDesc(StringUtils.join(assgineeDesc.toArray(), ","));
        return proc;
    }

}
