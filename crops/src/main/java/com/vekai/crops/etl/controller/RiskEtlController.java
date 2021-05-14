package com.vekai.crops.etl.controller;

import com.vekai.crops.common.constant.FileCmonConsts;
import com.vekai.common.fileman.entity.FileEntity;
import com.vekai.common.fileman.kit.FileManKit;
import com.vekai.common.fileman.service.FileManService;
import cn.fisok.raw.kit.IOKit;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.lang.BizException;
import cn.fisok.sqloy.core.BeanCruder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

@RestController
@RequestMapping("/risk/etl/")
public class RiskEtlController {
    protected static Logger logger = LoggerFactory.getLogger(RiskEtlController.class);

    @Autowired
    private FileManService fileManService;
    @Autowired
    private BeanCruder beanCruder;

    @PostMapping("/updateFile/{jobId}")
    public Integer updatePersonFile(StandardMultipartHttpServletRequest request,
                                    @PathVariable("jobId") String jobId) {
        if (request.getFileMap().isEmpty()) return null;
        MultipartFile multipartFile = request.getFileMap().values().iterator().next();
        if (multipartFile.isEmpty()) return null;
        InputStream inputStream = null;
        try {
            inputStream = multipartFile.getInputStream();

            //存储文件
            final String suffix = FileManKit.getSuffix(multipartFile);
            FileEntity fileEntity = FileManKit.parseFileEntity(multipartFile);

            String objectType = "Kettle";
            fileEntity.setStoreServiceName(FileCmonConsts.STORE_SERVICE_KETTLE);

            fileEntity.setObjectType(objectType);
            fileEntity.setObjectId(jobId);

            //直接根据文件ID保存文件内容
            fileManService.saveAndStoreFile(fileEntity, inputStream, false, fe -> {
                String fileName = fileEntity.getFileId()+"-"+fileEntity.getName();
                fileEntity.setStoredContent(fileName);
            });

            String updateTableSQL = "update ETL_SCHEDULE_JOB set DEFINE_FILE=:fileName where JOB_ID=:jobId";

            HashMap<String, String> param = new HashMap<>();
            param.put("fileName", fileEntity.getStoredContent());
            param.put("jobId", jobId);
            beanCruder.execute(updateTableSQL, param);

        } catch (IOException e) {
            throw new BizException("上传文件出错", e);
        } finally {
            IOKit.close(inputStream);
        }
        return 1;
    }

    @RequestMapping(value = "/restStatus", method = {RequestMethod.GET, RequestMethod.POST})
    public Integer restStatus(@RequestBody JobDTO job){
        beanCruder.execute("UPDATE ETL_SCHEDULE_JOB SET JOB_STATUS='' WHERE JOB_ID=:jobId",
                MapKit.mapOf("jobId", job.getJobId()));
        return 1;
    }
}
