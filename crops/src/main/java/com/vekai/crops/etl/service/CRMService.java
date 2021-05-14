package com.vekai.crops.etl.service;

import cn.fisok.sqloy.serialnum.finder.SerialNumberGeneratorFinder;
import cn.fisok.sqloy.serialnum.generator.SerialNumberGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.vekai.crops.util.DateUtil;
import com.vekai.crops.util.HttpUtil;
import com.vekai.crops.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CRMService {
    protected static Logger logger = LoggerFactory.getLogger(CRMService.class);
    @Value("${mrapi.gateway.url}")
    private String baseUrl;
    @Value("${mrapi.crm.path}")
    private String crmPath;
    @Autowired
    SerialNumberGeneratorFinder serialNoGeneratorFinder;
    /**
     * 生成业务流水号（自增）
     */
    private String createReqSeqNo(){
        SerialNumberGenerator<String> generator = serialNoGeneratorFinder.find("com.vekai.crops.etl.entity.MsbSeqNo.reqSeqNo");
        return generator.next("com.vekai.crops.etl.entity.MsbSeqNo.reqSeqNo");
    }

    /**
     * 生成业务跟踪号（20位=<应用系统代码2位><日期标识符，yyyymmdd><流水序号，数字最大10位>）
     * @param reqSeqNo
     */
    private String createTraceSeqNo(String reqSeqNo){
        StringBuffer buffer = new StringBuffer("MB");
        buffer.append(DateUtil.getNowTime("yyyyMMdd"));
        buffer.append(reqSeqNo);
        return buffer.toString();
    }

    /**
     * 消息提醒
     */
    public String getRemindMsg() throws Exception{
        String reqSeqNo = createReqSeqNo();
        String traceSeqNo = createTraceSeqNo(reqSeqNo);
        String nowTime = DateUtil.getNowTime("yyyy-MM-dd HH:mm:ss");

        JsonNode req = JsonUtil.getJson("{}");
        JsonNode head = JsonUtil.getJson("{}");
        JsonNode body = JsonUtil.getJson("{}");

        JsonNode header = JsonUtil.getJson("{}");
        JsonUtil.setJsonStringValue(header, "Content-Type", "application/json");
        JsonUtil.setJsonStringValue(header, "CRMAction", "pullRemindMsg");
        JsonUtil.setJsonStringValue(head, "traceSeqNo", traceSeqNo);
        JsonUtil.setJsonStringValue(head, "reqSeqNo", reqSeqNo);
        JsonUtil.setJsonStringValue(head, "chnNo", "21");
        JsonUtil.setJsonStringValue(head, "reqTime", nowTime);
        JsonUtil.setJsonStringValue(head, "orgNo", "VB9999");
        JsonUtil.setJsonStringValue(head, "userNo", "ozusVt_BdnL_UUo1oOL8eNSX5B7g");
        JsonUtil.setJsonValue(req,"body",body);
        JsonUtil.setJsonValue(req,"head",head);
        String rst = HttpUtil.doPost(baseUrl + crmPath, req.toString(), header);
        return rst;
    }
}
