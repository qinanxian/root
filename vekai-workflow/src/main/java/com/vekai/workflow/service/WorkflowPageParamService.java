package com.vekai.workflow.service;

import cn.fisok.raw.kit.JSONKit;
import com.vekai.workflow.exception.WorkflowException;
import com.vekai.workflow.entity.WorkflowProc;
import com.vekai.workflow.nopub.service.WorkflowEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class WorkflowPageParamService {
    @Autowired
    private WorkflowEntityService workflowEntityService;

    public Map<String,Object> getPageParam(String procInstId){
        Map<String,Object> params = new HashMap<>();
        WorkflowProc workflowProc = workflowEntityService.getWorkflowProc(procInstId);
        if(null==workflowProc) throw new WorkflowException("不存在实例ID:{},对应的流程实例对象!", procInstId);
        String paramText = workflowProc.getPageParam();
        if(null==paramText||"".equals(paramText)) return params;
        params = JSONKit.jsonToBean(paramText, Map.class);
        return params;
    }

    /**
     * 更新整个字段的数值,覆盖当前参数数值
     * @param procInstId
     * @param params
     */
    public void updatePageParam(String procInstId, Map<String,Object> params){
        WorkflowProc workflowProc = workflowEntityService.getWorkflowProc(procInstId);
        if(null==workflowProc) throw new WorkflowException("不存在实例ID:{},对应的流程实例对象!", procInstId);
        String paramText = JSONKit.toJsonString(params);
        if(null!=paramText||!"".equals(paramText)){
            workflowProc.setPageParam(paramText);
            workflowEntityService.updateWorkflowProc(workflowProc);
        }
    }

    /**
     * 在字段原有数据基础上增加
     * 有则更新,无则增加
     * @param procInstId
     * @param params
     * @return
     */
    public Map<String,Object> appendPageParam(String procInstId, Map<String,Object> params){
        Map<String,Object> restParams = getPageParam(procInstId);
        restParams.putAll(params);
        updatePageParam(procInstId,restParams);
        return getPageParam(procInstId);
    }

    /**
     * 追加单条参数
     * @param procInstId
     * @param key
     * @param value
     * @return
     */
    public Map<String,Object> appendPageParam(String procInstId, String key, Object value){
        Map<String,Object> restParams = getPageParam(procInstId);
        restParams.put(key, value);
        updatePageParam(procInstId,restParams);
        return getPageParam(procInstId);
    }
}
