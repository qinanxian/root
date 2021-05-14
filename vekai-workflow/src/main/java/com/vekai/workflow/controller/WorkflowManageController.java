package com.vekai.workflow.controller;

import com.vekai.auth.entity.User;
import com.vekai.auth.holder.AuthHolder;
import cn.fisok.raw.kit.ValidateKit;
import com.vekai.workflow.exception.WorkflowException;
import com.vekai.workflow.entity.WorkflowTask;
import com.vekai.workflow.model.ProcTask;
import com.vekai.workflow.nopub.service.WorkflowEntityService;
import com.vekai.workflow.service.WorkflowTaskService;
import io.swagger.annotations.Api;
import org.activiti.engine.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(description = "流程管理接口")
@Controller
@RequestMapping("/workflow/manage")
public class WorkflowManageController {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private WorkflowTaskService workflowTaskService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private WorkflowEntityService entityService;


    //管理员代为提交任务
    @PostMapping(path = "/commit")
    @ResponseBody
    public List<ProcTask> commit(@RequestParam("taskId") String taskId,
        @RequestParam("comment") String comment) {
        ValidateKit.notBlank(taskId);
        ValidateKit.notBlank(comment);
        List<ProcTask> procTasks;
        WorkflowTask workflowTask = entityService.getWorkflowTask(taskId);
        if (null != workflowTask && null == workflowTask.getFinishType()) {
            Map<String, Object> variables = new HashMap<>();
            //管理员代为提交任务
            User user = AuthHolder.getUser();
            if (null == user) {
                throw new WorkflowException("该管理员用户不存在,不能进行任务提交操作");
            }
            String userId = user.getId();
            String assignee = workflowTask.getAssignee();
            if (null!=assignee&&!userId.equals(assignee)){
                taskService.unclaim(taskId);
            }
            taskService.claim(taskId,userId);
            procTasks = workflowTaskService.commit(taskId, variables, userId, comment);

        } else {
            throw new WorkflowException("该任务taskId:{0}已被处理", taskId);
        }
        return procTasks;
    }
}
