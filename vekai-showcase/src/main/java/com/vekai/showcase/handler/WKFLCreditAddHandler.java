package com.vekai.showcase.handler;

import cn.fisok.sqloy.core.BeanCruder;
import cn.fisok.sqloy.core.MapObjectCruder;
import com.vekai.auth.entity.User;
import com.vekai.auth.holder.AuthHolder;
import com.vekai.dataform.handler.impl.BeanDataOneHandler;
import com.vekai.dataform.model.DataForm;
import com.vekai.showcase.entity.DemoCredit;
import com.vekai.workflow.entity.WorkflowProc;
import com.vekai.workflow.model.ProcInstance;
import com.vekai.workflow.nopub.service.WorkflowEntityService;
import com.vekai.workflow.service.WorkflowPageParamService;
import com.vekai.workflow.service.WorkflowProcService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Component
public class WKFLCreditAddHandler extends BeanDataOneHandler {
    @Autowired
    private WorkflowProcService workflowProcService;
    @Autowired
    private WorkflowEntityService workflowEntityService;
    @Autowired
    private MapObjectCruder mapObjectCruder;
    @Autowired
    private BeanCruder beanCruder;
    @Autowired
    private WorkflowPageParamService workflowPageParamService;


    @Transactional
    @Override
    public int save(DataForm dataForm, Object object) {
        DemoCredit demoCredit = (DemoCredit) object;
        String custName = demoCredit.getCustName();

        String planName = custName + "-信贷业务";
        demoCredit.setPlanName(planName);


        User loginUser = AuthHolder.getUser();
        demoCredit.setOwnerId(loginUser.getId());

        // 保存新增数据
        int saveResult = super.save(dataForm, demoCredit);


        WorkflowProc workflowProc = this.startWorkflow(demoCredit.getPlanId(), planName);

        // 更新业务对象的状态
        demoCredit.setPlanStatus(workflowProc.getStatus());
        // 保存更新
        beanCruder.save(demoCredit);

        return saveResult;
    }


    private WorkflowProc startWorkflow(String planId, String planName) {
        Map<String, Object> variables = new HashMap<>();
        String key = "DemoCredit";
        String objectType = "DemoCredit";
        String objectId = planId;
        variables.put("ObjectId", objectId);
        variables.put("ObjectType", objectType);
        // xxx客户-信贷业务-个人业务
        variables.put("Summary", planName);

        String userId = AuthHolder.getUser().getId();

        // 启动流程
        ProcInstance procInst = workflowProcService.start(key, variables, userId);

        // 查询流程实例
        WorkflowProc workflowProc = workflowEntityService.getWorkflowProc(procInst.getId());

        return workflowProc;
    }


}
