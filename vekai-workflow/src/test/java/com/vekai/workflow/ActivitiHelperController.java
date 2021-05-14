package com.vekai.workflow;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.interceptor.CommandExecutor;
import org.activiti.engine.impl.persistence.entity.DeploymentEntity;
import org.activiti.engine.impl.persistence.entity.ModelEntity;
import org.activiti.engine.impl.persistence.entity.ResourceEntity;
import org.activiti.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;

@RestController
public class ActivitiHelperController {

    static final Logger LOGGER = LoggerFactory.getLogger(ActivitiHelperController.class);

    @Autowired
    RepositoryService repositoryService;

    @Autowired
    ProcessEngineConfigurationImpl processEngineConfiguration;

    CommandExecutor commandExecutor;

    @PostConstruct
    public void init() {
        commandExecutor = processEngineConfiguration.getCommandExecutor();
    }

    String MODEL_ID = "modelId";
    String MODEL_NAME = "name";
    String MODEL_REVISION = "revision";
    String MODEL_DESCRIPTION = "description";

    ObjectMapper objectMapper = new ObjectMapper();
    @RequestMapping(path = "/activiti/query/reModel", method = RequestMethod.GET)
    public String getRandomModelId() throws Exception{
        DeploymentEntity deploymentEntity = (DeploymentEntity) repositoryService.createDeploymentQuery().list().get(0);
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();

        BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
        BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
        ObjectNode editorNode = jsonConverter.convertToJson(bpmnModel);

        ModelEntity modelData = (ModelEntity) repositoryService.newModel();
        ObjectNode modelObjectNode = objectMapper.createObjectNode();
        modelObjectNode.put(MODEL_NAME, "reModel");
        modelObjectNode.put(MODEL_REVISION, 1);
        modelData.setMetaInfo(modelObjectNode.toString());
        modelData.setName("reModel");
        modelData.setDeploymentId(deploymentEntity.getId());
        System.out.println(modelData.getId());
        System.out.println(editorNode);
        repositoryService.saveModel(modelData);

        repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("UTF-8"));
        return modelData.getId();

//        return modelData.getId();


        //repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));

    }

    private static class GetResourceCmd implements Command<String> {
        private final String deploymentId;
        public GetResourceCmd(String deploymentId) {
            this.deploymentId = deploymentId;
        }

        @Override
        public String execute(CommandContext commandContext) {

            List<ResourceEntity> resourceEntities =
                    commandContext.getResourceEntityManager().findResourcesByDeploymentId(deploymentId);
            for (ResourceEntity resourceEntity : resourceEntities) {
                if (resourceEntity.getName().endsWith("bpmn") || resourceEntity.getName().endsWith("xml"))
                    return resourceEntity.getId();
            }
            return null;
        }
    }
}
