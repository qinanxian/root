//package com.vekai.showcase.rules.action;
//
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
//public class FlowStartAction implements NodeEvent {
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
//
//    @Override
//    public void enter(FlowNode flowNode, ProcessInstance processInstance, FlowContext flowContext) {
//        logger.info("--------进入决策流开始节点---------");
//    }
//
//    @Override
//    public void leave(FlowNode flowNode, ProcessInstance processInstance, FlowContext flowContext) {
//        logger.info("--------离开决策流开始节点---------");
//    }
//}
