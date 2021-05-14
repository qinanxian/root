package com.vekai.workflow.service;

import cn.fisok.raw.kit.MapKit;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.workflow.entity.WorkflowProc;
import com.vekai.workflow.entity.WorkflowTask;
import com.vekai.workflow.model.WorkflowConstant;
import com.vekai.workflow.nopub.service.WorkflowEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 开发人员使用
 * 对开发过程中常用的WKFL_*表的查询进行封装
 */
@Service
public class WorkflowQueryService {
    @Autowired
    private WorkflowEntityService workflowEntityService;
    @Autowired
    private BeanCruder beanCruder;

    /**
     * 通过业务对象获取流程实例对象
     * @param objectType
     * @param objectId
     * @return
     */
    public WorkflowProc getWorkflowProcByObj(String objectType,String objectId){
        WorkflowProc workflowProc = workflowEntityService.getWorkflowProcByObjTypeAndObjId(objectType, objectId);
        return workflowProc;
    }

    /**
     * 通过流程实例ID获取流程实例对象
     * @param procInstId
     * @return
     */
    public WorkflowProc getWorkflowProc(String procInstId){
        WorkflowProc workflowProc = workflowEntityService.getWorkflowProc(procInstId);
        return workflowProc;
    }

    /**
     * 从WKFL_TASK表中查询待处理任务列表
     * @param procInstId
     * @return
     */
    public List<WorkflowTask> getLatestWorkflowTasks(String procInstId){
        Map<String, String> whereMap =
                MapKit.mapOf("procId", procInstId);
        List<WorkflowTask> workflowTasks = beanCruder.selectList(WorkflowTask.class, "select * from "
                + WorkflowConstant.WKFL_TASK_TABLE +
                " where PROC_ID=:procId and FINISH_TYPE IS NULL AND FINISH_TIME IS NULL", whereMap);
        return workflowTasks;
    }

    /**
     * 从WKFL_TASK表中查询待处理任务列表-单个
     * @param procInstId
     * @return
     */
    public WorkflowTask getLatestWorkflowTask(String procInstId){
        Map<String, String> whereMap =
                MapKit.mapOf("procId", procInstId);
        WorkflowTask workflowTask = beanCruder.selectOne(WorkflowTask.class, "select * from "
                + WorkflowConstant.WKFL_TASK_TABLE +
                " where PROC_ID=:procId and FINISH_TYPE IS NULL AND FINISH_TIME IS NULL", whereMap);
        return workflowTask;
    }

    /**
     * 取工作流名称
     * @param workflowDefKey
     * @return
     */
    public String getWorkflowName(String workflowDefKey){
        return beanCruder.selectOne(String.class,"select NAME_ from ACT_RE_MODEL where KEY_ = :key","key", workflowDefKey);
    }
}
