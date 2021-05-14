package com.vekai.crops.othApplications.electronicContract.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.vekai.crops.othApplications.electronicContract.entity.ElectronicContract;
import com.vekai.crops.othApplications.electronicContract.service.ElectronicContractService;
import com.vekai.crops.util.DateUtil;
import com.vekai.crops.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/electronicContract")
public class ElectronicContractController {

    protected static Logger logger = LoggerFactory.getLogger(ElectronicContractController.class);

    @Autowired
    private ElectronicContractService electronicContractService;

    /**
     * 根据custId获取客户意愿缩略图
     * @param custId
     * @return
     */
    @GetMapping("/getPdfFileByCustomerId/{custId}")
    @ResponseBody
    public JsonNode getPdfFileByCustomerId(@PathVariable("custId") String custId) throws Exception{

        JsonNode returnNode = JsonUtil.getJson();
        ElectronicContract  electronicContract= electronicContractService.getPdfFileById(custId);

        JsonUtil.setValue(returnNode,"status","1");
        JsonUtil.setValue(returnNode,"message","请求成功");
        JsonUtil.setJsonValue(returnNode,"body",JsonUtil.getJsonByJava(electronicContract));
        JsonUtil.setValue(returnNode,"responseTime",DateUtil.getNowTime());
        return returnNode;
    }

}
