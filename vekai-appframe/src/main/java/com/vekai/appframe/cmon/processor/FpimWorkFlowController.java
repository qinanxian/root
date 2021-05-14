package com.vekai.appframe.cmon.processor;

import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.workflow.entity.WorkflowProc;
import com.vekai.workflow.model.ProcTask;
import com.vekai.workflow.service.WorkflowTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/workflow/FpimWorkFlowController")
public class FpimWorkFlowController {

    @Autowired
    BeanCruder beanCruder;
    @Autowired
    WorkflowTaskService workflowTaskService;

    @GetMapping("/workFlowProcAndTask/{procDefKey}/{objectId}/{objectType}")
    public MapObject getWorkFlowProcAndTaskByObject(@PathVariable("procDefKey") String procDefKey,
                                                    @PathVariable("objectId") String objectId,
                                                    @PathVariable("objectType") String objectType) {
        MapObject mapObject = new MapObject();
        String sql = "SELECT * FROM WKFL_PROC WHERE PROC_DEF_KEY=:procDefKey AND OBJECT_ID=:objectId "
            + "AND OBJECT_TYPE=:objectType AND STATUS = 'presubmit' ";
        WorkflowProc workflowProc = beanCruder.selectOne(WorkflowProc.class, sql,
                MapKit.mapOf("procDefKey", procDefKey, "objectId", objectId, "objectType", objectType));
        if (null != workflowProc) {
            mapObject.put("procId", workflowProc.getProcId());
            mapObject.put("procInstId", workflowProc.getProcInstId());
            mapObject.put("summary", workflowProc.getSummary());
            mapObject.put("status", workflowProc.getStatus());
            mapObject.put("procDefId", workflowProc.getProcDefId());
            mapObject.put("pageParam", workflowProc.getPageParam());

            ProcTask latestTask = workflowTaskService.getLatestTask(workflowProc.getProcInstId());
            if (null != latestTask) {
                mapObject.put("taskId", latestTask.getId());
                mapObject.put("taskName", latestTask.getName());
            }
        }
        return mapObject;
    }


}
