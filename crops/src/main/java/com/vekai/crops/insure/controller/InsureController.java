package com.vekai.crops.insure.controller;

import cn.fisok.raw.kit.MapKit;
import cn.fisok.sqloy.core.BeanCruder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.vekai.crops.codetodo.image.entity.MsbImageRecord;
import com.vekai.crops.common.entity.AuthOrg;
import com.vekai.crops.insure.entity.BxNetCompanyRelInfo;
import com.vekai.crops.insure.service.InsureService;
import com.vekai.crops.util.DateUtil;
import com.vekai.crops.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/insure/network")
public class InsureController {

    protected static Logger logger = LoggerFactory.getLogger(InsureController.class);

    @Autowired
    private InsureService insureService;
    @Autowired
    private BeanCruder beanCruder;

    /**
     * 获取当前保险公司已配置的网点
     * @param id
     */
    @GetMapping("/getExistedNetwork/{id}")
    @ResponseBody
    public List<AuthOrg> getExistedNetwork(@PathVariable("id") String id){

        List<AuthOrg> existedNetwork = insureService.getExistedNetwork(id);
        return existedNetwork;
    }

    /**
     *新增网点
     * @return
     * @throws Exception
     */
    @PostMapping("/addNetWork")
    @ResponseBody
    public JsonNode addNetWork(@RequestBody JsonNode mainNode) throws Exception {
        logger.info("前端传入参数orgIdArray：" + mainNode.toString());

        ArrayNode orgIdJsonArray = JsonUtil.getArrayNodeByPath(mainNode, "orgIdArray");
        logger.info("共" + orgIdJsonArray.size() + "条数据");
        //保险公司Id
        String comId = JsonUtil.getJsonStringValue(mainNode, "comId");

        List<String> orgIdList = new ArrayList<>();
        for (int i = 0; i < orgIdJsonArray.size(); i++) {
            orgIdList.add(orgIdJsonArray.get(i).textValue());
        }
        JsonNode returnNode = JsonUtil.getJson();
        if (orgIdList.size() > 0){
            int count = 0;
            String netName="";
            List<BxNetCompanyRelInfo> bxNetCompanyRelInfos = new ArrayList<>();
            for (String orgId : orgIdList){
                   if (!insureService.isExistNetworkByOrgId(orgId,comId)){
                       netName = insureService.getNetName(orgId);
                       BxNetCompanyRelInfo bxNetCompanyRelInfo = new BxNetCompanyRelInfo();
                       bxNetCompanyRelInfo.setComId(comId);
                       bxNetCompanyRelInfo.setNetId(orgId);
                       bxNetCompanyRelInfo.setNetName(netName);
                       bxNetCompanyRelInfo.setFlag("0");
                       bxNetCompanyRelInfos.add(bxNetCompanyRelInfo);
                   }
            }
            count=bxNetCompanyRelInfos.size();
            beanCruder.insert(bxNetCompanyRelInfos);
            if (count >0){
                JsonUtil.setValue(returnNode,"status","1");
                JsonUtil.setValue(returnNode,"message","新增"+count+"条数据");
            }else {
                JsonUtil.setValue(returnNode,"status","0");
                JsonUtil.setValue(returnNode,"message","新增失败，请重试!");
            }
        }else {
            JsonUtil.setValue(returnNode,"status","1");
            JsonUtil.setValue(returnNode,"message","新增0条数据");
            logger.warn("获取参数数组长度为0");
        }
        return returnNode;
    }

    /**
     * 删除保险公司及其相关数据
     * @param comId
     * @return
     */
    @GetMapping("/deleteComById/{comId}")
    @ResponseBody
    @Transactional
    public int deleteComById(@PathVariable("comId") String comId){
        int res = beanCruder.execute("UPDATE BX_COMPANY_INFO SET FLAG='1' where COM_ID =:comId", MapKit.mapOf("comId", comId));
        int rst = beanCruder.execute("UPDATE BX_PRODUCT_INFO SET FLAG='1' where COM_ID =:comId", MapKit.mapOf("comId", comId));
        int rat = beanCruder.execute("delete from BX_NET_COMPANY_REL where COM_ID =:comId", MapKit.mapOf("comId", comId));
        return Math.min(res,(Math.min(rst,rat)));
    }

    /**
     * 删除保险产品数据
     * @param proId
     * @return
     */
    @GetMapping("/deleteProById/{proId}")
    @ResponseBody
    @Transactional
    public int deleteProById(@PathVariable("proId") String proId){
        int res = beanCruder.execute("UPDATE BX_PRODUCT_INFO SET FLAG='1' WHERE PRO_ID =:proId", MapKit.mapOf("proId", proId));

        return res;
    }

    /**
     * 获取网点一级支行名称、机构号
     * @param orgId
     * @return
     */
    @GetMapping("/getBraInfo/{orgId}")
    @ResponseBody
    @Transactional
    public JsonNode getBraInfo(@PathVariable("orgId") String orgId) throws Exception{
        JsonNode returnNode = JsonUtil.getJson();

        String sortCode="",braId="",braName="";
        String sql="SELECT SORT_CODE FROM AUTH_ORG WHERE ID = :orgId";
        sortCode = beanCruder.selectOne(String.class, sql,"orgId",orgId);
        String[] code = sortCode.split("/");
        int length = code.length ;
        if(length >=3){
            braId = code[2];
        }else if (length == 2){
            braId = code[1];
        }else{
            braId = code[0];
        }

        String sql2="SELECT NAME FROM AUTH_ORG WHERE ID = :braId";
        braName = beanCruder.selectOne(String.class, sql2,"braId",braId);
        if (braId.isEmpty()){
            JsonUtil.setValue(returnNode,"status","0");
            JsonUtil.setValue(returnNode,"message","查询一级支行机构失败!");
        }else{
            JsonUtil.setValue(returnNode,"status","1");
            JsonUtil.setValue(returnNode,"braId",braId);
            JsonUtil.setValue(returnNode,"braName",braName);
        }
        return returnNode;
    }


    /**
     * 根据Id获取客户上传的缴费凭证缩略图
     * @param id
     * @return
     */
    @GetMapping("/getCompressedImgById/{id}")
    @ResponseBody
    public JsonNode getCompressedImgById(@PathVariable("id") String id) throws Exception{

        JsonNode returnNode = JsonUtil.getJson();
        String sql="select * from MSB_IMAGE_RECORD where REAL_ID =:id AND REAL_TYPE='0006'";
        List<MsbImageRecord> imageRecordList = beanCruder.selectList(MsbImageRecord.class, sql, "id", id);
        int count = imageRecordList.size();
        if(count>0){
            JsonUtil.setValue(returnNode,"status","1");
            JsonUtil.setValue(returnNode,"message","请求成功");
            JsonUtil.setValue(returnNode,"count",count);
            JsonUtil.setJsonValue(returnNode,"voucher",JsonUtil.getJsonByJava(imageRecordList));
            JsonUtil.setValue(returnNode,"responseTime", DateUtil.getNowTime());
        }else{
            JsonUtil.setValue(returnNode,"status","0");
            JsonUtil.setValue(returnNode,"message","请求失败");
        }
        return returnNode;
    }

    /**
     * 根据Id用户按钮操作权限信息
     * @param id
     * @return
     */
    @GetMapping("/getUserRoleInfoById/{id}")
    @ResponseBody
    public JsonNode getUserRoleInfoById(@PathVariable("id") String id) throws Exception{
        JsonNode returnNode = JsonUtil.getJson();
        String sql="SELECT ROLE FROM BX_MANAGER_INFO WHERE MAN_OA =:id";
        String roleNum = beanCruder.selectOne(String.class, sql,"id",id);
        JsonUtil.setValue(returnNode,"status","1");
        JsonUtil.setValue(returnNode,"message","请求成功");
        JsonUtil.setValue(returnNode,"roleNum",roleNum);
        JsonUtil.setValue(returnNode,"responseTime", DateUtil.getNowTime());
        return returnNode;
    }

    /**
     *移交后，更新保单信息
     * @return
     * @throws Exception
     */
    @PostMapping("/updateOrderInfo")
    @ResponseBody
    public int updateOrderInfo(@RequestBody JsonNode mainNode) throws Exception {
        //保单信息Id
        String orderId = JsonUtil.getJsonStringValue(mainNode, "orderId");
        //移交类型
        String tranType = JsonUtil.getJsonStringValue(mainNode, "tranType");
        //接受人oa
        String revOaNo = JsonUtil.getJsonStringValue(mainNode, "revOaNo");
        //接受人姓名
        String revName = JsonUtil.getJsonStringValue(mainNode, "revName");
        int res = 0;
        res= beanCruder.execute("UPDATE BX_ORDER_INFO SET MGR_NAME =:revName,MGR_OA_NO =:revOaNo  WHERE ORDER_ID =:orderId", MapKit.mapOf("revName", revName,"revOaNo",revOaNo,"orderId",orderId));
        if(tranType.equals("T01")){
            res= beanCruder.execute("UPDATE BX_ORDER_INFO SET INS_STATUS ='03'  WHERE ORDER_ID =:orderId", MapKit.mapOf("orderId",orderId));
        }
        return res;
    }

    /**
     * 根据Id获取保单对应公司名称、产品名称
     * @param id
     * @return
     */
    @GetMapping("/getComProInfoById/{id}")
    @ResponseBody
    public JsonNode getComProInfoById(@PathVariable("id") String id) throws Exception{
        JsonNode returnNode = JsonUtil.getJson();
        String sql="SELECT COM_NAME  FROM BX_COMPANY_INFO WHERE COM_ID=(SELECT COM_ID FROM BX_ORDER_INFO WHERE ORDER_ID =:id)";
        String comName = beanCruder.selectOne(String.class, sql,"id",id);
        String sql2=" SELECT PRO_NAME  FROM BX_PRODUCT_INFO WHERE PRO_ID=(SELECT PRO_ID FROM  BX_ORDER_INFO WHERE ORDER_ID =:id)";
        String proName = beanCruder.selectOne(String.class, sql2,"id",id);
        JsonUtil.setValue(returnNode,"status","1");
        JsonUtil.setValue(returnNode,"message","请求成功");
        JsonUtil.setValue(returnNode,"comName",comName);
        JsonUtil.setValue(returnNode,"proName",proName);
        JsonUtil.setValue(returnNode,"responseTime", DateUtil.getNowTime());
        return returnNode;
    }
}
