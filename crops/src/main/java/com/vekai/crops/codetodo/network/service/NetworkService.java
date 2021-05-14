package com.vekai.crops.codetodo.network.service;

import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.crops.codetodo.image.entity.MsbImageRecord;
import com.vekai.crops.codetodo.image.service.ImageRecordService;
import com.vekai.crops.codetodo.network.entity.MsbNetworkInfo;
import com.vekai.crops.common.entity.NetworkInfoPO;
import com.vekai.crops.common.service.FileGatwayServiceImpl;
import com.vekai.crops.util.CommonUtil;
import com.vekai.crops.util.JsonUtil;
import cn.fisok.raw.kit.Base64Kit;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

@Service
public class NetworkService {

    protected static Logger logger = LoggerFactory.getLogger(NetworkService.class);

    //图片的临时目录
    @Value("${tempFile.originFilePath}")
    String imgTempPath;

    @Autowired
    private FileGatwayServiceImpl fileGatwayService;
    @Autowired
    private ImageRecordService imageRecordService;
    @Autowired
    private BeanCruder beanCruder;

    /**
     * 上传网点照片
     * @param multipartFile
     * @param netWorkId   : MSB_NETWORK_INFO表中的主键id
     * @param realType    : //图片表的关联类型，一般网点为0001，自助银行为0002, 业务类型为0003
     * @return
     * @throws IOException
     */
    public boolean uploadNetworkImg(MultipartFile multipartFile,String netWorkId,String realType) throws IOException {

        //客户端上传文件的大小
        long clientFileSize = multipartFile.getSize();
        //客户端上传文件的名字，包含扩展名
        String originalFilename = multipartFile.getOriginalFilename();
        logger.info("客户端上传的文件的大小："+clientFileSize);

        //客户端上传文件的输入流
        InputStream clientInputStream = multipartFile.getInputStream();
        //原图
        File originImg = new File(imgTempPath+"/"+originalFilename);
        logger.info("原图的临时文件路径: "+originImg.getAbsolutePath());
        FileUtils.copyInputStreamToFile(clientInputStream,originImg);
        //缩略图
        File compressedImg = new File(imgTempPath+"/"+ originalFilename.replace(".","_s."));
        logger.info("缩略图的临时文件路径: "+compressedImg.getAbsolutePath());


        //从网关中获取的文件id
        String fileId = "";

        //保存文件信息到数据库表中的结果
        boolean saveImageInfoResult = false;
        
        try {
            
            //获取网关文件Id
            fileId = CommonUtil.getUUID();
            logger.info("网关文件id："+fileId);

            //上传文件
            String uploadRes = fileGatwayService.uploadFile(multipartFile.getInputStream(), fileId);

            //将客户端上传的文件大小和网关返回的文件大小进行比较，相同则返回true，否则返回false
            String status = JsonUtil.getJsonStringValue(JsonUtil.getJson(uploadRes),"status");
            boolean uploadFileResult = "success".equals(status);
//            boolean uploadFileResult = true;

            if (uploadFileResult) {
                //文件保存成功
                logger.info("文件保存成功");

                //将该图片文件保存在图片表中，并与网点信息表关联
                MsbImageRecord imageRecord = new MsbImageRecord();
                imageRecord.setRealId(netWorkId);
                imageRecord.setRealType(realType);
                imageRecord.setBigImageId(fileId);
                imageRecord.setImageName(originalFilename);
                //将大图转换成缩略图，第三个参数值越大，压缩率越低
                imageRecordService.compress(originImg,compressedImg,100);
                //将缩略图转换成base64
                String compressedImgBase64 = Base64Kit.encodeFile(compressedImg.getAbsolutePath());
                //将缩略图base64字符串保存在imageRecord的imageBase字段中
                imageRecord.setImageBase("data:image/jpg;base64,"+compressedImgBase64);
                logger.info("保存图片的信息："+imageRecord.toString());

                //保存imageRecord对象,
                Integer result = imageRecordService.saveImageRecord(imageRecord);
                if (result > 0){
                    saveImageInfoResult = true;
                    logger.info("将网点照片信息保存在数据表中的结果：成功");
                } else {
                    saveImageInfoResult = false;
                    logger.error("将网点照片信息保存在数据表中的结果：失败");
                }

            } else {
                //文件保存失败
                logger.error("文件保存失败");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (clientInputStream != null){
                clientInputStream.close();
            }
        }
        //删除原图和缩略图的临时文件
        originImg.delete();
        compressedImg.delete();


        return saveImageInfoResult;
    }

    /**
     * 根据NETWORK_INFO表中的id获取网点照片
     * @param networkId-----网点id
     * @param realType-----图片表的关联类别，0001为一般网点，0002为自助银行，0003为业务类型
     * @return
     */
    public List<MsbImageRecord> getImageRecordByNetworkId(String networkId,String realType){

        List<MsbImageRecord> imageRecordList = imageRecordService.getImageRecordByRealId(networkId,realType);

        return imageRecordList;
    }

    /**
     * 新增网点，
     * @param networkInfoList
     * @return
     */
    public int saveNetworkList(List<NetworkInfoPO> networkInfoList){
        int result = beanCruder.save(networkInfoList);
        return result;
    }

    /**
     * 根据orgId判断NETWORK_INFO表里是否存在该数据
     * @param orgId
     * @return
     */
    public boolean isExistNetworkByOrgId(String orgId){

        String sql = "select * from MSB_NETWORK_INFO where NETWORK_NO = :orgId";

        MsbNetworkInfo msbNetworkInfo = beanCruder.selectOne(MsbNetworkInfo.class, sql, "orgId", orgId);

        return CommonUtil.ifIsNotNull(msbNetworkInfo);
    }

    /**
     * 获取当前网点下已保存的网点
     * @param userOrg
     * @return
     */
    public List<NetworkInfoPO> getExistedNetwork(String userOrg){
        String sql = "SELECT * FROM MSB_NETWORK_INFO A,AUTH_ORG B" +
                " WHERE " +
                " A.NETWORK_NO = B.CODE AND " +
                " LOCATE(CONCAT(CONCAT('/',:userOrg),'/'),CONCAT(CONCAT('/',B.SORT_CODE),'/'))>0";
        List<NetworkInfoPO> existedNetwork = beanCruder.selectList(NetworkInfoPO.class, sql,"userOrg",userOrg);

        return existedNetwork;
    }
}
