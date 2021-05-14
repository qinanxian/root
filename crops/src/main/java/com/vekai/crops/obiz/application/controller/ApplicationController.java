package com.vekai.crops.obiz.application.controller;

import com.vekai.crops.constant.BizConst;
import com.vekai.crops.dossier.service.DossierService;
import com.vekai.crops.obiz.application.dao.ApplicationDao;
import com.vekai.crops.obiz.application.entity.ObizApplication;
import com.vekai.crops.obiz.application.model.ApplicationTaskState;
import com.vekai.appframe.policy.model.PolicyDefinition;
import com.vekai.appframe.policy.service.PolicyService;
import com.vekai.auth.entity.User;
import com.vekai.auth.holder.AuthHolder;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.kit.ValidateKit;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.workflow.entity.WorkflowProc;
import com.vekai.workflow.entity.WorkflowTask;
import com.vekai.workflow.model.ProcInstance;
import com.vekai.workflow.model.ProcTask;
import com.vekai.workflow.service.WorkflowPageParamService;
import com.vekai.workflow.service.WorkflowProcService;
import com.vekai.workflow.service.WorkflowQueryService;
import com.vekai.workflow.service.WorkflowTaskService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/application")
public class ApplicationController {
    @Autowired
    private BeanCruder beanCruder;
    @Autowired
    private ApplicationDao applicationDao;
    @Autowired
    private WorkflowProcService workflowProcService;
    @Autowired
    private WorkflowTaskService workflowTaskService;
    @Autowired
    private WorkflowQueryService workflowQueryService;
    @Autowired
    private WorkflowPageParamService workflowPageParamService;
    @Autowired
    private PolicyService policyService;
    @Autowired
    private DossierService dossierService;

    @ApiOperation(value = "取业务的控制参数",response = ObizApplication.class)
    @GetMapping("/policy/{applicationId}")
    public ObizApplication getApplicationPolicy(@PathVariable("applicationId") String applicationId){
        ObizApplication application = applicationDao.queryApplicationProfile(applicationId);
        return application;
    }

    /**
     * 取当前登录用户对和指定申请的任务关联对象
     */
    @ApiOperation(value = "获取一笔申请的流程任务状态描述",response = WorkflowProc.class)
    @GetMapping("/workflow/taskStateDescription/{applicationId}")
    public ApplicationTaskState getTaskStateDescription (@PathVariable("applicationId") String applicationId){
        User user = AuthHolder.getUser();
        String userId = (user != null) ? user.getId():"";
        ApplicationTaskState appTaskState = new ApplicationTaskState();

        WorkflowProc workflowProc = workflowQueryService.getWorkflowProcByObj(BizConst.APPLICATION, applicationId);

        if(workflowProc == null){//还没有发起流程
            appTaskState.setTaskState(ApplicationTaskState.NOT_YET);
        }else{
            //给定初始值
            ProcTask procTask = workflowTaskService.getLatestTask(workflowProc.getProcInstId());
            appTaskState.setProcDefKey(workflowProc.getProcDefKey());
            appTaskState.setProcName(workflowProc.getProcName());
            appTaskState.setTaskState(ApplicationTaskState.NOT_CARE);

            if(procTask != null){
                appTaskState.setTaskId(procTask.getId());
                appTaskState.setTaskName(procTask.getName());
                //当前用户是发起人,并且流程没有提交
                if(userId.equals(workflowProc.getSponsor()) && workflowTaskService.isFirstTask(procTask.getId())){
                    appTaskState.setTaskState(ApplicationTaskState.FIRST_UN_SUBMIT);
                }else if(userId.equals(workflowProc.getSponsor())){
                    appTaskState.setTaskState(ApplicationTaskState.FIRST_SUBMITTED);
                }
            }
        }
        return appTaskState;

    }


    @ApiOperation(value = "发起申请流程",response = WorkflowProc.class)
    @PostMapping("/workflow/start/{applicationId}")
    public WorkflowTask start(@PathVariable("applicationId") String applicationId){
        ObizApplication application = applicationDao.queryApplicationProfile(applicationId);
        ValidateKit.notNull(application,"业务对象:{0}不存在", applicationId);

        //检测是否已经发起流程
        WorkflowProc workflowProc = workflowQueryService.getWorkflowProcByObj(BizConst.APPLICATION, applicationId);
        if(workflowProc==null){
            String workflowKey = application.getWorkflowKey();
            String summary = this.genWorkflowSummary(application.getCustName(), application.getPolicyId(), workflowKey);

            User loginUser = AuthHolder.getUser();
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.put("ObjectId", applicationId);
            variables.put("ObjectType", BizConst.APPLICATION);
            variables.put("Summary", summary);
            ProcInstance procInstance = workflowProcService.start(workflowKey, variables, loginUser.getId());
            String processInstanceId = procInstance.getProcessInstanceId();
            workflowProc = workflowQueryService.getWorkflowProc(processInstanceId);

            String custId = application.getCustId();

            // 业务详情的DataformId存储到PageParam
            String dataformId = application.getDataformId();

            // 资料清单
            String dossierId = application.getDossierId();
            ValidateKit.notNull(dossierId, "资料清单Id获取失败,ApplicationId:{0}", applicationId);

            String inquireDocId = application.getInquireDocId();
            ValidateKit.notBlank(dossierId, "获取资料清单编号为空");
            Map<String, Object> pageParam = new HashMap<>();
            pageParam.put("custId", custId);
            pageParam.put("dataformId", dataformId);
            pageParam.put("dossierId", dossierId);
            pageParam.put("inquireDocId", inquireDocId);

            workflowPageParamService.appendPageParam(processInstanceId, pageParam);
        }

        WorkflowTask latestWorkflowTask = workflowQueryService.getLatestWorkflowTask(workflowProc.getProcInstId());
        latestWorkflowTask.setWorkflowProc(workflowProc);

        return latestWorkflowTask;
    }

    /**
     * 组装流程简介
     * @param custName
     * @param policyId
     * @param workflowKey
     * @return
     */
    private String genWorkflowSummary(String custName, String policyId, String workflowKey){
        PolicyDefinition policyDefinition = policyService.getPolicyDefinition(policyId);
        String policyName = policyDefinition.getPolicyName();
        String workflowDefName = workflowQueryService.getWorkflowName(workflowKey);

        ValidateKit.notBlank(policyName,"产品名称:{0}为空", policyId);
        ValidateKit.notBlank(workflowDefName,"流程定义名称:{0}为空", workflowDefName);

        return StringKit.format("{0}-{1}-{2}",custName,policyName,workflowDefName);
    }
}
