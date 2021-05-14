package com.vekai.crops.codetodo.businesstype.controller;

import cn.fisok.sqloy.core.BeanCruder;
import cn.fisok.web.kit.HttpKit;
import com.fasterxml.jackson.databind.JsonNode;
import com.vekai.crops.codetodo.businesstype.service.BusinessTypeService;
import com.vekai.crops.codetodo.image.entity.MsbImageRecord;
import com.vekai.crops.codetodo.image.service.ImageRecordService;
import com.vekai.crops.util.DateUtil;
import com.vekai.crops.util.JsonUtil;
import cn.fisok.raw.kit.IOKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/businessType")
public class BusinessTypeController {

    protected static Logger logger = LoggerFactory.getLogger(BusinessTypeController.class);

    @Autowired
    private BusinessTypeService businessTypeService;

    @Autowired
    private ImageRecordService imageRecordService;

    @Autowired
    private BeanCruder beanCruder;

    @GetMapping("/deleteDelFlagById/{businessTypeId}")
    public JsonNode deleteDelFlagById(@PathVariable("businessTypeId") String businessTypeId) throws Exception{

        JsonNode nReturn = JsonUtil.getJson();

        Integer result = businessTypeService.deleteDelFlagById(businessTypeId);

        if (result>0){
            JsonUtil.setJsonValue(nReturn,"status","1");
        } else {
            JsonUtil.setJsonValue(nReturn,"status","0");
        }

        return nReturn;
    }


    /**
     * 上传业务流程图
     * @param businessTypeId
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/uploadBusinessFlowImg/{businessTypeId}")
    @ResponseBody
    public JsonNode uploadBusinessFlowImg(@PathVariable("businessTypeId") String businessTypeId,
                                          StandardMultipartHttpServletRequest request) throws Exception{
        JsonNode returnJson = JsonUtil.getJson();

        //文件信息
        if (request.getFileMap().isEmpty()){
            logger.error("找不到上传的文件,fileMap为null");
            JsonUtil.setValue(returnJson,"status","0");
            JsonUtil.setValue(returnJson,"message","找不到上传的文件,fileMap为null");
            JsonUtil.setValue(returnJson,"time", DateUtil.getNowTime());
            return returnJson;
        }
        MultipartFile multipartFile = request.getFileMap().values().iterator().next();
        if (multipartFile.isEmpty()){
            logger.error("找不到上传的文件,multipartFile为null");
            JsonUtil.setValue(returnJson,"status","0");
            JsonUtil.setValue(returnJson,"message","找不到上传的文件,multipartFile为null");
            JsonUtil.setValue(returnJson,"time", DateUtil.getNowTime());
            return returnJson;
        }
        String realType = "0003";
        List<MsbImageRecord> msbImageRecords = beanCruder.selectList(MsbImageRecord.class,
                "select * from MSB_IMAGE_RECORD where REAL_ID = :realId and REAL_TYPE = :realType",
                "realId", businessTypeId, "realType", realType);
        if(msbImageRecords.size()>0){
            JsonUtil.setValue(returnJson,"status","0");
            JsonUtil.setValue(returnJson,"message","不要重复上传图片");
            JsonUtil.setValue(returnJson,"time", DateUtil.getNowTime());
            return returnJson;
        }



        MsbImageRecord imageRecord = businessTypeService.uploadBusinessFlowImg(businessTypeId,multipartFile,realType);
        if (imageRecord != null){
            JsonUtil.setValue(returnJson,"status","1");
            JsonUtil.setValue(returnJson,"message","文件上传成功");
            JsonUtil.setValue(returnJson,"body",JsonUtil.getJsonByJava(imageRecord));
        } else {
            JsonUtil.setValue(returnJson,"status","0");
            JsonUtil.setValue(returnJson,"message","文件上传失败");
        }

        JsonUtil.setValue(returnJson,"time", DateUtil.getNowTime());

        return returnJson;
    }

    /**
     * 根据businessId获取缩略图
     * @param businessId
     * @return
     */
    @GetMapping("/getCompressedImgByBusinessId/{businessId}")
    @ResponseBody
    public JsonNode getCompressedImgByBusinessId(@PathVariable("businessId") String businessId) throws Exception{

        JsonNode returnNode = JsonUtil.getJson();

        String realType = "0003";
        List<MsbImageRecord> imageRecordList = imageRecordService.getImageRecordByRealId(businessId, realType);

        JsonUtil.setValue(returnNode,"status","1");
        JsonUtil.setValue(returnNode,"message","请求成功");
        JsonUtil.setJsonValue(returnNode,"body",JsonUtil.getJsonByJava(imageRecordList));
        JsonUtil.setValue(returnNode,"responseTime",DateUtil.getNowTime());
        return returnNode;
    }

    /**
     * 根据MSB_IMAGE_RECORD表中的ID获取原图
     * @param id 图片id
     */
    @GetMapping("/getBigImgById/{id}")
    public void getBigImgById(@PathVariable("id") String id, HttpServletResponse response) throws IOException {
        MsbImageRecord imageRecord = imageRecordService.getImageRecordById(id);
        logger.info("图片信息"+imageRecord.toString());

        InputStream gatewayInputStream = null;
        try {
            String fileId = imageRecord.getBigImageId();
            logger.info("fileId: "+fileId);

            gatewayInputStream = imageRecordService.getImgByFileId(fileId);
            HttpKit.renderStream(response,gatewayInputStream,"octets/stream",null);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("出现异常"+e.getMessage());
        } finally {
            IOKit.close(gatewayInputStream);
        }
    }

}
