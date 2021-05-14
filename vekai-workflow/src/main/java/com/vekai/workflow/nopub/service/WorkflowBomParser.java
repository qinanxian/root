package com.vekai.workflow.nopub.service;

import com.vekai.workflow.entity.WorkflowProc;
import com.vekai.workflow.entity.WorkflowTask;
import com.vekai.workflow.exception.WorkflowException;
import com.vekai.workflow.handler.ProcHandler;
import com.vekai.workflow.nopub.bom.BomContext;
import com.vekai.workflow.nopub.bom.BomLoader;
import com.vekai.workflow.nopub.bom.BomObject;
import com.vekai.workflow.nopub.bom.BomParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程业务参数解析
 * @Auther 左晓敏 <xmzuo@amarsoft.com>
 * @Date 2018-02-06
 */
@Component
public class WorkflowBomParser {
    @Autowired
    private BomLoader bomLoader;
    @Autowired
    private BomParser bomParser;

    private final static String FILL_WORKFLOW_PARAM_NAME="fillWorkflowParam";


    public Map<String, Object> parse(String workflowDefKey, List<ProcHandler> procHandlers, WorkflowProc proc, WorkflowTask task){
        ProcHandler procHandler = filterFillWorkflowParamHandler(procHandlers);
        if(null==procHandler) return new HashMap<>();

        //1.加载BOM模型
        BomObject bom = bomLoader.load(workflowDefKey);
        //2.设置上下文原始业务参数-----------------从handler中的fillWorkflowParam结果中获取
        BomContext ctx = procHandler.fillWorkflowParam(proc,task);
        if (null == ctx || ctx.isEmpty()) return new HashMap<>();
        //3.解析规则
        bomParser.parse(bom,ctx);
        //4.查看结果
        return bom.flatValue();
    }


    /**
     * 检查流程handler的业务参数填充方法,并获取存在业务参数填充方法的handler(可为null)
     * @param procHandlers
     * @return
     */
    private ProcHandler filterFillWorkflowParamHandler(List<ProcHandler> procHandlers){
        ProcHandler procFillWorkflowParamHandler = null;
        int fillWorkflowParamNum = 0;
        for (ProcHandler procHandler : procHandlers) {
            Class hdClass = procHandler.getClass();
            for (Method method : hdClass.getDeclaredMethods()) {
                if(FILL_WORKFLOW_PARAM_NAME.equals(method.getName())) {
                    procFillWorkflowParamHandler = procHandler;
                    fillWorkflowParamNum++;
                }
                if(fillWorkflowParamNum>1) {
                    throw new WorkflowException("流程业务参数填充方法fillWorkflowParam只能在一个handler中实现一次!");
                }
            }
        }
        return procFillWorkflowParamHandler;
    }
}
