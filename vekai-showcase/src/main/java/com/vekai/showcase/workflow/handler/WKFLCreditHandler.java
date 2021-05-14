package com.vekai.showcase.workflow.handler;

import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.showcase.entity.DemoCredit;
import com.vekai.workflow.entity.WorkflowProc;
import com.vekai.workflow.entity.WorkflowTask;
import com.vekai.workflow.handler.ProcHandlerAdapter;
import com.vekai.workflow.nopub.bom.BomContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class WKFLCreditHandler extends ProcHandlerAdapter{
    @Autowired
    private BeanCruder beanCruder;


    // proc 流程实例对象
    // srcTask 当前提交的任务对象
    // dscTasks 任务提交后生成的待提交任务
    @Override
    public void afterTaskCommit(WorkflowProc proc, WorkflowTask srcTask, List<WorkflowTask> dscTasks) {
        super.afterTaskCommit(proc, srcTask, dscTasks);

        if(proc!=null){
            String planId = proc.getObjectId();

            Map<String, String> parameter = new HashMap<>();
            parameter.put("planId", planId);

            DemoCredit demoCredit = beanCruder.selectOne(DemoCredit.class, "select * from DEMO_WKFL_CREDIT  where PLAN_ID=:planId", parameter);
            demoCredit.setPlanStatus(proc.getStatus());
            beanCruder.save(demoCredit);
        }

    }


    // 流程参数填充发发
    // 返回值与“流程参数配置”中的表达式相对应
    @Override
    public BomContext fillWorkflowParam(WorkflowProc proc, WorkflowTask task) {
        super.fillWorkflowParam(proc, task);
        BomContext bomContext = new BomContext();

        // 这里要判断流程是否发起
        // 流程参数的勾兑触发的位置主要有两类:1. 发起流程前  2. 任务提交前
        if(proc!=null) {
            // 查询业务对象
            String planId = proc.getObjectId();
            Map<String, String> parameter = new HashMap<>();
            parameter.put("planId", planId);
            DemoCredit demoCredit = beanCruder.selectOne(DemoCredit.class, "select * from DEMO_WKFL_CREDIT  where PLAN_ID=:planId", parameter);

            // 获取贷款金额
            bomContext.put("planAmt", demoCredit.getPlanAmt());


            // 设置参会人员
            // 实际开发过程
            // 1. 界面上使用选择参会人员的信息块选择参会人员
            // 2. 数据库存储参数人员
            // 3. 此处代码查询取出
            // 此处演示, 直接设置
            List<String> users = new ArrayList<>();
            users.add("hjin");
            users.add("hjzhou");
            users.add("hzheng2");

            bomContext.put("mUsers", users);

        }


        return bomContext;
    }
}
