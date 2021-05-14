package com.vekai.workflow.service;

import com.vekai.workflow.model.HistoricInstance;
import com.vekai.workflow.model.ProcComment;
import com.vekai.workflow.model.ProcInstance;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.impl.task.TaskDefinition;
import org.activiti.engine.repository.Model;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 流程实例操作Service
 */
public interface WorkflowProcService {

    /**
     * 启动流程
     * @param key
     * @param variables
     * @param userId
     * @return
     */
    ProcInstance start(String key, Map<String, Object> variables, String userId);

    /**
     * 流程暂停
     * @param procInstId
     */
    void suspend(String procInstId);

    /**
     * 流程激活
     * @param procInstId
     */
    void activate(String procInstId);

    /**
     * 流程师傅处于暂停状态
     * @param procInstId
     * @return
     */
    boolean isSuspended(String procInstId);

    /**
     * 获取流程实例
     * @param procInstId
     * @return
     */
    ProcInstance getProcInst(String procInstId);

    /**
     * 获取流程参数
     * @param procInstId
     * @return
     */
    Map<String, Object> getProcVariables(String procInstId);

    /**
     * 获取历史流程实例
     * @param instanceId
     * @return
     */
    HistoricInstance getHistInst(String instanceId);

    /**
     * 流程是否结束
     * @param procInstId
     * @return
     */
    boolean isFinished(String procInstId);

    /**
     * 获取流程的意见
     * @param procInstId
     * @return
     */
    List<ProcComment> getProcComments(String procInstId);

    /**
     * 判断流程是否处在并行阶段
     * @param procInstId
     * @return
     */
    boolean inParallelBranch(String procInstId);

    /**
     * 获取流程的所有任务定义
     * @param procInstId
     * @return
     */
    List<TaskDefinition> getTaskDefinitions(String procInstId);

    /**
     * 获取历史活动节点
     * @return
     */
    List<HistoricActivityInstance> getHisActInsts(String procInstId, boolean isFinished);

    /*
    * 模型导入替换
    * */
    void importUpdateDefinitionModel(String category, InputStream inputStream);

    /*
    * 模型导入新增
    * */
    Model importAddModel(InputStream inputStream);

    /**
     * 流程最深路径长度
     * 当前节点最深路径长度
     * @param procInstId
     * @param taskId
     * @return
     */
    List<String> getWorkflowProcessDepth(String procInstId,String taskId);
}
