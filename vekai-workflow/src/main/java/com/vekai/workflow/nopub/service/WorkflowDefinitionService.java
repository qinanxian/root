package com.vekai.workflow.nopub.service;

import org.activiti.engine.form.FormProperty;
import org.activiti.engine.impl.persistence.entity.ModelEntity;
import org.activiti.engine.impl.task.TaskDefinition;

import java.util.List;

/**
 * 流程管理接口的设计
 */
public interface WorkflowDefinitionService {

    long deployedCount();

    String getDefinitionModelAsXml(String modelId);

    byte[] getDefinitionModelAsBytes(String modelId);

    String  deployDefinitionModel(String modelId);

    void deleteDefinitionModel(String modelId);

    List<ModelEntity> getDefinitionModels();

    /**
     * 强制更新存活流程实例所使用的流程定义到最新版
     * 谨慎使用
     * 特别是新版本的流程设计对已走过的任务节点定义进行修改后,流程退回可能会存在问题
     * @param modelId
     */
    void upgradeProcInstDef(String modelId);
}
