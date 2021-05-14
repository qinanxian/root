package com.vekai.crops.codetodo.businesstype.service;

import cn.fisok.raw.kit.DateKit;
import cn.fisok.sqloy.core.BeanCruder;
import cn.fisok.sqloy.serialnum.finder.SerialNumberGeneratorFinder;
import cn.fisok.sqloy.serialnum.generator.SerialNumberGenerator;
import com.vekai.auth.holder.AuthHolder;
import com.vekai.crops.codetodo.businesstype.entity.MsbBusinessType;
import com.vekai.crops.codetodo.image.entity.MsbImageRecord;
import com.vekai.crops.codetodo.image.service.ImageRecordService;
import com.vekai.crops.common.service.FileGatwayServiceImpl;
import com.vekai.crops.util.CommonUtil;
import com.vekai.crops.util.JsonUtil;
import cn.fisok.raw.kit.Base64Kit;
import cn.fisok.raw.kit.MapKit;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class BusinessTypeService {

    protected static Logger logger = LoggerFactory.getLogger(BusinessTypeService.class);

    //图片的临时目录
    @Value("${tempFile.originFilePath}")
    String imgTempPath;

    @Autowired
    private BeanCruder beanCruder;
    @Autowired
    private FileGatwayServiceImpl fileGatwayService;
    @Autowired
    private ImageRecordService imageRecordService;
    @Autowired
    private SerialNumberGeneratorFinder finder;

    public int deleteDelFlagById(String businessTypeId){
        String sql = "delete from MSB_BUSINESS_TYPE where ID = :businessTypeId";
        Map<String,Object> params = new HashMap<>();
        params.put("businessTypeId",businessTypeId);

        int result = beanCruder.execute(sql, params);

        return result;
    }

    /**
     * 上传业务流程图
     * @param multipartFile
     * @param businessTypeId   关联业务表id
     * @param realType    关联类型，0001为一般网点，0002为自助银行，0003为业务类型
     * @return 保存在MSB_BUSINESS_TYPE表中的数据，若文件保存失败，则返回null
     */
    public MsbImageRecord uploadBusinessFlowImg(String businessTypeId,MultipartFile multipartFile,String realType) throws Exception {

        InputStream clientInputStream = multipartFile.getInputStream();
        long clientFileSize = multipartFile.getSize();
        logger.info("客户端上传文件的大小: "+clientFileSize);
        String originalFilename = multipartFile.getOriginalFilename();
        logger.info("客户端上传文件名: "+originalFilename);

        //原图
        File originImg = new File(imgTempPath+"/"+originalFilename);
        logger.info("原图的临时路径: "+originImg.getAbsolutePath());
        FileUtils.copyInputStreamToFile(clientInputStream,originImg);
        //缩略图
        File compressedImg = new File(imgTempPath+"/"+ originalFilename.replace(".","_s."));
        logger.info("缩略图的临时路径: "+compressedImg.getAbsolutePath());

        String fileId = "";

        fileId = CommonUtil.getUUID();
        logger.info("fileId: "+fileId);
        String uploadRes = fileGatwayService.uploadFile(multipartFile.getInputStream(), fileId);
        String status = JsonUtil.getJsonStringValue(JsonUtil.getJson(uploadRes),"status");
        boolean uploadFileResult = "success".equals(status);
//        boolean uploadFileResult = true;

        if (uploadFileResult){
            //文件保存成功
            logger.info("文件上传成功");

            MsbBusinessType businessType = null;
            //在新增业务类型时，businessType表中的id为null，因此先插入一条数据以生成主键id
            if (!businessTypeId.equals("null")){
                businessType = beanCruder.selectOneById(MsbBusinessType.class, MapKit.mapOf("id",businessTypeId));
                businessType.setFlowFileId(fileId);
            } else {
                businessType = new MsbBusinessType();
                SerialNumberGenerator<String> serialNumberGenerator = finder.find("MSB_BUSINESS_TYPE.ID");
                String next = serialNumberGenerator.next("MSB_BUSINESS_TYPE.ID");
                businessType.setId(next);
                businessType.setFlowFileId(fileId);
                businessType.setCreatedBy(AuthHolder.getUser().getId());
                businessType.setCreatedTime(DateKit.now());
            }

            beanCruder.save(businessType);

            //将业务流程图的信息插入到MSB_IMAGE_RECORD表中，并关联上边获取到的id
            MsbImageRecord imageRecord = beanCruder.selectOne(MsbImageRecord.class,
                    "select * from MSB_IMAGE_RECORD where REAL_ID = :realId and REAL_TYPE = :realType","realId", businessType.getId(),"realType",realType);
            if(CommonUtil.ifIsEmpty(imageRecord)){
                imageRecord = new MsbImageRecord();
                imageRecord.setRealId(businessType.getId());
                //0001为一般网点，0002为自助银行，0003为业务类型
                imageRecord.setRealType(realType);
            }
            imageRecord.setBigImageId(fileId);
            imageRecord.setImageName(originalFilename);
            //将大图转换成缩略图，第三个参数值越大，压缩率越低
            imageRecordService.compress(originImg,compressedImg,100);
            //将缩略图转成base64
            String compressedImgBase64 = Base64Kit.encodeFile(compressedImg.getAbsolutePath());
            //将缩略图base64字符串保存在imageRecord中
            imageRecord.setImageBase("data:image/jpg;base64,"+compressedImgBase64);
            logger.info("保存图片的信息："+imageRecord.toString());

            //保存imageRecord对象,
            Integer result = imageRecordService.saveImageRecord(imageRecord);
            if (result>0){
                logger.info("imageRecord保存成功");
            } else {
                logger.info("imageRecord保存失败");
            }

            return imageRecord;

        }else {
            logger.info("文件上传失败");
            return null;
        }
    }

    public MsbBusinessType getBusinessTypeByFileId(String fileId){
        String sql = "select * from MSB_BUSINESS_TYPE where FLOW_FILE_ID = :fileId";
        MsbBusinessType businessType = beanCruder.selectOne(MsbBusinessType.class, sql, "fileId", fileId);
        return businessType;
    }

}
