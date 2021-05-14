//package com.vekai.showcase.rules.action;
//
//import com.bstek.urule.model.flow.ActionNode;
//import com.bstek.urule.model.flow.FlowAction;
//import com.bstek.urule.model.flow.FlowNode;
//import com.bstek.urule.model.flow.NodeEvent;
//import com.bstek.urule.model.flow.ins.FlowContext;
//import com.bstek.urule.model.flow.ins.ProcessInstance;
//import com.bstek.urule.runtime.KnowledgeSession;
//import com.vekai.urule.entity.Customer;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//
//import java.util.Map;
//
///**
// * Created by luyu on 2018/9/10.
// */
//@Component
//public class FlowActionOne implements FlowAction,NodeEvent {
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
//
//
//    @Override
//    public void execute(ActionNode actionNode, FlowContext flowContext, ProcessInstance processInstance) {
//        logger.info("--------动作节点1执行---------");
//        KnowledgeSession session = (KnowledgeSession) flowContext.getWorkingMemory();
//        Map<String, Object> params = session.getParameters();
//        params.put("isCreditPass",Boolean.TRUE);
//        Map<String, Object> bizObjects = session.getAllFactsMap();
//        Customer customer = (Customer)bizObjects.get(flowContext.getVariableCategoryClass("customer"));
//        customer.setAge(20);
//
//    }
//
//    @Override
//    public void enter(FlowNode flowNode, ProcessInstance processInstance, FlowContext flowContext) {
//        logger.info("--------进入动作节点1事件---------");
//    }
//
//    @Override
//    public void leave(FlowNode flowNode, ProcessInstance processInstance, FlowContext flowContext) {
//        logger.info("--------离开动作节点1事件---------");
//    }
//}
