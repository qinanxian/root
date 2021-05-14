package com.vekai.showcase.handler;

import com.vekai.dataform.handler.impl.BeanDataOneHandler;
import org.springframework.stereotype.Component;

@Component
public class WKFLCreditAddHandler extends BeanDataOneHandler {
//    @Autowired
//    private WorkflowProcService workflowProcService;
//    @Autowired
//    private WorkflowEntityService workflowEntityService;
//    @Autowired
//    private MapObjectCruder mapObjectCruder;
//    @Autowired
//    private BeanCruder beanCruder;
//    @Autowired
//    private WorkflowPageParamService workflowPageParamService;
//
//
//
//    @Transactional
//    @Override
//    public int save(DataForm dataForm, Object object) {
//        DemoCredit demoCredit = (DemoCredit) object;
//        String custName = demoCredit.getCustName();
//
//        String planName = custName+"-信贷业务";
//        demoCredit.setPlanName(planName);
//
//
//        User loginUser = AuthHolder.getUser();
//        demoCredit.setOwnerId(String.valueOf(loginUser.getId()));
//
//        // 保存新增数据
//        int saveResult = super.save(dataForm, demoCredit);
//
//
//        WorkflowProc workflowProc = this.startWorkflow(demoCredit.getPlanId(), planName);
//
//        // 更新业务对象的状态
//        demoCredit.setPlanStatus(workflowProc.getStatus());
//        // 保存更新
//        beanCruder.save(demoCredit);
//
//        return saveResult;
//    }
//
//
//    private WorkflowProc startWorkflow(String planId, String planName){
//        Map<String, Object> variables = new HashMap<>();
//        String key = "DemoCredit";   //流程定义key
//        String objectType = "DemoCredit"; //业务对象类型  例如：授信申请，合同审批等等
//        String objectId = planId;  //业务对象的流水号   objectType和ObjectId 确定唯一一笔业务 并 和流程实例绑定
//        variables.put("ObjectId", objectId);
//        variables.put("ObjectType", objectType);
//        // xxx客户-信贷业务-个人业务
//        variables.put("Summary", planName); //流程实例的简介   xxx客户-信贷业务-个人业务  格式自定义
//
//        Long userId = AuthHolder.getUser().getId();  //获取当前登录用户 一般流程发起者为当前登录用户 也可手动指定发起人
//
//        // 启动流程
//        ProcInstance procInst = workflowProcService.start(key, variables, userId);
//
//        // 查询流程实例
//        WorkflowProc workflowProc = workflowEntityService.getWorkflowProc(procInst.getId());
//
//        return workflowProc;
//    }


}
