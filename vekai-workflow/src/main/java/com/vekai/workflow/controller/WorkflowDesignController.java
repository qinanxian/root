package com.vekai.workflow.controller;

import cn.fisok.web.kit.HttpKit;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cn.fisok.raw.holder.MessageHolder;
import cn.fisok.raw.kit.DateKit;
import cn.fisok.raw.lang.BizException;
import com.vekai.workflow.exception.WorkflowException;
import com.vekai.workflow.nopub.kit.FileOperationKit;
import com.vekai.workflow.nopub.service.WorkflowDefinitionService;
import com.vekai.workflow.service.WorkflowProcService;
import io.swagger.annotations.Api;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(description = "流程设计接口")
@Controller
@RequestMapping("/workflow/design")
public class WorkflowDesignController {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    static final Logger LOGGER = LoggerFactory.getLogger(WorkflowDesignController.class);

    @Autowired
    private RepositoryService repositoryService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    private WorkflowDefinitionService definitionService;
    @Autowired
    private WorkflowProcService workflowProcService;


    @RequestMapping(value = "/model/addModel/{category}", method = RequestMethod.POST)
    @ResponseBody
    public void addModel(@PathVariable String category) {
        RepositoryService repositoryService = processEngine.getRepositoryService();
        //初始化一个空模型
        Model model = repositoryService.newModel();

        //设置一些默认信息，可以用参数接收
        String name = "";
        String description = "";
        int revision = 1;
        String key = "";

        ObjectNode modelNode = objectMapper.createObjectNode();
        modelNode.put(ModelDataJsonConstants.MODEL_NAME, name);
        modelNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
        modelNode.put(ModelDataJsonConstants.MODEL_REVISION, revision);

        model.setName(name);
        model.setKey(key);
        model.setMetaInfo(modelNode.toString());
        model.setCategory(category);

        repositoryService.saveModel(model);
        String id = model.getId();

        //完善ModelEditorSource
        ObjectNode editorNode = objectMapper.createObjectNode();
        editorNode.put("id", "canvas");
        editorNode.put("resourceId", "canvas");
        ObjectNode stencilSetNode = objectMapper.createObjectNode();
        stencilSetNode.put("namespace",
            "http://b3mn.org/stencilset/bpmn2.0#");
        editorNode.put("stencilset", stencilSetNode);
        try {
            repositoryService.addModelEditorSource(id, editorNode.toString().getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //        return new ModelAndView("redirect:/modeler.html?modelId=" + id);
    }

    @RequestMapping(value = "/model/{modelId}/deploy", method = RequestMethod.POST)
    @ResponseBody
    public Object deploy(@PathVariable String modelId) {
        Map<String, Object> map = new HashMap<>();
        try {
            String deploymentId = definitionService.deployDefinitionModel(modelId);
            if (null != deploymentId&&!"".equals(deploymentId)) {
                map.put("result", "流程部署成功");
                LOGGER.info("流程部署成功");
            } else {
                map.put("result", "流程部署失败");
                LOGGER.error("流程部署失败");
            }
        } catch (Exception e) {
            throw new BizException(MessageHolder.getMessage("", "workflow.modal.deploy.failed"));
        }
        return map;
    }

    @RequestMapping(value = "/model/{modelId}/deployUpgrade", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public void deployUpgrade(@PathVariable String modelId) {
        definitionService.deployDefinitionModel(modelId);
        definitionService.upgradeProcInstDef(modelId);
    }




    /**
     * 实现文件上传替换
     */
    @RequestMapping(value = "/fileUploadUpdate/{modelId}", method = RequestMethod.POST)
    @ResponseBody
    public Boolean fileUploadUpdate(StandardMultipartHttpServletRequest request,
        @PathVariable String modelId) {
        InputStream inputStream = null;
        for (String fileName : request.getFileMap().keySet()) {
            MultipartFile multipartFile = request.getFileMap().get(fileName);
            try {
                inputStream = multipartFile.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            workflowProcService.importUpdateDefinitionModel(modelId, inputStream);
            return true;
        }
        return false;
    }

    /*
    * 实现流程文件导入（单个或多个）
    * */
    @RequestMapping(value = "/fileUploadAdd/{category}", method = RequestMethod.POST)
    @ResponseBody
    public Boolean fileUploadAdd(StandardMultipartHttpServletRequest request,
        @PathVariable String category) {
        InputStream inputStream = null;
        for (String fileName : request.getFileMap().keySet()) {
            MultipartFile multipartFile = request.getFileMap().get(fileName);
            try {
                inputStream = multipartFile.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Model model = workflowProcService.importAddModel(inputStream);
            model.setCategory(category);
            repositoryService.saveModel(model);
            return true;
        }
        return false;
    }

    //单个下载bpmn文件
    @RequestMapping(value = "/export/{modelId}", method = RequestMethod.GET)
    @ResponseBody
    public void export(@PathVariable("modelId") String modelId,
        HttpServletResponse response, HttpServletRequest request) {
        try {
            Model modelData = repositoryService.getModel(modelId); //获取模型对象
            BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
            JsonNode editorNode = new ObjectMapper() //从模型对象中读取JSON文件
                .readTree(repositoryService.getModelEditorSource(modelData.getId()));

            //把JSON对象转换为BpmnModel
            BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);
            BpmnXMLConverter xmlConverter = new BpmnXMLConverter();

            //把BpmnModel对象转换为XML格式
            byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);

            //创建输入流输出文件内容到浏览器
            ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);
            IOUtils.copy(in, response.getOutputStream());

            String fileName = HttpKit.iso8859(
                bpmnModel.getMainProcess().getName() + "/" + bpmnModel.getMainProcess().getId()
                    + ".bpmn20.xml", request);

            response.setHeader("Content-Disposition",
                "attachment; filename=" + fileName);
            response.flushBuffer();
        } catch (Exception e) {
            logger.error("导出model的xml文件失败:modelId={}", modelId, e);
        }

    }



    //批量下载bpmn文件
    @RequestMapping(value = "/exportBatch", method = RequestMethod.GET)
    public void exportBatch(@RequestParam(name = "modelIds") String[] modelIds,
        HttpServletResponse response) {

        List<String> ids = Arrays.asList(modelIds);
        //临时存储文件
        String bpmnTemp = saveBpmnTemp(ids);
        if (StringUtils.isBlank(bpmnTemp)) {
            return;
        }
        //压缩文件
        File zipPath = new File(bpmnTemp);
        String zipFileName = new File(bpmnTemp).getName() + ".zip";
        try {
            FileOperationKit.fileToZip(bpmnTemp, zipPath.getPath(), zipFileName);
        } catch (Exception e) {
            logger.error("压缩文件失败：" + bpmnTemp + zipPath.getPath(), e);
        }

        //下载文件
        File zipFile = new File(zipPath.getAbsolutePath() + "/" + zipFileName);
        try {
            downloadZipFiles(zipFile, response, zipFileName);
            FileOperationKit.delete(new File(bpmnTemp));
            FileOperationKit.delete(zipFile);
        } catch (IOException e) {
            logger.error("下载压缩包文件失败：" + zipFile.getPath(), e);
        }
    }


    //删除model
    @RequestMapping(value = "/deleteModel/{modelId}", method = RequestMethod.POST)
    @ResponseBody
    public Object deleteModel(@PathVariable("modelId") String modelId) {
        Map<String, Object> map = new HashMap<>();
        definitionService.deleteDefinitionModel(modelId);
        Model model = repositoryService.getModel(modelId);
        if (null != model) {
            map.put("result", "模型删除失败");
            logger.info("模型删除失败");
        } else {
            map.put("result", "模型删除成功");
            logger.info("模型删除成功");
        }
        return map;
    }


    private String saveBpmnTemp(List<String> modelIds) {
        //设置临时保存名称
        String tempPath =
            "workflow_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(DateKit.now());

        StringBuffer categories = new StringBuffer("");
        String lineSeparator = System.getProperty("line.separator");
        for (int i = 0; i < modelIds.size(); i++) {
            String modelId = modelIds.get(i);
            Model modelData = repositoryService.getModel(modelId); //获取模型对象
            String category = modelData.getCategory();
            String key = modelData.getKey();
            if (null == key || "".equals(key)) {
                throw new WorkflowException("尚未设置流程定义KEY,不能进行批量导出");
            }

            String name = modelData.getName();
            categories.append(category + ":");
            categories.append(key + ":");
            categories.append(name);
            categories.append(lineSeparator);

            byte[] bpmnBytes = definitionService.getDefinitionModelAsBytes(modelId);

            File tmpFile = new File(tempPath);
            if (!tmpFile.isDirectory()) {
                tmpFile.mkdirs();
            }
            String fileName = key.replace("/", "-");
            String modelPath = tempPath + "/" + fileName + ".bpmn20.xml";
            File modelFile = new File(modelPath);
            if (!modelFile.isFile()) {
                try {
                    modelFile.createNewFile();
                } catch (IOException e) {
                    logger.error("创建文件失败:" + modelPath, e);
                }
            }

            InputStream inputStream = new ByteArrayInputStream(bpmnBytes);
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(modelFile);
                int len = -1;
                byte[] buffer = new byte[10240];
                while ((len = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, len);
                }
            } catch (Exception e) {
                logger.error("创建文件输出流失败:" + modelPath, e);
            } finally {
                try {
                    outputStream.close();
                    inputStream.close();
                } catch (IOException e) {
                    logger.error("流关闭失败", e);
                }

            }
        }

        String packageName = tempPath + "/" + "list.txt";
        File file = new File(packageName);
        if (file.exists()) {
            SecurityManager securityManager = System.getSecurityManager();
            securityManager.checkDelete(packageName);
            file.delete();
        }
        try {
            file.createNewFile();
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file));
            out.write(categories.toString());
            out.flush();
            out.close();
        } catch (Exception e) {
            logger.error("创建文件夹失败:" + file.getPath(), e);
        }
        return tempPath;
    }

    private void downloadZipFiles(File file, HttpServletResponse response, String downloadFileName)
        throws IOException {
        BufferedInputStream bInputStream = new BufferedInputStream(new FileInputStream
            (file.getPath()));
        response.addHeader("Content-Disposition", "attachment; filename=" + downloadFileName);
        IOUtils.copy(bInputStream, response.getOutputStream());
        response.flushBuffer();
        file.getAbsoluteFile().delete();
    }



}

