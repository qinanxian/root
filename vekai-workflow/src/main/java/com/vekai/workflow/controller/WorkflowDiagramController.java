package com.vekai.workflow.controller;

import com.vekai.auth.entity.Role;
import com.vekai.auth.entity.User;
import com.vekai.auth.service.AuthService;
import cn.fisok.raw.kit.DateKit;
import com.vekai.workflow.controller.model.HistoricTaskDesc;
import com.vekai.workflow.model.HistoricActivity;
import com.vekai.workflow.model.HistoricTask;
import com.vekai.workflow.model.ProcTask;
import com.vekai.workflow.model.enums.TaskStatusEnum;
import com.vekai.workflow.service.WorkflowProcService;
import com.vekai.workflow.service.WorkflowTaskService;
import io.swagger.annotations.Api;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@Api(description = "流程图进度查看")
@Controller
@RequestMapping("/workflow/diagram")
public class WorkflowDiagramController {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private WorkflowTaskService workflowTaskService;
    @Autowired
    private WorkflowProcService workflowProcService;
    @Autowired
    protected AuthService authService;


    @RequestMapping(path = "/hisActinsts/{procInstId}", method = RequestMethod.GET)
    @ResponseBody
    public List<HistoricActivity> diagramHisActinsts(
            @PathVariable("procInstId") String procInstId) {
        List<HistoricActivityInstance> histActInsts = workflowProcService.getHisActInsts(procInstId, false);

        List<HistoricTask> histTasks = workflowTaskService.getHistTasks(procInstId);
        Map<String,HistoricTask> allHisTasks = new HashMap<>();
        histTasks.stream().filter(histTask->histTask!=null&&histTask.getTaskDefinitionKey()!=null).forEach((hisActInst)->{
            allHisTasks.put(hisActInst.getId(), hisActInst);
        });

        // 用来过滤出最新的任务
        Map<String,HistoricActivityInstance> recentTaskFilter = new HashMap<>();

        histActInsts.stream().filter(histActInst->histActInst!=null&&histActInst.getActivityId()!=null).forEach((histActInst)->{
            recentTaskFilter.put(histActInst.getActivityId(), histActInst);
        });

        List<HistoricActivityInstance> rstHistActs = new ArrayList<>();
        Iterator iter = recentTaskFilter.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            HistoricActivityInstance histInstTmp = (HistoricActivityInstance)entry.getValue();
            String taskId = histInstTmp.getTaskId();
            HistoricTaskInstance historicTaskInstance = allHisTasks.get(taskId);

            HistoricActivity historicActivity = new HistoricActivity(histInstTmp);

            if(historicTaskInstance!=null) {
                historicActivity.setAssignee(historicTaskInstance.getAssignee());
                historicActivity.setEndTime(historicTaskInstance.getEndTime());
            }
            rstHistActs.add(historicActivity);
        }


        return this.parseHistoricActivityAssignee(rstHistActs);
    }

    /**
     * 将受理人id转化为用户名(用户code)
     *
     * @return
     */
    private List<HistoricActivity> parseHistoricActivityAssignee(List<HistoricActivityInstance> hisActInsts){
        List<HistoricActivity> historicActivitys = new ArrayList<>();

        for (HistoricActivityInstance hisActInt:hisActInsts) {
            HistoricActivity historicActivity = new HistoricActivity(hisActInt);
            String assignee = hisActInt.getAssignee();
            if(assignee!=null) {
                User user = authService.getUser(assignee);
                if(user!=null) historicActivity.setAssignee(user.getName()+"("+user.getCode()+")");
            }

            historicActivitys.add(historicActivity);
        }
        return historicActivitys;
    }

    /**
     *  流程图查看任务权限人和组
     * @param procInstId
     * @param taskKey
     * @return
     */
    @RequestMapping(path = "/latestTasksCandidates/{procInstId}/{taskKey}", method =
            RequestMethod.GET)
    @ResponseBody
    public HashMap<String, Object> diagramLatestTasksCandidates(
            @PathVariable("procInstId") String procInstId,
            @PathVariable("taskKey") String taskKey){
        HashMap<String, Object> candidates = this.getLatestTasksCandidates(procInstId, taskKey);
        List<String> candidateGroups = (List<String>) candidates.get("candidateGroups");
        List<String> candidateUsers = (List<String>) candidates.get("candidateUsers");

        HashMap<String, Object> rstCandidates = new HashMap<>();
        List<String> rstCandidateGroups = new ArrayList<>();
        List<String> rstCandidateUsers = new ArrayList<>();

        for (int i=0;i<candidateGroups.size();i++) {
            String roleId = candidateGroups.get(i);
            Role role = authService.getRole(roleId);
            if(role!=null) {
                String groupStr = role.getName()+"("+role.getCode()+")";
                rstCandidateGroups.add(groupStr);
            }

        }

        for (int i=0;i<candidateUsers.size();i++) {
            String userId = candidateUsers.get(i);
            String groupStr = convertUsername(userId);
            if(groupStr!=null) rstCandidateUsers.add(groupStr);

        }

        rstCandidates.put("candidateGroups", rstCandidateGroups);
        rstCandidates.put("candidateUsers", rstCandidateUsers);


        return rstCandidates;

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

    private String  convertUsername(String userId){
        User user = authService.getUser(userId);
        if(user!=null) {
            String groupStr = user.getName()+"("+user.getCode()+")";

            return groupStr;
        }

        return null;
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
     * 历史任务描述
     * @param taskId
     * @return
     */
    @RequestMapping(path = "/histTaskDesc/{taskId}", method = RequestMethod.GET)
    @ResponseBody
    public HistoricTaskDesc histTaskDesc(@PathVariable("taskId") String taskId) {
        HistoricTask historicTask = workflowTaskService.getHistTask(taskId);
        HistoricTaskDesc historicTaskDesc = new HistoricTaskDesc(historicTask);



        List<HistoricTask> histTasks = workflowTaskService.getHistTasks(historicTask.getProcessInstanceId(), true);
        StringBuilder descHtml = new StringBuilder();
        histTasks.stream().filter(hisTask -> hisTask!=null&&historicTask.getTaskDefinitionKey().equals(hisTask.getTaskDefinitionKey())).forEach((hisTask) -> {
            String assgineeDesc = hisTask.getAssignee()!=null?this.convertUsername(hisTask.getAssignee()):"无";
            String dealType = (hisTask.getDeleteReason()!=null&&!"".equals(hisTask.getDeleteReason()))?TaskStatusEnum.getEnum(hisTask.getDeleteReason()).getLabel():"无";
            descHtml.append(DateKit.format(hisTask.getEndTime())+"-"+assgineeDesc+"-"+dealType+"<br/>");
        });

        historicTaskDesc.setTraceDesc(descHtml.toString());
        return historicTaskDesc;
    }
}

