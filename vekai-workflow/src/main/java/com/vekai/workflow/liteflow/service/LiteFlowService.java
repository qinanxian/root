package com.vekai.workflow.liteflow.service;

import com.vekai.workflow.entity.WorkflowComment;
import com.vekai.workflow.entity.WorkflowProc;
import com.vekai.workflow.entity.WorkflowTask;
import com.vekai.workflow.liteflow.entity.LiteflowForm;
import com.vekai.workflow.liteflow.entity.LiteflowResource;
import com.vekai.workflow.liteflow.entity.LiteflowTemplate;
import com.vekai.workflow.model.enums.TaskStatusEnum;

import java.util.List;
import java.util.Map;

/**
 * 简式流程接口
 */

public interface LiteFlowService {

    /**
     * 简式流程启动
     * @param expr
     * @param procDefKey
     * @param variables
     * @param userId
     * @return
     */
    WorkflowProc start(String expr, String procDefKey, Map<String, Object> variables, String userId);



    /**
     * 简式流程提交
     * @param taskId
     * @param variables
     * @param userId
     * @param comment
     * @return
     */
    List<WorkflowTask> commit(String taskId, Map<String, Object> variables, String userId, String comment);


    /**
     * 流程否决
     * @param taskId
     * @param userId
     * @param comment
     */
    void reject(String taskId, String userId, String comment);


    /**
     * 流程废弃
     * @param taskId
     * @param userId
     * @param comment
     */
    void abolish(String taskId,String userId,String comment);

    /**
     * 流程驳回
     * @param srcTaskId
     * @param dscTaskKey
     * @param userId
     * @param comment
     * @return
     */
    WorkflowTask backOrigin(String srcTaskId, String dscTaskKey, String userId, String comment);



    /**
     * 添加意见
     * @param taskId
     * @param userId
     * @param comment
     */

    void addComment(String taskId, String userId, String comment,TaskStatusEnum statusEnum);



    /**
     * 获取简式流程首个意见
     * @param taskId
     * @return
     */
    WorkflowComment firstComment(String taskId);



    /**
     * 获取流程资源列表
     * @param procDefKey
     * @return
     */
    List<LiteflowResource> getResourceList(String procDefKey);


    /**
     * 获取流程审批单
     * @return
     */
    List<LiteflowForm> getLiteForm();


    /**
     * 获取表达式预设模板
     * @param category
     * @return
     */
    List<LiteflowTemplate> getTemplate(String category);

    void saveTemplate(String name,String expr);

    void updateTemplate(String templateId,String expr);

}
