package com.vekai.workflow.nopub.service.impl;

import cn.fisok.sqloy.core.BeanCruder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.kit.ValidateKit;
import com.vekai.workflow.exception.WorkflowException;
import com.vekai.workflow.nopub.service.WorkflowDefinitionService;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.exceptions.XMLException;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.persistence.entity.ModelEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ModelQuery;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 流程定义
 *
 * @author 姚启扬<qyyao@amarsoft.com>
 * @date 2017-12-28
 */
@Component
public class WorkflowDefinitionServiceImpl implements WorkflowDefinitionService {
    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    private BeanCruder beanCruder;

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Override
    public long deployedCount() {
        return repositoryService.createProcessDefinitionQuery().count();
    }

    @Override
    public String getDefinitionModelAsXml(String modelId) {
        byte[] bpmnBytes = getDefinitionModelAsBytes(modelId);
        ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);
        try {
            int count = in.available();
            byte[] contents = new byte[count];
            in.read(contents);
            String xmlStr = new String(contents);
            return xmlStr;
        } catch (IOException e) {
            logger.error("流程模型数据转XML出错", e);
            throw new WorkflowException(e);
        }
    }

    @Override
    public byte[] getDefinitionModelAsBytes(String modelId) {
        BpmnModel bpmnModel = getBpmnModel(modelId);
        BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
        byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);//把BpmnModel对象转换为XML格式
        return bpmnBytes;
    }

    @Transactional
    @Override
    public String deployDefinitionModel(String modelId) throws XMLException {
        BpmnModel bpmnModel = getBpmnModel(modelId);
        Model modelData = repositoryService.getModel(modelId);
        String processName = modelData.getName() + "bpmn20.xml";
        Deployment deployment;
        String deploymentId = "";
        deployment = repositoryService.createDeployment().name(modelData.getName())
            .addBpmnModel(processName, bpmnModel).deploy();
        if (null != deployment) {
            deploymentId = deployment.getId();
            modelData.setDeploymentId(deploymentId);
            repositoryService.saveModel(modelData);
        }
        return deploymentId;
    }

    @Transactional
    @Override
    public void deleteDefinitionModel(String modelId) {
        repositoryService.deleteModel(modelId);
    }


    @Override
    public List<ModelEntity> getDefinitionModels() {
        ModelQuery modelQuery = repositoryService.createModelQuery();
        List<ModelEntity> modelEntityList = new ArrayList<>();
        List<Model> modelList = modelQuery.list();
        try {
            for (Model model : modelList) {
                ModelEntity modelEntity = (ModelEntity) model;
                modelEntityList.add(modelEntity);
            }
        } catch (Exception e) {
            logger.error("没有可用的流程模型!", e);
            throw new WorkflowException(e);
        }
        return modelEntityList;
    }

    @Transactional
    @Override
    public void upgradeProcInstDef(String modelId) {
        ValidateKit.notBlank(modelId, "传入的参数modelId为空");
        Model modelData = repositoryService.getModel(modelId);
        //这是最新的流程定义
        String deploymentId = modelData.getDeploymentId();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId).singleResult();

        //更新流程实例相关表字段 ID_,KEY_,VERSION_,DEPLOYMENT_ID_
        String id = processDefinition.getId();
        String key = processDefinition.getKey();

        Validate.notNull(id, "获取新的部署定义Id为空");
        Validate.notNull(key, "流程定义KEY为空");

        beanCruder
                .execute("update ACT_HI_ACTINST set PROC_DEF_ID_ =:id where PROC_INST_ID_ in (select PROC_ID from WKFL_PROC where PROC_DEF_KEY=:key AND FINISH_TIME IS NULL)",
                        MapKit.mapOf("id", id, "key", key));
        beanCruder
                .execute("update ACT_HI_PROCINST set PROC_DEF_ID_ =:id where ID_ in (select PROC_ID from WKFL_PROC where PROC_DEF_KEY=:key AND FINISH_TIME IS NULL)",
                        MapKit.mapOf("id", id, "key", key));
        beanCruder
                .execute("update ACT_HI_TASKINST set PROC_DEF_ID_ =:id where PROC_INST_ID_ in (select PROC_ID from WKFL_PROC where PROC_DEF_KEY=:key AND FINISH_TIME IS NULL)",
                        MapKit.mapOf("id", id, "key", key));
        beanCruder
                .execute("update ACT_RU_TASK set PROC_DEF_ID_ =:id where PROC_INST_ID_ in (select PROC_ID from WKFL_PROC where PROC_DEF_KEY=:key AND FINISH_TIME IS NULL)",
                        MapKit.mapOf("id", id, "key", key));
        beanCruder
                .execute("update ACT_RU_EXECUTION set PROC_DEF_ID_ =:id where PROC_INST_ID_ in (select PROC_ID from WKFL_PROC where PROC_DEF_KEY=:key AND FINISH_TIME IS NULL)",
                        MapKit.mapOf("id", id, "key", key));

        beanCruder
                .execute("update WKFL_PROC set PROC_DEF_ID =:id where PROC_DEF_KEY=:key AND FINISH_TIME IS NULL",
                        MapKit.mapOf("id", id, "key", key));

    }


    private BpmnModel getBpmnModel(String modelId) {
        Model modelData = repositoryService.getModel(modelId);
        BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
        JsonNode editorNode = null;//从模型对象中读取JSON数据源
        try {
            editorNode =
                new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelData
                    .getId()));
        } catch (IOException e) {
            logger.error("获取可编辑流程模型出错", e);
            throw new WorkflowException(e);
        }

        JsonNode childShapes = editorNode.get("childShapes");
        if (childShapes.size() > 0 && childShapes.size() == 1)
            throw new WorkflowException("程设计XML获取异常,疑似丢失流程连接线,请尝试在流程编辑器中点击保存");

        return jsonConverter.convertToBpmnModel(editorNode);
    }


}
