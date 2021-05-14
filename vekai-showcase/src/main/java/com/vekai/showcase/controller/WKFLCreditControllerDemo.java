package com.vekai.showcase.controller;

import cn.fisok.sqloy.core.BeanCruder;
import cn.fisok.sqloy.core.MapObjectCruder;
import com.vekai.workflow.entity.WorkflowProc;
import com.vekai.workflow.entity.WorkflowTask;
import com.vekai.workflow.service.WorkflowPageParamService;
import com.vekai.workflow.service.WorkflowProcService;
import com.vekai.workflow.service.WorkflowQueryService;
import com.vekai.workflow.service.WorkflowTaskService;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/showcase/WKFLCreditControllerDemo")
public class WKFLCreditControllerDemo {

    @Autowired
    private WorkflowProcService workflowProcService;
    @Autowired
    private WorkflowTaskService workflowTaskService;
    @Autowired
    private WorkflowQueryService workflowQueryService;
    @Autowired
    private MapObjectCruder mapObjectCruder;
    @Autowired
    private BeanCruder beanCruder;
    @Autowired
    private WorkflowPageParamService workflowPageParamService;


    @RequestMapping(path = "/workflowProc/planId/{planId}", method = RequestMethod.GET)
    @ResponseBody
    public WorkflowProc getWorkflowProc(@PathVariable("planId") String planId){
        String objectType = "DemoCredit";

        // WKFL_开头的表,开发人员可以自己写sql来查询
        // 查询流程实例对象
        WorkflowProc workflowProc = workflowQueryService.getWorkflowProcByObj(objectType, planId);
        Validate.notNull(workflowProc,"流程实例不存在");

        // 查询流程当前待处理的任务
        List<WorkflowTask> latestWorkflowTasks = workflowQueryService.getLatestWorkflowTasks(workflowProc.getProcInstId());
        workflowProc.setLatestTasks(latestWorkflowTasks);

        return workflowProc;
    }

}
