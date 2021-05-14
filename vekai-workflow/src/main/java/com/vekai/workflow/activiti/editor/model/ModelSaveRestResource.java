package com.vekai.workflow.activiti.editor.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.vekai.workflow.exception.WorkflowException;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import static org.activiti.editor.constants.EditorJsonConstants.EDITOR_CHILD_SHAPES;

@RestController
@RequestMapping("workflow")
public class ModelSaveRestResource {
    static final Logger LOGGER = LoggerFactory.getLogger(ModelSaveRestResource.class);

    @Autowired
    RepositoryService repositoryService;

    final static String MODEL_NAME = "name";
    final static String MODEL_DESCRIPTION = "description";
    final String EDITOR_SHAPE_PROPERTIES = "properties";

    @Autowired
    ObjectMapper objectMapper;

    @RequestMapping(value="/model/{modelId}/save", method = RequestMethod.PUT)
    public void saveModel(@PathVariable String modelId, @RequestParam MultiValueMap<String, String> values) {
        try {

            Model model = repositoryService.getModel(modelId);

            ObjectNode modelJson = (ObjectNode) objectMapper.readTree(model.getMetaInfo());

            modelJson.put(MODEL_NAME, values.getFirst("name"));
            modelJson.put(MODEL_DESCRIPTION, values.getFirst("description"));
            /**
             * 设置流程模型的名称和原信息
             */
            model.setName(values.getFirst("name"));
            model.setMetaInfo(modelJson.toString());

            byte[] editorSource = values.getFirst("json_xml").getBytes("utf-8");

            String processId="";

            JsonNode objectNode = new ObjectMapper().readTree(editorSource);
            JsonNode childShapes = objectNode.get(EDITOR_CHILD_SHAPES);

            // 先从childShapes.properties.process_id节点读
            processId = objectNode.get(EDITOR_SHAPE_PROPERTIES).get("process_id").textValue();

            if(childShapes.size()>0){
                if(childShapes.size()==1) throw new WorkflowException("此流程疑似丢失流程连接线,请尝试在流程编辑器中点击保存)");
                JsonNode poolProperties = childShapes.get(0).get(EDITOR_SHAPE_PROPERTIES);

                /**
                 * 存在泳池时
                 * 网页版本流程编辑中有进去空白页面和点击泳池两地方设置key
                 * 经过测试发下,存在泳池时起到绝对作用的是泳池位置的key(关系到流程部署和启动)
                 * 空白页面中的key获取properties.process_id
                 * 泳池位置key获取childShapes.properties.process_id
                 */
                JsonNode pJsonNode = poolProperties.get("process_id");
                if(pJsonNode!=null){
                    processId = pJsonNode.textValue();
                    if("".equals(processId)) LOGGER.warn("尚未在泳池属性中设置流程的key");
                }
            }

            if ("".equals(processId)) LOGGER.warn("尚未在流程属性中设置流程的key");
            model.setKey(processId);

            repositoryService.saveModel(model);

            repositoryService.addModelEditorSource(model.getId(), editorSource);

            InputStream svgStream = new ByteArrayInputStream(values.getFirst("svg_xml").getBytes("utf-8"));
            TranscoderInput input = new TranscoderInput(svgStream);

            PNGTranscoder transcoder = new PNGTranscoder();
            // Setup output
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            TranscoderOutput output = new TranscoderOutput(outStream);

            // Do the transformation
            transcoder.transcode(input, output);
            final byte[] result = outStream.toByteArray();
            repositoryService.addModelEditorSourceExtra(model.getId(), result);
            outStream.close();

        } catch (Exception e) {
            LOGGER.error("Error saving model", e);
            throw new ActivitiException("Error saving model", e);
        }
    }
}
