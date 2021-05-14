package com.vekai.crops.obiz.application.handler;

import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.crops.constant.BizConst;
import com.vekai.crops.constant.dict.DictAppStatus;
import com.vekai.common.landmark.service.LandmarkService;
import cn.fisok.raw.kit.MapKit;
import com.vekai.workflow.entity.WorkflowProc;
import com.vekai.workflow.entity.WorkflowTask;
import com.vekai.workflow.handler.ProcHandlerAdapter;
import com.vekai.workflow.model.enums.ProcFinishEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplicationWorkflowHandler extends ProcHandlerAdapter{
    @Autowired
    private BeanCruder beanCruder;
    @Autowired
    LandmarkService landmarkService;


    @Override
    public void afterProcStart(WorkflowProc proc, List<WorkflowTask> tasks) {
        super.afterProcStart(proc, tasks);

        if(proc!=null){
            String applicationId = proc.getObjectId();
            this.updateLandmarkStep(applicationId, "App1020");
        }
    }

    @Override
    public void afterTaskCommit(WorkflowProc proc, WorkflowTask srcTask, List<WorkflowTask> dscTasks) {
        super.afterTaskCommit(proc, srcTask, dscTasks);

        if(proc!=null){
            String applicationId = proc.getObjectId();
            if("sponsor".equals(srcTask.getTaskDefKey())) {
                this.updateStatus(applicationId, DictAppStatus.IN_PROCESS);
                this.updateLandmarkStep(applicationId, "App20");
            }
        }

    }

    @Override
    public void afterProcFinished(ProcFinishEnum finishType, WorkflowProc proc, WorkflowTask task) {
        super.afterProcFinished(finishType, proc, task);
        String applicationId = proc.getObjectId();
        // 流程正常完成
        if(finishType.equals(ProcFinishEnum.COMPLETED)){
            this.updateStatus(applicationId, DictAppStatus.ENABLED);
            this.updateLandmarkStep(applicationId, "App6010");
        } else {
            this.updateStatus(applicationId, DictAppStatus.DISABLED);
            this.updateLandmarkStep(applicationId, "App6020");
        }

    }

    private void updateLandmarkStep(String applicationId, String itemKey){
        landmarkService.activeSetp(applicationId, BizConst.APPLICATION, itemKey);
        beanCruder.execute("update obiz_application set APP_MILESTONE=:key where APPLICATION_ID=:appId",
                MapKit.mapOf("appId", applicationId, "key", itemKey));
    }

    private void updateStatus(String applicationId, String status){
        beanCruder.execute("update obiz_application set APP_STATUS=:status where APPLICATION_ID=:appId",
                MapKit.mapOf("appId", applicationId, "status", status));
    }

}
