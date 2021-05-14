package com.vekai.crops.loanBook.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.vekai.crops.business.service.BusinessService;
import com.vekai.crops.util.JsonUtil;
import cn.fisok.raw.kit.JSONKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/loanBook")
public class LoanBookController {

    @Autowired
    private BusinessService businessService;

    @PostMapping("/whiteEntrying")
    @ResponseBody
    public JsonNode whiteEntrying(@RequestBody JsonNode nBody) throws Exception{

        JsonNode nReturn = JsonUtil.getJson();

        ArrayNode arrayNodeList = JsonUtil.getArrayNodeByPath(nBody, "list");

        List<String> applyIds = JSONKit.jsonToBeanList(arrayNodeList.toString(), String.class);
        if (!applyIds.isEmpty()){

            Integer applyIdSize = applyIds.size();
            Integer count = 0;

            for (String applyId : applyIds){
                count = count + businessService.whiteEntry(applyId);
            }

            JsonUtil.setJsonStringValue(nReturn,"code","1");
            JsonUtil.setJsonStringValue(nReturn,"msg","成功录入"+count+"条数据");

        }else{
            JsonUtil.setJsonStringValue(nReturn,"code","0");
            JsonUtil.setJsonStringValue(nReturn,"msg","没有数据等待录入");
        }

        return nReturn;
    }
}
