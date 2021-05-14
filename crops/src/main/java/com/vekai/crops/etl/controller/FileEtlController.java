package com.vekai.crops.etl.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.vekai.crops.common.service.FileGatwayServiceImpl;
import com.vekai.crops.customer.entity.CustomerInfoPO;
import com.vekai.crops.util.CommonUtil;
import com.vekai.crops.util.JsonUtil;
import com.vekai.crops.util.mongo3.DBEntry;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.kit.ValidateKit;
import cn.fisok.sqloy.core.BeanCruder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/file/etl")
public class FileEtlController {
    protected static Logger logger = LoggerFactory.getLogger(FileEtlController.class);
    @Autowired
    private BeanCruder beanCruder;
    @Autowired
    private FileGatwayServiceImpl fileGatwayService;

    @Value("${kettle.base.path}")
    public String basePath;

    /**
     * 迁移身份证正面识别成功的照片
     */
    @GetMapping("/moveFrontImage")
    public String moveFrontImage() throws Exception{
        String rstMessage = "";
        JsonNode[] customer_infos = DBEntry.findJsonArrayByFilter("customer_info", null, null);
        String customerId = null;
        List<CustomerInfoPO> list = new ArrayList<CustomerInfoPO>();
        for(JsonNode customer_info : customer_infos){
            try{
                customerId = JsonUtil.getJsonStringValue(customer_info,"_id");
                CustomerInfoPO customerInfoPO = beanCruder.selectOneById(CustomerInfoPO.class, MapKit.mapOf("id", customerId));
                //获取所有征信授权pdf的id
                String pdfIds = JsonUtil.getJsonStringName(JsonUtil.queryJson(customer_info, "customer_info.i"));
                if(!CommonUtil.ifIsEmpty(pdfIds)){
                    String[] ids = pdfIds.split(",");
                    //有效pdf文件ID
                    String pdfId = JsonUtil.getJsonStringValue(customer_info,"customer_info.i."+ids[0]+".j");
                    GridFSFile oneGridFSByID = DBEntry.findOneGridFSByID(pdfId, null);
                    if(!CommonUtil.ifIsEmpty(oneGridFSByID)){
                        String filename = oneGridFSByID.getFilename();
                        FileOutputStream fos = new FileOutputStream(basePath+filename);
                        DBEntry.downloadGridFS2StreamByID(pdfId,fos);
                        File file = new File(basePath+filename);
                        InputStream inputStream = new FileInputStream(file);
                        String fileId = CommonUtil.getUUID();
                        ValidateKit.notEmpty(fileId,"获取文件Id为空!");
                        String uploadRes = fileGatwayService.uploadFile(inputStream, fileId);
                        String status = JsonUtil.getJsonStringValue(JsonUtil.getJson(uploadRes),"status");
                        boolean rst = "success".equals(status);
                        if(rst){
                            if(!CommonUtil.ifIsEmpty(customerInfoPO)){
                                customerInfoPO.setFrontImageId(fileId);
                                customerInfoPO.setFrontImageName(filename);
                                list.add(customerInfoPO);
                            }
                        }
                        fos.flush();
                        fos.close();
                    }

                }else {
                    rstMessage = customerId+"|";
                }
            }catch (Exception e){
                logger.info("move front error:"+customerId);
                continue;
            }
        }
        logger.info("未更新的customerId:"+rstMessage);
        int save = beanCruder.save(list);
        return "成功更新条数:"+save;
    }

    /**
     * 迁移身份证反面识别成功的照片
     */
    @GetMapping("/moveReverseImage")
    public String moveReverseImage() throws Exception{
        String rstMessage = "";
        JsonNode[] customer_infos = DBEntry.findJsonArrayByFilter("customer_info", null, null);
        String customerId = null;
        String realAuth = "0";
        List<CustomerInfoPO> list = new ArrayList<CustomerInfoPO>();
        for(JsonNode customer_info : customer_infos){
            try{
                customerId = JsonUtil.getJsonStringValue(customer_info,"_id");
                realAuth = JsonUtil.getJsonStringValue(customer_info,"customer_info.c");
                CustomerInfoPO customerInfoPO = beanCruder.selectOneById(CustomerInfoPO.class, MapKit.mapOf("id", customerId));
                //获取所有征信授权pdf的id
                String pdfIds = JsonUtil.getJsonStringName(JsonUtil.queryJson(customer_info, "customer_info.i"));
                if(!CommonUtil.ifIsEmpty(pdfIds)){
                    String[] ids = pdfIds.split(",");
                    //有效pdf文件ID
                    String pdfId = JsonUtil.getJsonStringValue(customer_info,"customer_info.i."+ids[ids.length-1]+".j");
                    GridFSFile oneGridFSByID = DBEntry.findOneGridFSByID(pdfId, null);
                    if(!CommonUtil.ifIsEmpty(oneGridFSByID)){
                        String filename = oneGridFSByID.getFilename();
                        FileOutputStream fos = new FileOutputStream(basePath+filename);
                        DBEntry.downloadGridFS2StreamByID(pdfId,fos);
                        File file = new File(basePath+filename);
                        InputStream inputStream = new FileInputStream(file);
                        String fileId = CommonUtil.getUUID();
                        ValidateKit.notEmpty(fileId,"获取文件Id为空!");
                        String uploadRes = fileGatwayService.uploadFile(inputStream, fileId);
                        String status = JsonUtil.getJsonStringValue(JsonUtil.getJson(uploadRes),"status");
                        boolean rst = "success".equals(status);
                        if(rst){
                            if(!CommonUtil.ifIsEmpty(customerInfoPO)){
                                customerInfoPO.setReverseImageId(fileId);
                                customerInfoPO.setReverseImageName(filename);
                                if(!"1".equals(realAuth)){//未实名的，设置实名步骤
                                    customerInfoPO.setAuthStep("0003");
                                }
                                list.add(customerInfoPO);
                            }
                        }
                        fos.flush();
                        fos.close();
                    }

                }else {
                    rstMessage = customerId+"|";
                }
            }catch (Exception e){
                logger.info("move reverse error:"+customerId);
                continue;
            }
        }
        logger.info("未更新的customerId:"+rstMessage);
        int save = beanCruder.save(list);
        return "成功更新条数:"+save;
    }

    /**
     * 迁移征信授权成功的pdf文件
     */
    @GetMapping("/moveCredit")
    public String moveCredit() throws Exception{
        String rstMessage = "";
        JsonNode[] customer_infos = DBEntry.findJsonArrayByFilter("customer_info", null, null);
        String customerId = null;
        List<CustomerInfoPO> list = new ArrayList<CustomerInfoPO>();
        for(JsonNode customer_info : customer_infos){
            try{
                customerId = JsonUtil.getJsonStringValue(customer_info,"_id");
                CustomerInfoPO customerInfoPO = beanCruder.selectOneById(CustomerInfoPO.class, MapKit.mapOf("id", customerId));
                //获取所有征信授权pdf的id
                String pdfIds = JsonUtil.getJsonStringName(JsonUtil.queryJson(customer_info, "customer_info.o1"));
                if(!CommonUtil.ifIsEmpty(pdfIds)){
                    String[] ids = pdfIds.split(",");
                    //有效pdf文件ID
                    String pdfId = JsonUtil.getJsonStringValue(customer_info,"customer_info.o1."+ids[ids.length-1]+".j");
                    GridFSFile oneGridFSByID = DBEntry.findOneGridFSByID(pdfId, null);
                    if(!CommonUtil.ifIsEmpty(oneGridFSByID)){
                        String filename = oneGridFSByID.getFilename();
                        FileOutputStream fos = new FileOutputStream(basePath+filename);
                        DBEntry.downloadGridFS2StreamByID(pdfId,fos);
                        File file = new File(basePath+filename);
                        InputStream inputStream = new FileInputStream(file);
                        String fileId = CommonUtil.getUUID();
                        ValidateKit.notEmpty(fileId,"获取文件Id为空!");
                        String uploadRes = fileGatwayService.uploadFile(inputStream, fileId);
                        String status = JsonUtil.getJsonStringValue(JsonUtil.getJson(uploadRes),"status");
                        boolean rst = "success".equals(status);
                        if(rst){
                            if(!CommonUtil.ifIsEmpty(customerInfoPO)){
                                customerInfoPO.setCreditFileId(fileId);
                                customerInfoPO.setRealCredit("1");
                                list.add(customerInfoPO);
                            }
                        }
                        fos.flush();
                        fos.close();
                    }
                }else {
                    rstMessage = customerId+"|";
                }
            }catch (Exception e){
                logger.info("move credit error:"+customerId);
                continue;
            }
        }
        logger.info("未更新的customerId:"+rstMessage);
        int save = beanCruder.save(list);
        return "成功更新条数:"+save;
    }

    /**
     * 迁移人脸识别成功的照片
     */
    @GetMapping("/moveFaceImage")
    public String moveFaceImage() throws Exception{
        String rstMessage = "";
        JsonNode[] customer_infos = DBEntry.findJsonArrayByFilter("customer_info", null, null);
        String customerId = null;
        List<CustomerInfoPO> list = new ArrayList<CustomerInfoPO>();
        for(JsonNode customer_info : customer_infos){
            try{
                customerId = JsonUtil.getJsonStringValue(customer_info,"_id");
                CustomerInfoPO customerInfoPO = beanCruder.selectOneById(CustomerInfoPO.class, MapKit.mapOf("id", customerId));
                //获取所有征信授权pdf的id
                String pdfIds = JsonUtil.getJsonStringName(JsonUtil.queryJson(customer_info, "esign_info.i"));
                if(!CommonUtil.ifIsEmpty(pdfIds)){
                    String[] ids = pdfIds.split(",");
                    //有效pdf文件ID
                    String pdfId = JsonUtil.getJsonStringValue(customer_info,"esign_info.i."+ids[ids.length-1]+".j");
                    GridFSFile oneGridFSByID = DBEntry.findOneGridFSByID(pdfId, null);
                    if(!CommonUtil.ifIsEmpty(oneGridFSByID)){
                        String filename = oneGridFSByID.getFilename();
                        FileOutputStream fos = new FileOutputStream(basePath+filename);
                        DBEntry.downloadGridFS2StreamByID(pdfId,fos);
                        File file = new File(basePath+filename);
                        InputStream inputStream = new FileInputStream(file);
                        String fileId = CommonUtil.getUUID();
                        ValidateKit.notEmpty(fileId,"获取文件Id为空!");
                        String uploadRes = fileGatwayService.uploadFile(inputStream, fileId);
                        String status = JsonUtil.getJsonStringValue(JsonUtil.getJson(uploadRes),"status");
                        boolean rst = "success".equals(status);
                        if(rst){
                            if(!CommonUtil.ifIsEmpty(customerInfoPO)){
                                customerInfoPO.setFaceFileId(fileId);
                                list.add(customerInfoPO);
                            }
                        }
                        fos.flush();
                        fos.close();
                    }
                }else {
                    rstMessage = customerId+"|";
                }
            }catch (Exception e){
                logger.info("move credit error:"+customerId);
                continue;
            }
        }
        logger.info("未更新的customerId:"+rstMessage);
        int save = beanCruder.save(list);
        return "成功更新条数:"+save;
    }
}
