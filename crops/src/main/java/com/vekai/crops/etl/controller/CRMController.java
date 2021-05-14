package com.vekai.crops.etl.controller;

import cn.fisok.raw.lang.RawException;
import cn.fisok.sqloy.core.BeanCruder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.vekai.crops.etl.entity.HmRemindMsg;
import com.vekai.crops.etl.service.CRMService;
import com.vekai.crops.util.CommonUtil;
import com.vekai.crops.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/crm")
public class CRMController {
    @Autowired
    private BeanCruder beanDao;
    @Autowired
    private CRMService crmService;

    @GetMapping("/pullRemindMsg")
    public String pullRemindMsg(){
        int res = 0;
        try{
            List<HmRemindMsg> hmRemindMsgs = new ArrayList<HmRemindMsg>();
            String crmRes = crmService.getRemindMsg();
            JsonNode crmResult = JsonUtil.getJson(crmRes);
            String ret = JsonUtil.getJsonStringValue(JsonUtil.queryJson(crmResult, "head"), "ret");
            if("1".equals(ret)){
                ArrayNode body = (ArrayNode) JsonUtil.queryJson(crmResult,"body");
                if(CommonUtil.ifIsNotEmpty(body) && body.size()>0) {
                    for(JsonNode hmMsgJson: body){
                        HmRemindMsg hmRemindMsg = JsonUtil.getJavaFromJson(hmMsgJson, HmRemindMsg.class);
                        hmRemindMsgs.add(hmRemindMsg);
                    }
                    res = beanDao.save(hmRemindMsgs);
                }
            }
        }catch (Exception e){
            throw new RawException("定时任务失败",e);
        }
        return Integer.toString(res);
    }
}
