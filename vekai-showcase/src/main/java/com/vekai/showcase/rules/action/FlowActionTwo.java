//package com.vekai.showcase.rules.action;
//
//import com.bstek.urule.model.flow.ActionNode;
//import com.bstek.urule.model.flow.FlowAction;
//import com.bstek.urule.model.flow.FlowNode;
//import com.bstek.urule.model.flow.NodeEvent;
//import com.bstek.urule.model.flow.ins.FlowContext;
//import com.bstek.urule.model.flow.ins.ProcessInstance;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
///**
// * Created by luyu on 2018/9/10.
// */
//@Component
//public class FlowActionTwo implements FlowAction,NodeEvent {
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    @Override
//    public void execute(ActionNode actionNode, FlowContext flowContext, ProcessInstance processInstance) {
//        logger.info("--------动作节点2执行---------");
//    }
//
//    @Override
//    public void enter(FlowNode flowNode, ProcessInstance processInstance, FlowContext flowContext) {
//        logger.info("--------进入动作节点2事件---------");
//    }
//
//    @Override
//    public void leave(FlowNode flowNode, ProcessInstance processInstance, FlowContext flowContext) {
//        logger.info("--------离开动作节点2事件---------");
//    }
//}
