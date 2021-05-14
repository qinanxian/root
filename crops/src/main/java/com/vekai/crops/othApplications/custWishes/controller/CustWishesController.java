package com.vekai.crops.othApplications.custWishes.controller;

import cn.fisok.sqloy.core.BeanCruder;
import com.fasterxml.jackson.databind.JsonNode;
import com.vekai.crops.codetodo.image.entity.MsbImageRecord;
import com.vekai.crops.othApplications.custWishes.service.CustWishesService;
import com.vekai.crops.util.DateUtil;
import com.vekai.crops.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/customerWishes")
public class CustWishesController {

    protected static Logger logger = LoggerFactory.getLogger(CustWishesController.class);

    @Autowired
    private CustWishesService custWishesService;
    @Autowired
    private BeanCruder beanCruder;

    /**
     * 根据custId获取客户意愿缩略图
     * @param custId
     * @return
     */
    @GetMapping("/getCompressedImgByCustomerId/{custId}")
    @ResponseBody
    public JsonNode getCompressedImgByCustomerId(@PathVariable("custId") String custId) throws Exception{

        JsonNode returnNode = JsonUtil.getJson();

        String loan_node="loan_node";
        String party="counterparty";
        String drawings_from="drawings_from";

        String faceFileId="",frontImageId="",reverseImageId="";
        //借款借据缩略图
        List<MsbImageRecord> loanRecordList = custWishesService.getImageRecordByRealId(custId, loan_node);
        //交易对手信息缩略图
        List<MsbImageRecord> counterRecordList = custWishesService.getImageRecordByRealId(custId,party);
        //提款申请书缩略图
        List<MsbImageRecord> drawingsRecordList = custWishesService.getImageRecordByRealId(custId,drawings_from);
        //人脸识别照片、身份证正反面照片
        String sql="SELECT FACE_FILE_ID FROM CUSTOMER_INFO WHERE REAL_AUTH='1' AND CERT_ID =(SELECT CERT_ID  FROM ELECTRONIC_INTEND WHERE ID =:custId)";
        faceFileId = beanCruder.selectOne(String.class, sql,"custId",custId);
        String sql2="SELECT FRONT_IMAGE_ID FROM CUSTOMER_INFO WHERE REAL_AUTH='1' AND CERT_ID =(SELECT CERT_ID  FROM ELECTRONIC_INTEND WHERE ID =:custId)";
        frontImageId = beanCruder.selectOne(String.class, sql2,"custId",custId);
        String sql3="SELECT REVERSE_IMAGE_ID FROM CUSTOMER_INFO WHERE REAL_AUTH='1' AND CERT_ID =(SELECT CERT_ID  FROM ELECTRONIC_INTEND WHERE ID =:custId)";
        reverseImageId = beanCruder.selectOne(String.class, sql3,"custId",custId);
        //人脸缩略图
        List<MsbImageRecord> faceRecordList = custWishesService.getImageRecordByBigFileId(custId, faceFileId);
        //身份证正面照片
        List<MsbImageRecord> frontRecordList = custWishesService.getImageRecordByBigFileId(custId, frontImageId);
        //身份证反面面照片
        List<MsbImageRecord> reverseRecordList = custWishesService.getImageRecordByBigFileId(custId, reverseImageId);


        JsonUtil.setValue(returnNode,"status","1");
        JsonUtil.setValue(returnNode,"message","请求成功");
        JsonUtil.setJsonValue(returnNode,"loan",JsonUtil.getJsonByJava(loanRecordList));
        JsonUtil.setJsonValue(returnNode,"counter",JsonUtil.getJsonByJava(counterRecordList));
        JsonUtil.setJsonValue(returnNode,"drawings",JsonUtil.getJsonByJava(drawingsRecordList));
        JsonUtil.setJsonValue(returnNode,"faceFileId",JsonUtil.getJsonByJava(faceRecordList));
        JsonUtil.setJsonValue(returnNode,"frontImageId",JsonUtil.getJsonByJava(frontRecordList));
        JsonUtil.setJsonValue(returnNode,"reverseImageId",JsonUtil.getJsonByJava(reverseRecordList));
        JsonUtil.setValue(returnNode,"responseTime",DateUtil.getNowTime());
        return returnNode;
    }

}
