package com.vekai.workflow.controller;

import cn.fisok.raw.kit.JSONKit;
import cn.fisok.raw.kit.ValidateKit;
import cn.fisok.sqloy.core.BeanCruder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.vekai.auth.entity.User;
import com.vekai.auth.holder.AuthHolder;
import com.vekai.auth.service.AuthService;
import com.vekai.workflow.controller.model.*;
import com.vekai.workflow.entity.*;
import com.vekai.workflow.exception.WorkflowException;
import com.vekai.workflow.model.*;
import com.vekai.workflow.model.enums.TaskStatusEnum;
import com.vekai.workflow.nopub.service.WorkflowEntityService;
import com.vekai.workflow.service.WorkflowProcService;
import com.vekai.workflow.service.WorkflowTaskService;
import io.swagger.annotations.Api;
import org.activiti.engine.delegate.Expression;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.bpmn.behavior.UserTaskActivityBehavior;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Api(description = "流程处理接口")
@Controller
@RequestMapping("/workflow/process")
public class WorkflowProcessController {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    protected WorkflowProcService workflowProcService;
    @Autowired
    protected WorkflowTaskService workflowTaskService;
    @Autowired
    protected WorkflowEntityService workflowEntityService;
    @Autowired
    protected AuthService authService;
    @Autowired
    protected BeanCruder beanCruder;
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 流程启动
     *
     * @param key        流程定义Key
     * @param objectType 业务类型
     * @param objectId   业务编号
     * @param summary    流程简要说明
     * @return 流程实例
     */
    @RequestMapping(path = "/start", method = RequestMethod.POST)
    @ResponseBody
    public ProcInstance start(@RequestParam("Key") String key,
                              @RequestParam("ObjectId") String objectId,
                              @RequestParam("ObjectType") String objectType,
                              @RequestParam("Summary") String summary) {
        // 启动一个流程
        User loginUser = AuthHolder.getUser();
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("ObjectId", objectId);
        variables.put("ObjectType", objectType);
        variables.put("Summary", summary);
        return workflowProcService.start(key, variables, loginUser.getId());
    }

    /**
     * 流程暂停
     *
     * @param procInstId
     */
    @RequestMapping(path = "/suspend", method = RequestMethod.POST)
    @ResponseBody
    public void suspend(@RequestParam("procInstId") String procInstId) {
        workflowProcService.suspend(procInstId);
    }

    /**
     * 流程激活
     *
     * @param procInstId
     */
    @RequestMapping(path = "/activate", method = RequestMethod.POST)
    @ResponseBody
    public void activate(@RequestParam("procInstId") String procInstId) {
        workflowProcService.activate(procInstId);
    }

    /**
     * 流程是否处于暂停状态
     *
     * @param procInstId
     * @return
     */
    @RequestMapping(path = "/isSuspended/{procInstId}", method = RequestMethod.GET)
    @ResponseBody
    public boolean isSuspended(@PathVariable("procInstId") String procInstId) {
        Boolean isSuspended = workflowProcService.isSuspended(procInstId);
        return isSuspended;
    }

    /**
     * 获取流程参数
     *
     * @param procInstId
     * @return
     */
    @RequestMapping(path = "/procVariables/{procInstId}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> procVariables(@PathVariable("procInstId") String procInstId) {
        Map<String, Object> map = workflowProcService.getProcVariables(procInstId);
        return map;
    }

    /**
     * 获取流程实例对象(自定义)
     *
     * @param procInstId
     * @return
     */
    @RequestMapping(path = "/workflowProc/{procInstId}", method = RequestMethod.GET)
    @ResponseBody
    public WorkflowProc workflowProc(@PathVariable("procInstId") String procInstId) {
        WorkflowProc workflowProc = workflowEntityService.getWorkflowProc(procInstId);
        String sponsor = workflowProc.getSponsor();
        workflowProc.setSponsorName(this.convertUserName(sponsor));
        return workflowProc;
    }

    /**
     *  根据objectType objectId
     * 获取流程实例对象
     * @param objectType
     * @param objectId
     * @return
     */

    @GetMapping(path = "/workProc")
    @ResponseBody
    public WorkflowProc getWorkflowProcByObjTypeAndObjId(@RequestParam("ObjectType") String objectType,
        @RequestParam("ObjectId") String objectId) {
        ValidateKit.notBlank(objectType,"传入的参数objectType为空");
        ValidateKit.notBlank(objectId,"传入的参数objectId为空");
        return workflowEntityService.getWorkflowProcByObjTypeAndObjId(objectType,objectId);
    }

    /**
     * 获取流程任务实例对象(自定义)
     *
     * @param taskId
     * @return
     */
    @RequestMapping(path = "/workflowTask/{taskId}", method = RequestMethod.GET)
    @ResponseBody
    public WorkflowTask workflowTask(@PathVariable("taskId") String taskId) {
        WorkflowTask workflowTask = new WorkflowTask();
        HistoricTask histTask = workflowTaskService.getHistTask(taskId);
        boolean finished = workflowProcService.isFinished(histTask.getProcessInstanceId());

        if (finished) {
            WorkflowTaskHistory workflowTaskHist =
                workflowEntityService.getWorkflowTaskHistory(taskId);
            workflowTask.setTaskId(workflowTaskHist.getTaskId());
            workflowTask.setProcId(workflowTaskHist.getProcId());
            workflowTask.setTaskInstId(workflowTaskHist.getTaskInstId());
            workflowTask.setTaskDefKey(workflowTaskHist.getTaskDefKey());
            workflowTask.setTaskName(workflowTaskHist.getTaskName());
            workflowTask.setOwner(workflowTaskHist.getOwner());
            workflowTask.setAssignee(workflowTaskHist.getAssignee());
            workflowTask.setArrivalTime(workflowTaskHist.getArrivalTime());
            workflowTask.setFinishTime(workflowTaskHist.getFinishTime());
            workflowTask.setFinishType(workflowTaskHist.getFinishType());
            workflowTask.setRevision(workflowTaskHist.getRevision());
            workflowTask.setCreatedBy(workflowTaskHist.getCreatedBy());
            workflowTask.setCreatedTime(workflowTaskHist.getCreatedTime());
            workflowTask.setUpdatedBy(workflowTaskHist.getUpdatedBy());
            workflowTask.setUpdatedTime(workflowTaskHist.getUpdatedTime());
        } else {
            workflowTask = workflowEntityService.getWorkflowTask(taskId);
        }
        if (workflowTask == null)
            throw new WorkflowException("流程任务实例:{0}不存在", taskId);
        WorkflowProc workflowProc = workflowEntityService.getWorkflowProc(workflowTask.getProcId());
        String sponsor = workflowProc.getSponsor();
        workflowProc.setSponsorName(this.convertUserName(sponsor));
        workflowTask.setWorkflowProc(workflowProc);
        return workflowTask;
    }

    private String convertUserName(String userId) {
        if (null != userId && !"".equals(userId)) {
            User user = authService.getUser(userId);
            if (null != user) {
                String userName = user.getName();
                return userName;
            }
        }
        return "";
    }

    /**
     * 流程是否结束
     *
     * @param procInstId
     * @return
     */
    @RequestMapping(path = "/isFinished/{procInstId}", method = RequestMethod.GET)
    @ResponseBody
    public Boolean isFinished(@PathVariable("procInstId") String procInstId) {
        Boolean isFinished = workflowProcService.isFinished(procInstId);
        return isFinished;
    }

    /**
     * 流程提交
     *
     * @return
     */
    @RequestMapping(value = "/commit", method = RequestMethod.POST)
    @ResponseBody
    public List<ProcTask> commit(@RequestParam("taskId") String taskId,
                                 @RequestParam("comment") String comment) {
        Validate.notBlank(taskId, "任务Id传入为空");
        Validate.notBlank(comment, "意见传入为空");
        User user = AuthHolder.getUser();
        String userId = user.getId();
        workflowTaskService.claimTask(taskId, userId);
        Map<String, Object> variables = new HashMap<>();
        List<ProcTask> procTasks =
            workflowTaskService.commit(taskId, variables, userId, comment);

        return procTasks;
    }

    /**
     * 流程作废
     *
     * @param taskId
     * @return
     */
    @RequestMapping(value = "/abolish", method = RequestMethod.POST)
    @ResponseBody
    public void abolish(@RequestParam("taskId") String taskId,
        @RequestParam("comment") String comment) {
        User user = AuthHolder.getUser();
        workflowTaskService.abolish(taskId, user.getId(), comment);
    }

    /**
     * 流程退回上一节点（流程自然顺序）
     *
     * @param taskId
     * @return
     */
    @RequestMapping(value = "/backToPrev", method = RequestMethod.POST)
    @ResponseBody
    public ProcTask backToPrev(@RequestParam("taskId") String taskId,
                               @RequestParam("comment") String comment) {
        User user = AuthHolder.getUser();
        ProcTask procTask = workflowTaskService.backToPrev(taskId, user.getId(), comment);
        return procTask;
    }

    /**
     * 获取流程的意见
     *
     * @param procInstId
     * @return
     */
    @RequestMapping(path = "/procComments/{procInstId}", method = RequestMethod.GET)
    @ResponseBody
    public List<ProcComment> procComments(@PathVariable("procInstId") String procInstId) {
        List<ProcComment> list = workflowProcService.getProcComments(procInstId);
        return list;
    }

    /**
     * 判断流程是否处在并行阶段
     *
     * @param procInstId
     * @return
     */
    @RequestMapping(path = "/inParallelBranch/{procInstId}", method = RequestMethod.GET)
    @ResponseBody
    public Boolean inParallelBranch(@PathVariable("procInstId") String procInstId) {
        Boolean inParallelBranch = workflowProcService.inParallelBranch(procInstId);
        return inParallelBranch;
    }

    /**
     * 获取流程的所有任务定义
     *
     * @param procInstId
     * @return
     */
    @RequestMapping(path = "/taskDefinitions/{procInstId}", method = RequestMethod.GET)
    @ResponseBody
    public List<TaskDefinition> taskDefinitions(@PathVariable("procInstId") String procInstId) {
        List<TaskDefinition> list = workflowProcService.getTaskDefinitions(procInstId);
        return list;
    }

    /**
     * 流程否决
     *
     * @param taskId
     */
    @RequestMapping(path = "/reject", method = RequestMethod.POST)
    @ResponseBody
    public void reject(@RequestParam("taskId") String taskId,
        @RequestParam("comment") String comment) {
        User user = AuthHolder.getUser();
        workflowTaskService.reject(taskId, user.getId(), comment);
    }

    /**
     * 任务取回
     *
     * @param vars
     */
    @RequestMapping(path = "/retrieve", method = RequestMethod.POST)
    @ResponseBody
    public void retrieve(@RequestBody Map<String, Object> vars) {
        String taskId = (String) vars.get("taskId");
        String comment = (String) vars.get("comment");
        Validate.notBlank(taskId, "任务Id为空");
        Validate.notBlank(comment, "意见为空");
        User user = AuthHolder.getUser();
        workflowTaskService.retrieve(taskId, user.getId(), comment);
    }

    /**
     * 任务取回条件检测
     *
     * @param taskId
     * @return
     */
    @RequestMapping(path = "/retrieveCondition/{taskId}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, String> retrieveCondition(@PathVariable("taskId") String taskId) {
        return workflowTaskService.retrieveCondition(taskId);
    }

    /**
     * 退回
     *
     * @return 目标任务对象
     */
    @RequestMapping(path = "/backFront", method =
        RequestMethod.POST)
    @ResponseBody
    public Task backFront(@RequestParam("taskId") String taskId,
        @RequestParam("dscTaskKey") String dscTaskKey,
        @RequestParam("comment") String comment) {
        User user = AuthHolder.getUser();
        String srcUserId = user.getId();
        Task task = workflowTaskService.backFront(taskId, dscTaskKey, srcUserId, comment);
        return task;
    }

    /**
     * 驳回
     *
     * @return 目标任务对象
     */
    @RequestMapping(path = "/backOrigin", method =
        RequestMethod.POST)
    @ResponseBody
    public Task backOrigin(@RequestParam("taskId") String taskId,
        @RequestParam("comment") String comment) {
        User user = AuthHolder.getUser();
        String userId = user.getId();
        Task task = workflowTaskService.backOrigin(taskId, userId, comment);
        return task;
    }

    /**
     * 退回修改
     *
     * @return 目标任务对象
     */
    @RequestMapping(path = "/backTrack", method = RequestMethod.POST)
    @ResponseBody
    public Task backTrack(@RequestParam("taskId") String taskId,
        @RequestParam("dscTaskKey") String dscTaskKey,
        @RequestParam("comment") String comment) {
        Validate.notBlank(taskId, "任务Id传入为空");
        Validate.notBlank(dscTaskKey, "跳转目标任务Key传入为空");
        Validate.notBlank(comment, "意见传入为空");
        User user = AuthHolder.getUser();
        String srcUserId = user.getId();
        Task task = workflowTaskService.backTrack(taskId, dscTaskKey, srcUserId, comment);
        return task;
    }


    /**
     * 流程往后跑到任意节点
     *
     * @param srcTaskId
     * @param dscTaskKey
     * @param taskStatusEnum
     * @return
     */
    @RequestMapping(path = "/jumpBack", method = RequestMethod.POST)
    @ResponseBody
    public Task jumpBack(@RequestParam("taskId") String srcTaskId,
        @RequestParam("dscTaskKey") String dscTaskKey,
        @RequestParam("userId") String userId, @RequestParam("taskStatusEnum")
        TaskStatusEnum taskStatusEnum, @RequestParam("comment") String comment) {
        Task task =
            workflowTaskService.jumpBack(srcTaskId, dscTaskKey, userId, taskStatusEnum, comment);
        return task;
    }

    /**
     * 流程往前跳到任意节点
     *
     * @param srcTaskId
     * @param dscTaskKey
     * @param taskStatusEnum
     * @return
     */
    @RequestMapping(path = "/jumpForward", method = RequestMethod.POST)
    @ResponseBody
    public Task jumpForward(@RequestParam("taskId") String srcTaskId,
        @RequestParam("dscTaskKey") String dscTaskKey,
        @RequestParam("userId") String userId, @RequestParam("taskStatusEnum")
        TaskStatusEnum taskStatusEnum, @RequestParam("comment") String comment) {
        Task task =
            workflowTaskService.jumpForward(srcTaskId, dscTaskKey, userId, taskStatusEnum, comment);
        return task;
    }




    /**
     * 签收任务
     *
     * @param taskId
     * @param userId
     */
    @RequestMapping(path = "/claimTask", method = RequestMethod.POST)
    @ResponseBody
    public void claimTask(@RequestParam("taskId") String taskId,
        @RequestParam("userId") String userId) {
        workflowTaskService.claimTask(taskId, userId);
    }

    /**
     * 取消签收任务
     *
     * @param taskId
     */
    @RequestMapping(path = "/unClaimTask", method = RequestMethod.POST)
    @ResponseBody
    public void unClaimTask(@RequestParam("taskId") String taskId) {
        workflowTaskService.unClaimTask(taskId);
    }



    @RequestMapping(path = "/jumpYForward", method = RequestMethod.POST)
    @ResponseBody
    public Task jumpYForward(@RequestParam("taskId") String srcTaskId,
        @RequestParam("dscTaskKey") String dscTaskKey,
        @RequestParam("comment") String comment) {
        String userId =AuthHolder.getUser().getId();

        return workflowTaskService.jumpYForwardTask(srcTaskId, dscTaskKey, userId, TaskStatusEnum.FORWARD, comment);
    }

    //------------------------------------------------任务扭转指定受理人Start
    // ---------------------------------------//

    /**
     * 任务提交受理人列表
     * 流程“提交指定受理人”不适用的场景
     * 1. 下一节点是结束节点
     * 2. 下一阶段为并行任务
     * 3. 下一阶段为多实例任务(会签)
     * 4. 下一节点为判断网关(参数容易算错,不支持)
     * 5. 下一节点为“退回修改发起节点”则受理人只能是改发起人
     * 6. 多实例任务中,只有最后一个任务才能弹出指定受理人选择框
     *
     * @param taskId
     * @return
     */
    @RequestMapping(path = "/commitTaskAssignee/{taskId}", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> commitTaskAssignee(@PathVariable("taskId") String taskId) {
        Validate.notBlank(taskId, "流程任务Id未传入");
        Map<String, Object> res = new HashMap<>();
        List<String> users = new ArrayList<>();
        List<String> roles = new ArrayList<>();

        res.put("users", users);
        res.put("roles", roles);
        res.put("status", 0);

        List<PvmActivity> nextActivities = workflowTaskService.getNextActivities(taskId);
        if (null == nextActivities || nextActivities.isEmpty()) {
            // 下一节点没有任务,未绘制结束节点等
            res.put("status", 1);
            return res;
        }
        if (nextActivities.size() > 1) {
            // 下一个节点绘制了很多节点
            res.put("status", 2);
            return res;
        }
        if (nextActivities.size() == 1) {
            PvmActivity current = nextActivities.get(0);
            if ("parallelGateway".equals(current.getProperty("type")) || "inclusiveGateway"
                .equals(current.getProperty("type"))) {
                // 下一个节点为并行网关
                res.put("status", 2);
                return res;

            } else if ("exclusiveGateway".equals(current.getProperty("type"))) {
                // 下一个节点为排他网关
                // 网关未能进行表达式解析
                res.put("status", 4);
                return res;

            } else if ("endEvent".equals(current.getProperty("type"))) {
                // 下一个节点为结束
                res.put("status", 1);
                return res;
            } else if ("userTask".equals(current.getProperty("type"))) {
                /**
                 * 判断当前节点是否是多实例任务
                 */
                ProcTask curTask = workflowTaskService.getTask(taskId);
                String curTaskkey = curTask.getTaskDefinitionKey();
                List<ProcTask> latestTasks =
                    workflowTaskService.getLatestTasks(curTask.getProcessInstanceId());

                // 当前未处理的多实例任务个数
                int multiInstTaskNum = 0;
                if (latestTasks.size() > 1) {
                    for (ProcTask task : latestTasks) {
                        if (curTaskkey.equals(task.getTaskDefinitionKey())) {
                            multiInstTaskNum++;
                        }
                    }
                }

                if (multiInstTaskNum > 1) {
                    //多实例任务中,只有最后一个任务才能弹出指定受理人选择框
                    res.put("status", 6);
                    return res;
                }

                // 以下是下一阶段只有一个普通任务节点的判断
                List<HistoricTask> histTasks =
                    workflowTaskService.getHistTasks(curTask.getProcessInstanceId());
                if (null != histTasks && histTasks.size() > 1) {
                    HistoricTask prevHistTask = histTasks.get(histTasks.size() - 1);
                    // 上一个任务为退回修改任务
                    if (TaskStatusEnum.BACKTRACK.getName().equals(prevHistTask.getDeleteReason())) {
                        users.add(prevHistTask.getAssignee());
                        res.put("nextTaskKey", prevHistTask.getTaskDefinitionKey());
                        res.put("nextTaskName", prevHistTask.getName()+"(退回修改任务)");
                        return res;
                    }
                }

                TaskDefinition taskDefinition =
                    (TaskDefinition) current.getProperty("taskDefinition");
                res.put("nextTaskKey", taskDefinition.getKey());
                res.put("nextTaskName", taskDefinition.getNameExpression().getExpressionText());
                /**
                 * 目前存在的问题:
                 * 若候选人或者侯选组配置的是表达式${user},则此处返回的是表达式
                 */
                for (Expression expression : taskDefinition.getCandidateUserIdExpressions()) {
                    users.add(expression.getExpressionText());
                }
                for (Expression expression : taskDefinition.getCandidateGroupIdExpressions()) {
                    roles.add(expression.getExpressionText());
                }
            }
        }


        return res;
    }


    /**
     * 任务提交受理人列表---提交设置受理人使用
     *
     * @param procInstId
     * @param taskDefKey
     * @return
     */
    @RequestMapping(path = "/backTaskAssignee/{procInstId}/{taskDefKey}", method = RequestMethod
        .GET)
    @ResponseBody
    public Map<String, Object> backTaskAssignee(@PathVariable("procInstId") String procInstId,
        @PathVariable("taskDefKey") String taskDefKey) {
        Validate.notBlank(procInstId, "流程实例Id未传入");
        Validate.notBlank(taskDefKey, "流程任务定义Key未传入");
        Map<String, Object> res = new HashMap<>();
        List<String> users = new ArrayList<>();
        List<String> roles = new ArrayList<>();
        res.put("users", users);
        res.put("roles", roles);
        res.put("status", 0);

        HistoricTask firstHistTask = workflowTaskService.getFirstHistTask(procInstId);
        if (taskDefKey.equals(firstHistTask.getTaskDefinitionKey())) {
            users.add(firstHistTask.getAssignee());
            return res;
        }
        TaskDefinition taskDefinition =
            workflowTaskService.getTaskDefinition(procInstId, taskDefKey);
        if (taskDefinition != null) {
            for (Expression expression : taskDefinition.getCandidateUserIdExpressions()) {
                users.add(expression.getExpressionText());
            }
            for (Expression expression : taskDefinition.getCandidateGroupIdExpressions()) {
                roles.add(expression.getExpressionText());
            }
        }
        return res;
    }


    /**
     * 提交指定签收人
     *
     * @return
     */
    @RequestMapping(value = "/commitAssignee", method = RequestMethod.POST)
    @ResponseBody
    public List<ProcTask> commitAssignee(@RequestParam("taskId") String taskId,
        @RequestParam("comment") String comment,
        @RequestParam("assignee") String assignee) {
        Validate.notBlank(taskId, "任务Id传入为空");
        Validate.notBlank(comment, "意见传入为空");
        Validate.notBlank(assignee, "受理人传入为空");
        User user = AuthHolder.getUser();
        String userId = user.getId();
        workflowTaskService.claimTask(taskId, userId);
        Map<String, Object> variables = new HashMap<>();
        List<ProcTask> procTasks =
            workflowTaskService.commitAssignee(taskId, variables, userId, comment, assignee);

        return procTasks;
    }

    /**
     * 退回指定受理人
     *
     * @return 目标任务对象
     */
    @RequestMapping(path = "/backFrontAssignee", method = RequestMethod.POST)
    @ResponseBody
    public Task backFrontAssignee(@RequestParam("taskId") String taskId,
        @RequestParam("dscTaskKey") String dscTaskKey,
        @RequestParam("comment") String comment,
        @RequestParam("assignee") String assignee) {

        User user = AuthHolder.getUser();
        String srcUserId = user.getId();
        Task task =
            workflowTaskService.backFrontAssignee(taskId, dscTaskKey, srcUserId, comment, assignee);
        return task;
    }

    /**
     * 退回修改
     *
     * @return 目标任务对象
     */
    @RequestMapping(path = "/backTrackAssignee", method = RequestMethod.POST)
    @ResponseBody
    public Task backTrackAssignee(@RequestParam("taskId") String taskId,
        @RequestParam("dscTaskKey") String dscTaskKey,
        @RequestParam("comment") String comment,
        @RequestParam("assignee") String assignee) {
        Validate.notBlank(taskId, "任务Id传入为空");
        Validate.notBlank(dscTaskKey, "跳转目标任务Key传入为空");
        Validate.notBlank(comment, "意见传入为空");
        Validate.notBlank(assignee, "受理人传入为空");
        User user = AuthHolder.getUser();
        String srcUserId = user.getId();
        Task task =
            workflowTaskService.backTrackAssignee(taskId, dscTaskKey, srcUserId, comment, assignee);
        return task;
    }



    //------------------------------------------------任务扭转指定受理人End
    // ---------------------------------------//


    /**
     * 获取任务的受理人
     *
     * @param taskId
     * @return
     */
    @RequestMapping(path = "/assignee", method = RequestMethod.POST)
    @ResponseBody
    public String assignee(@RequestParam("taskId") String taskId) {
        String assignee = workflowTaskService.getAssignee(taskId);
        return assignee;
    }

    /**
     * 更改流程任务受理人(只有一个)
     */
    @RequestMapping(path = "/changeAssignee", method = RequestMethod.POST)
    @ResponseBody
    public void changeAssignee(@RequestBody ChangeAssigneeModel changeAssigneeModel) {
        workflowTaskService
            .changeAssignee(changeAssigneeModel.getTaskId(), changeAssigneeModel.getUserId());
    }

    /**
     * 给任务添加候选人
     *
     * @param taskId
     * @param userId
     */
    @RequestMapping(path = "/addCandidateUser", method = RequestMethod.POST)
    @ResponseBody
    public void addCandidateUser(@RequestParam("taskId") String taskId,
        @RequestParam("userId") String userId) {
        workflowTaskService.addCandidateUser(taskId, userId);
    }

    /**
     * 给任务添加多个候选人
     */
    @RequestMapping(path = "/addCandidateUsers", method = RequestMethod.POST)
    @ResponseBody
    public void addCandidateUsers(@RequestBody AddCandidateModel addCandidateModel) {
        List<String> userIds = addCandidateModel.getUserIds();
        String taskId = addCandidateModel.getTaskId();
        for (String userId : userIds) {
            workflowTaskService.addCandidateUser(taskId, userId);
        }
    }

    /**
     * 删除任务的候选人
     *
     * @param taskId
     * @param userId
     */
    @RequestMapping(path = "/deleteCandidateUser", method = RequestMethod.POST)
    @ResponseBody
    public void deleteCandidateUser(@RequestParam("taskId") String taskId,
        @RequestParam("id") String userId) {
        workflowTaskService.deleteCandidateUser(taskId, userId);
    }

    /**
     * 增加任务候选组
     */
    @RequestMapping(path = "/addCandidateGroup", method = RequestMethod.POST)
    @ResponseBody
    public void addCandidateGroup(@RequestBody AddCandidateGroupModel addCandidateGroupModel) {
        List<String> roleIds = addCandidateGroupModel.getRoleIds();
        String taskId = addCandidateGroupModel.getTaskId();
        for (String roleId : roleIds) {
            workflowTaskService.addCandidateGroup(taskId, roleId);
        }
    }

    /**
     * 删除任务候选组
     *
     * @param taskId
     * @param groupId
     */
    @RequestMapping(path = "/deleteCandidateGroup", method = RequestMethod.POST)
    @ResponseBody
    public void deleteCandidateGroup(@RequestParam("taskId") String taskId,
        @RequestParam("id") String groupId) {
        workflowTaskService.deleteCandidateGroup(taskId, groupId);
    }

    /**
     * 任务候选组
     *
     * @param taskId
     */
    @RequestMapping(path = "/candidateGroups/{taskId}", method = RequestMethod.GET)
    @ResponseBody
    public List<String> candidateGroups(@PathVariable("taskId") String taskId) {
        List<String> list = workflowTaskService.getCandidateGroups(taskId);
        return list;
    }

    /**
     * 获取任务候选人
     *
     * @param taskId
     * @param includeCandidateGroup
     */
    @RequestMapping(path = "/candidateUsers", method =
        RequestMethod.GET)
    @ResponseBody
    public List<String> candidateUsers(@RequestParam("taskId") String taskId,
        @RequestParam boolean includeCandidateGroup) {
        List<String> list = workflowTaskService.getCandidateUsers(taskId, includeCandidateGroup);
        return list;
    }

    /**
     * 获取任务候选组中候选人
     *
     * @param taskId
     */
    @RequestMapping(path = "/candidateGroupUsers/{taskId}", method = RequestMethod.GET)
    @ResponseBody
    public List<String> candidateGroupUsers(@PathVariable("taskId") String taskId) {
        List<String> list = workflowTaskService.getCandidateGroupUsers(taskId);
        return list;
    }

    /**
     * 获取任务实例
     *
     * @param taskId
     * @return
     */
    @RequestMapping(path = "/task/{taskId}", method = RequestMethod.GET)
    @ResponseBody
    public ProcTask task(@PathVariable("taskId") String taskId) {
        ProcTask procTask = workflowTaskService.getTask(taskId);
        return procTask;
    }

    /**
     * 判断是否是发起岗位任务
     *
     * @param taskId
     * @return
     */
    @RequestMapping(path = "/isFirstTask/{taskId}", method = RequestMethod.GET)
    @ResponseBody
    public boolean isFirstTask(@PathVariable("taskId") String taskId) {
        boolean isFirstTask = workflowTaskService.isFirstTask(taskId);
        return isFirstTask;
    }

    /**
     * 获取当前存活任务(多个)
     *
     * @param procInstId
     * @return
     */
    @RequestMapping(path = "/latestTasks/{procInstId}", method = RequestMethod.GET)
    @ResponseBody
    public List<ProcTask> latestTasks(@PathVariable("procInstId") String procInstId) {
        List<ProcTask> list = workflowTaskService.getLatestTasks(procInstId);
        return list;
    }

    /**
     * 获取当前存活任务(单个)
     *
     * @param procInstId
     * @return
     */
    @RequestMapping(path = "/latestTask/{procInstId}", method = RequestMethod.GET)
    @ResponseBody
    public ProcTask latestTask(@PathVariable("procInstId") String procInstId) {
        ProcTask procTask = workflowTaskService.getLatestTask(procInstId);
        return procTask;
    }

    /**
     * 判断流程是否处于并行阶段
     *
     * @param taskId
     * @return
     */
    @RequestMapping(path = "/taskInParallelBranch/{taskId}", method = RequestMethod.GET)
    @ResponseBody
    public boolean taskInParallelBranch(@PathVariable("taskId") String taskId) {
        boolean inParallelBranch = workflowTaskService.taskInParallelBranch(taskId);
        return inParallelBranch;
    }

    /**
     * 给任务添加意见
     *
     * @param taskId
     * @param userId
     * @param taskStatusEnum
     * @param comment
     */
    @RequestMapping(path = "/addComment", method =
        RequestMethod.POST)
    @ResponseBody
    public void addComment(@RequestParam("taskId") String taskId,
        @RequestParam("userId") String userId,
        @RequestParam("taskStatusEnum") TaskStatusEnum taskStatusEnum,
        @RequestParam("comment") String comment) {
        workflowTaskService.addComment(taskId, userId, taskStatusEnum, comment);
    }

    /**
     * 给任务删除意见
     *
     * @param commentId
     */
    @RequestMapping(path = "/deleteComment", method = RequestMethod.POST)
    @ResponseBody
    public void deleteComment(@RequestParam("commentId") String commentId) {
        workflowTaskService.deleteComment(commentId);
    }

    /**
     * 给任务更新意见
     *
     * @param comment
     * @param commentId
     */
    @RequestMapping(path = "/updateComment", method =
        RequestMethod.POST)
    @ResponseBody
    public void updateComment(@RequestParam("commentId") String commentId,
        @RequestParam("comment") String comment) {
        workflowTaskService.updateComment(commentId, comment);
    }

    /**
     * 获取任务的所有意见
     *
     * @param taskId
     */
    @RequestMapping(path = "/taskComments/{taskId}", method = RequestMethod.GET)
    @ResponseBody
    public List<ProcComment> taskComments(@PathVariable("taskId") String taskId) {
        List<ProcComment> list = workflowTaskService.getTaskComments(taskId);
        return list;
    }

    /**
     * 获取历史任务实例
     *
     * @param taskId
     * @return
     */
    @RequestMapping(path = "/histTask/{taskId}", method = RequestMethod.GET)
    @ResponseBody
    public HistoricTask histTask(@PathVariable("taskId") String taskId) {
        HistoricTask historicTask = workflowTaskService.getHistTask(taskId);
        return historicTask;
    }

    /**
     * 根据流程实例ID获取历史任务实例
     *
     * @param procInstId 流程实例Id
     * @param isFinished 是否是已完成任务
     * @return
     */
    @RequestMapping(path = "/histTasks", method =
        RequestMethod.GET)
    @ResponseBody
    public List<HistoricTask> histTasks(@RequestParam("procInstId") String procInstId,
        @RequestParam("isFinished") boolean isFinished) {
        List<HistoricTask> list = workflowTaskService.getHistTasks(procInstId, isFinished);
        return list;
    }

    /**
     * 获取发起岗位任务
     *
     * @param procInstId
     * @return
     */
    @RequestMapping(path = "/firstHistTask/{procInstId}", method = RequestMethod.GET)
    @ResponseBody
    public HistoricTask firstHistTask(@PathVariable("procInstId") String procInstId) {
        HistoricTask historicTask = workflowTaskService.getFirstHistTask(procInstId);
        return historicTask;
    }

    /**
     * 获取指定任务的任务定义
     *
     * @param taskId
     * @return
     */
    @RequestMapping(path = "/taskDefinition/{taskId}", method = RequestMethod.GET)
    @ResponseBody
    public ProcTaskDefinition taskDefinition(@PathVariable("taskId") String taskId) {
        TaskDefinition taskDefinition = workflowTaskService.getTaskDefinition(taskId);
        return new ProcTaskDefinition(taskDefinition);
    }

    /**
     * 根据流程实例ID和任务定义key获取任务定义
     *
     * @param procInstId
     * @param taskDefKey
     * @return
     */
    @RequestMapping(path = "/taskDefinition/{procInstId}/{taskDefKey}", method = RequestMethod.GET)
    @ResponseBody
    public ProcTaskDefinition taskDefinition(@PathVariable("procInstId") String procInstId,
        @PathVariable("taskDefKey") String taskDefKey) {
        TaskDefinition taskDefinition =
            workflowTaskService.getTaskDefinition(procInstId, taskDefKey);
        return new ProcTaskDefinition(taskDefinition);
    }

    /**
     * 获取任务表单定义属性
     *
     * @param taskId
     * @return
     */
    @RequestMapping(path = "/taskFormProperties/{taskId}", method = RequestMethod.GET)
    @ResponseBody
    public List<FormProperty> taskFormProperties(@PathVariable("taskId") String taskId) {
        List<FormProperty> list = workflowTaskService.getTaskFormProperties(taskId);
        return list;
    }

    @RequestMapping(path = "/candidates/{taskId}", method = RequestMethod.GET)
    @ResponseBody
    public HashMap<String, Object> candidates(@PathVariable("taskId") String taskId) {
        return getTaskCandidates(taskId);
    }

    @RequestMapping(path = "/hisActinsts/{procInstId}", method = RequestMethod.GET)
    @ResponseBody
    public List<HistoricActivityInstance> hisActinsts(
        @PathVariable("procInstId") String procInstId) {
        return workflowProcService.getHisActInsts(procInstId, false);
    }


    /**
     * 根据流程实例ID和流程的定义KEY(兼容多实例任务)
     * 查询出指定任务KEY位置的候选人
     *
     * @param procInstId
     * @return
     */
    @RequestMapping(path = "/latestTasksCandidates/{procInstId}/{taskKey}", method =
        RequestMethod.GET)
    @ResponseBody
    public HashMap<String, Object> latestTasksCandidates(
        @PathVariable("procInstId") String procInstId,
        @PathVariable("taskKey") String taskKey) {
        return getLatestTasksCandidates(procInstId, taskKey);
    }

    private HashMap<String, Object> getLatestTasksCandidates(String procInstId, String taskKey){
        List<ProcTask> tasks = new ArrayList<ProcTask>();
        for (ProcTask procTask : workflowTaskService.getLatestTasks(procInstId)) {
            if (taskKey.equals(procTask.getTaskDefinitionKey())) {
                tasks.add(procTask);
            }
        }

        HashMap<String, Object> candidates = new HashMap<String, Object>();
        for (ProcTask task : tasks) {
            HashMap<String, Object> cds = getTaskCandidates(task.getId());
            if (cds != null && cds.size() > 0) {
                if (candidates.size() > 0) {
                    List<String> candidateGroups = (List<String>) candidates.get("candidateGroups");
                    List<String> candidateUsers = (List<String>) candidates.get("candidateUsers");

                    List<String> cdGroups = (List<String>) cds.get("candidateGroups");
                    List<String> cdUsers = (List<String>) cds.get("candidateUsers");

                    candidateGroups.addAll(cdGroups);
                    candidateUsers.addAll(cdUsers);
                } else {
                    candidates.putAll(cds);
                }
            }
        }
        return candidates;
    }

    /**
     * 获取未完成的流程历史任务节点（去重并排序）
     *
     * @param procInstId
     * @return
     */
    @RequestMapping(path = "/histTasks/{procInstId}", method = RequestMethod.GET)
    @ResponseBody
    public List<HistoricTask> histTasks(@PathVariable("procInstId") String procInstId) {
        return workflowTaskService.getHistTasks(procInstId);
    }

    private HashMap<String, Object> getTaskCandidates(String taskId) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        ProcTask task = workflowTaskService.getTask(taskId);
        String assignee = task.getAssignee();
        if (!StringUtils.isBlank(assignee)) {
            map.put("assignee", assignee);
            return map;
        }
        map.put("candidateGroups", workflowTaskService.getCandidateGroups(taskId));
        map.put("candidateUsers", workflowTaskService.getCandidateUsers(taskId, true));
        return map;
    }

    /**
     * 转他人处理
     *
     * @param taskId
     * @param userId
     * @param comment
     */
    @RequestMapping(path = "/deliverTo", method = RequestMethod.POST)
    @ResponseBody
    public void deliverTo(@RequestParam("taskId") String taskId,
        @RequestParam("userId") String userId,
        @RequestParam("comment") String comment) {
        Validate.notBlank(taskId, "任务Id传入为空");
        Validate.notBlank(userId, "任务受理人传入为空");
        Validate.notBlank(comment, "意见传入为空");
        User user = AuthHolder.getUser();
        workflowTaskService.deliverTo(taskId, userId, user.getId(), comment);
    }


    /**
     * 征求他人意见
     *
     * @param taskId
     * @param comment
     * @param userIds
     */
    @RequestMapping(path = "/takeAdvice", method = RequestMethod.POST)
    @ResponseBody
    public void takeAdvice(@RequestParam("taskId") String taskId,
        @RequestParam("comment") String comment,
        @RequestParam("userIds[]") String[] userIds) {
        List<String> userIdList = Arrays.asList(userIds);
        User user = AuthHolder.getUser();
        String srcUserId = user.getId();
        workflowTaskService.takeAdvice(userIdList, taskId, comment, srcUserId);
    }

    @GetMapping(path = "/taskSolicit")
    @ResponseBody
    public List<WorkflowSolicit> getTaskSolicit(@RequestParam("taskId") String taskId) {
        ValidateKit.notBlank(taskId, "传入的taskId为空");
        return workflowEntityService.getWorkflowSolicitByTaskId(taskId);
    }

    /**
     * 征求意见-反馈意见
     *
     * @param taskId
     * @param comment
     */
    @RequestMapping(path = "/feedback", method = RequestMethod.POST)
    @ResponseBody
    public void feedback(@RequestParam("taskId") String taskId,
        @RequestParam("comment") String comment) {
        User user = AuthHolder.getUser();
        String userId = user.getId();
        workflowTaskService.feedback(taskId, comment, userId);
    }

    /**
     * 判断任务是否处于征求他人意见状态中
     * @param taskId
     * @return
     */
    @RequestMapping(path = "/feedbackStatus", method = RequestMethod.GET)
    @ResponseBody
    public boolean feedbackStatus(@RequestParam("taskId") String taskId) {
        return workflowTaskService.feedbackStatus(taskId);
    }

    /**
     * 我参与的流程-为流程实例资源查看页面提供WorkflowTask对象
     *
     * @param procInstId
     * @return
     */
    @RequestMapping(path = "/procInstResourceWorkflowTask/{procInstId}", method = RequestMethod.GET)
    @ResponseBody
    public WorkflowTask procInstResourceWorkflowTask(
        @PathVariable("procInstId") String procInstId) {
        WorkflowTask workflowTask = new WorkflowTask();
        WorkflowProc workflowProc = workflowEntityService.getWorkflowProc(procInstId);
        if (null == workflowProc)
            throw new WorkflowException("流程实例:{0}不存在", procInstId);
        String sponsor = workflowProc.getSponsor();
        workflowProc.setSponsorName(this.convertUserName(sponsor));
        workflowTask.setWorkflowProc(workflowProc);
        return workflowTask;
    }


    /**
     * 获取任务下一节点的任务定义
     *
     * @param taskId
     * @return
     */
    @RequestMapping(path = "/nextTaskDefinition/{taskId}", method = RequestMethod.GET)
    @ResponseBody
    public List<ProcTaskDefinition> nextTaskDefinition(@PathVariable("taskId") String taskId) {
        List<ProcTaskDefinition> taskDefinitions = new ArrayList<>();
        Set<PvmActivity> nextUserActivities = workflowTaskService.getNextUserActivities(taskId);
        for (PvmActivity pvmActivity : nextUserActivities) {
            TaskDefinition taskDefinition =
                (TaskDefinition) pvmActivity.getProperty("taskDefinition");
            taskDefinitions.add(new ProcTaskDefinition(taskDefinition));
        }
        return taskDefinitions;
    }


    /**
     * 查询可退回的活动节点----流程退回节点选择列表
     *
     * @param procInstId
     * @return
     */
    @RequestMapping(path = "/allowBackActivitys/{procInstId}", method = RequestMethod.GET)
    @ResponseBody
    public List<HistoricActivityInstance> allowBackActivitys(
        @PathVariable("procInstId") String procInstId) {
        return workflowTaskService.getAllowBackActivitys(procInstId);
    }

    /**
     * 流程所有意见----兼容历史任务
     *
     * @param procInstId
     * @return
     */
    @RequestMapping(path = "/procInstComments/{procInstId}", method = RequestMethod.GET)
    @ResponseBody
    public TaskNode procInstComments(@PathVariable("procInstId") String procInstId) {

        boolean finished = workflowProcService.isFinished(procInstId);

        String taskTable = "WKFL_TASK";
        String commentTable = "WKFL_COMMENT";
        String solicitTable = "WKFL_SOLICIT";

        if (finished) {
            taskTable = "WKFL_TASK_HIST";
            commentTable = "WKFL_COMMENT_HIST";
            solicitTable = "WKFL_SOLICIT_HIST";
        }

        Map<String, Object> whereMap = new HashMap<>();
        whereMap.put("procId", procInstId);

        List<WorkflowTask> workflowTasks = beanCruder.selectList(WorkflowTask.class,
            "SELECT * FROM " + taskTable
                + " WT WHERE WT.PROC_ID=:procId  ORDER BY WT.ARRIVAL_TIME ASC", whereMap);

        List<WorkflowComment> workflowComments = beanCruder.selectList(WorkflowComment.class,
            "SELECT * FROM " + commentTable
                + " WC WHERE WC.PROC_ID=:procId ORDER BY WC.CREATED_TIME ASC", whereMap);

        List<WorkflowSolicit> workflowSolicits = beanCruder.selectList(WorkflowSolicit.class,
            "SELECT * FROM " + solicitTable
                + " WS WHERE WS.PROC_ID=:procId ORDER BY WS.CREATED_TIME ASC", whereMap);

        //workflowTask --> TaskComment
        TaskNode taskNode = new TaskNode();
        taskNode.setProcId(procInstId);
        Map<String,List<TaskComment>> taskStage = new LinkedHashMap<>();
        for (WorkflowTask task:workflowTasks){
            List<TaskComment> taskComments = new ArrayList<>();
            workflowComments.forEach(comment -> {
                if (task.getTaskId().equals(comment.getTaskId())){
                    TaskComment taskComment = new TaskComment();
                    taskComment.setCommentId(comment.getCommentId());
                    taskComment.setType(comment.getCommentType());
                    taskComment.setUserName(authService.getUserNameById(comment.getUserId()));
                    taskComment.setContent(comment.getMessage());
                    taskComment.setCreatedTime(comment.getCreatedTime());
                    taskComments.add(taskComment);
                }
            });
            workflowSolicits.forEach(workflowSolicit -> {
                if (task.getTaskId().equals(workflowSolicit.getTaskId())){
                    TaskSolicitComment solicitComment = new TaskSolicitComment();
                    solicitComment.setSolicitorSummary(workflowSolicit.getSolicitSummary());
                    solicitComment.setSolicitor(authService.getUserNameById(workflowSolicit.getSolicitor()));
                    solicitComment.setSolicitReply(workflowSolicit.getReplyComment());
                    solicitComment.setSolicitComment(workflowSolicit.getSolicitComment());
                    solicitComment.setUserName(authService.getUserNameById(workflowSolicit.getAskFor()));
                    solicitComment.setCreatedTime(workflowSolicit.getCreatedTime());
                    solicitComment.setUpdatedTime(workflowSolicit.getUpdatedTime());
                    taskComments.add(solicitComment);
                }
            });
            task.setAssignee(authService.getUserNameById(task.getAssignee()));
            task.setOwner(authService.getUserNameById(task.getOwner()));
            String str = JSONKit.toJsonString(task);
            taskStage.put(str,taskComments);
        }
        taskNode.setTaskStage(taskStage);
        return taskNode;
    }



    @GetMapping(path = "/forwardActivityTask/{procInstId}")
    @ResponseBody
    public List<TaskDefinition>  getAllForwardActivityTask(@PathVariable("procInstId") String procInstId){
        List<ActivityImpl> allForwardActivities =
            workflowTaskService.getAllForwardActivities(procInstId);
        List<TaskDefinition> taskList = new ArrayList<>();
        for (ActivityImpl activity:allForwardActivities) {
            TaskDefinition taskDefinition =
                ((UserTaskActivityBehavior) activity.getActivityBehavior()).getTaskDefinition();
            taskList.add(taskDefinition);
        }
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        return taskList;
    }
}
