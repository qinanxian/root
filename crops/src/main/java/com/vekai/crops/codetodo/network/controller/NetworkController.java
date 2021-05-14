package com.vekai.crops.codetodo.network.controller;

import cn.fisok.raw.kit.IOKit;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.sqloy.core.BeanCruder;
import cn.fisok.sqloy.serialnum.finder.SerialNumberGeneratorFinder;
import cn.fisok.web.kit.HttpKit;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.vekai.crops.codetodo.image.entity.MsbImageRecord;
import com.vekai.crops.codetodo.image.service.ImageRecordService;
import com.vekai.crops.codetodo.network.entity.MsbNetworkInfo;
import com.vekai.crops.codetodo.network.service.NetworkService;
import com.vekai.crops.common.entity.AuthOrg;
import com.vekai.crops.common.entity.NetworkInfoPO;
import com.vekai.crops.common.service.AuthOrgService;
import com.vekai.crops.util.DateUtil;
import com.vekai.crops.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/network")
public class NetworkController {

    protected static Logger logger = LoggerFactory.getLogger(NetworkController.class);

    @Autowired
    private NetworkService networkService;
    @Autowired
    private AuthOrgService authOrgService;
    @Autowired
    private ImageRecordService imageRecordService;
    @Autowired
    private SerialNumberGeneratorFinder serialNumberGeneratorFinder;
    @Autowired
    private BeanCruder beanCruder;
    @Value("${kettle.base.path}")
    private String path;


    /**
     * 上传网点照片接口
     * @param request
     * @param netWorkId   : MSB_NETWORK_INFO表中的主键id
     */
    @PostMapping("/uploadImg/{netWorkId}/{realType}")
    @ResponseBody
    public JsonNode uploadNetworkImg(StandardMultipartHttpServletRequest request,
                                     @PathVariable("netWorkId") String netWorkId,@PathVariable("realType") String realType) throws Exception{

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

        //图片表的关联类型，一般网点为0001，自助银行为0002, 业务类型为0003
        boolean result = networkService.uploadNetworkImg(multipartFile,netWorkId,realType);

        if (result){
            JsonUtil.setValue(returnJson,"status","1");
            JsonUtil.setValue(returnJson,"message","文件上传成功");
        }else {
            JsonUtil.setValue(returnJson,"status","0");
            JsonUtil.setValue(returnJson,"message","文件上传失败");
        }

        JsonUtil.setValue(returnJson,"time", DateUtil.getNowTime());

        return returnJson;
    }

    /**
     * 根据NETWORK_INFO表中的id获取网点照片
     * @param networkId-----网点id
     * @return
     */
    @GetMapping("/getImageRecordByNetworkId/{networkId}/{realType}")
    @ResponseBody
    public JsonNode getImageRecordByNetworkId(@PathVariable("networkId") String networkId,@PathVariable("realType") String realType) throws Exception{

        JsonNode returnNode = JsonUtil.getJson();

        //图片表的关联类别，0001为一般网点，0002为自助银行，0003为业务类型
        List<MsbImageRecord> imageRecordList = networkService.getImageRecordByNetworkId(networkId,realType);
        logger.info("网点照片列表: "+ imageRecordList.toString());

        JsonNode imageRecordJsonList = JsonUtil.getJsonByJava(imageRecordList);
        JsonUtil.setJsonStringValue(returnNode,"status","1");
        JsonUtil.setJsonValue(returnNode,"body",imageRecordJsonList);
        JsonUtil.setJsonStringValue(returnNode,"responseTime",DateUtil.getNow());
        return returnNode;
    }

    /**
     * 根据MSB_IMAGE_RECORD表中的ID获取原图
     * @param imgId 图片id
     */
    @GetMapping("/getBigImgById/{imgId}")
    public void getBigImgById(@PathVariable("imgId") String imgId, HttpServletRequest request, HttpServletResponse response) throws IOException{
//        MsbImageRecord imageRecord = imageRecordService.getImageRecordById(id);
//        logger.info("图片信息"+imageRecord.toString());

        InputStream gatewayInputStream = null;
        try {
//            String fileId = imageRecord.getBigImageId();

            gatewayInputStream = imageRecordService.getImgByFileId(imgId);
            logger.info("网关下载文件大小:"+gatewayInputStream.available());
//            gatewayInputStream = new FileInputStream("/Users/shibin/Downloads/4UQWGfJF.jpg");
            HttpKit.renderStream(response,gatewayInputStream,"octets/stream",null);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("出现异常"+e.getMessage());
        } finally {
            IOKit.close(gatewayInputStream);
        }
    }

    /**
     *新增网点
     * @return
     * @throws Exception
     */
    @PostMapping("/addNetWork")
    @ResponseBody
    public JsonNode addNetWork(@RequestBody JsonNode mainNode) throws Exception{
        logger.info("前端传入参数orgIdArray：" + mainNode.toString());

        ArrayNode orgIdJsonArray = JsonUtil.getArrayNodeByPath(mainNode, "orgIdArray");
        logger.info("共"+orgIdJsonArray.size()+"条数据");

        List<String> orgIdList = new ArrayList<>();
        for (int i=0; i<orgIdJsonArray.size(); i++){
            orgIdList.add(orgIdJsonArray.get(i).textValue());
        }

        JsonNode returnNode = JsonUtil.getJson();
        if (orgIdList.size() > 0){

            //需要保存的网点列表
            List<NetworkInfoPO> networkInfoListToSave = new ArrayList<>();
            //需要保存的网点附属信息
            List<MsbNetworkInfo> msbNetworkInfos = new ArrayList<>();
            //已存在的网点列表
            List<String> networkInfoListExisted = new ArrayList<>();
            for (String orgId : orgIdList){
                //避免插入重复数据
                if (!networkService.isExistNetworkByOrgId(orgId)){
                    //根据orgId获取AuthOrg对象
                    AuthOrg authOrg = authOrgService.getAuthOrgByOrgId(orgId);
                    logger.info("根据orgId获取的authOrg对象："+authOrg);

                    //将AuthOrg对象中的信息设置到NETWORK_INFO表中
//                    NetworkInfoPO networkInfo = new NetworkInfoPO();
////                    SerialNoGenerator<String> generator = serialNumberGeneratorFinder.find("com.vekai.crops.common.entity.NetworkInfoPO.id");
////                    String serialNo = generator.next("com.vekai.crops.common.entity.NetworkInfoPO.id");
////                    networkInfo.setId(serialNo);
////                    networkInfo.setOrgId(orgId);
//                    networkInfo.setId(orgId);
//                    networkInfo.setName(authOrg.getName());
//                    networkInfo.setDistricts(authOrg.getDistricts());
//                    networkInfo.setAddress(authOrg.getAddress());
//                    networkInfo.setCode(authOrg.getPostCode());
//                    networkInfo.setPhone(authOrg.getPhone());
//                    networkInfo.setWorkeHour(authOrg.getWorkeHour());
//                    networkInfo.setLongitude(authOrg.getLongitude());
//                    networkInfo.setLatitude(authOrg.getLatitude());
//
//                    networkInfoListToSave.add(networkInfo);

                    MsbNetworkInfo msbNetworkInfo = new MsbNetworkInfo();
                    msbNetworkInfo.setNetworkNo(orgId);
                    msbNetworkInfos.add(msbNetworkInfo);
                }

                networkInfoListExisted.add(orgId);
            }
            logger.info("已存在的网点："+networkInfoListExisted.toString());

            int result = networkService.saveNetworkList(networkInfoListToSave);
            beanCruder.save(msbNetworkInfos);
            logger.info("新增"+result+"条数据");
            if (result > 0){
                JsonUtil.setValue(returnNode,"status","1");
                JsonUtil.setValue(returnNode,"message","新增"+result+"条数据");
            }else {
                JsonUtil.setValue(returnNode,"status","0");
                JsonUtil.setValue(returnNode,"message","新增失败，请重试!");
            }
        }else {
            JsonUtil.setValue(returnNode,"status","1");
            JsonUtil.setValue(returnNode,"message","新增0条数据");
            logger.warn("获取参数数组长度为0");
        }

        JsonUtil.setValue(returnNode,"responseTime",DateUtil.getNowTime());

        return returnNode;
    }


    /**
     * 获取当前网点下已保存的网点
     * @param userOrg 用户机构
     */
    @GetMapping("/getExistedNetwork/{userOrg}")
    @ResponseBody
    public List<NetworkInfoPO> getExistedNetwork(@PathVariable("userOrg") String userOrg){

        List<NetworkInfoPO> existedNetwork = networkService.getExistedNetwork(userOrg);

        return existedNetwork;
    }

    /**
     * 删除网点或自助银行图片
     * @param id
     * @return
     */
    @GetMapping("/deleteImgById/{id}")
    @ResponseBody
    public int deleteImgById(@PathVariable("id") String id){
        int res = beanCruder.execute("delete from MSB_IMAGE_RECORD where ID=:id", MapKit.mapOf("id", id));
        return res;
    }

    /**
     * 删除网点及其相关数据
     * @param id
     * @return
     */
    @GetMapping("/deleteNetById/{id}")
    @ResponseBody
    @Transactional
    public int deleteNetById(@PathVariable("id") String id){
        int res = beanCruder.execute("delete from NETWORK_INFO where ID=:id", MapKit.mapOf("id", id));
        int rst = beanCruder.execute("delete from MSB_NETWORK_INFO where NETWORK_NO=:id", MapKit.mapOf("id", id));
        return Math.min(res,rst);
    }

    public void closeStream(InputStream is,OutputStream os) throws IOException{
        if (is != null){
            is.close();
        }
        if (os != null){
            os.close();
        }
    }

    @GetMapping("/test")
    @ResponseBody
    public String test(){
        return path;
    }
}
