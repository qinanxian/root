package com.vekai.showcase.controller;

import cn.fisok.raw.kit.IOKit;
import cn.fisok.raw.lang.BizException;
import cn.fisok.web.kit.HttpKit;
import com.vekai.common.fileman.FileManConsts;
import com.vekai.common.fileman.entity.FileEntity;
import com.vekai.common.fileman.kit.FileManKit;
import com.vekai.common.fileman.service.FileManService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/showcase/FileOperateControllerDemo")
public class FileOperateControllerDemo {

    @Autowired
    private FileManService fileManService;
    protected static Logger logger = LoggerFactory.getLogger(FileOperateControllerDemo.class);

    @PostMapping("/updatePersonFile/{personId}/{fileId}")
    public Integer updatePersonFile(StandardMultipartHttpServletRequest request
            , @PathVariable("personId") String personId
            , @PathVariable("fileId") String fileId) {
        if (request.getFileMap().isEmpty()) return null;
        MultipartFile multipartFile = request.getFileMap().values().iterator().next();
        if (multipartFile.isEmpty()) return null;
        String objectType = "DEMO_PERSON";
        InputStream inputStream = null;
        try {
//            docFileManageService.saveFile(fileId, multipartFile, false, fe -> {
//                fe.setObjectId(personId);
//                fe.setObjectType(objectType);
//            });
            inputStream = multipartFile.getInputStream();

            //存储文件
            final String suffix = FileManKit.getSuffix(multipartFile);
            FileEntity fileEntity = FileManKit.parseFileEntity(multipartFile);
            fileEntity.setStoreServiceName(FileManConsts.STORE_SERVICE_DEFAULT);    //指定文件用什么样的服务来保存
            fileEntity.setObjectType(objectType);
            fileEntity.setObjectId(personId);

            //直接根据文件ID保存文件内容
            fileManService.saveAndStoreFile(fileEntity, inputStream, false, fe -> {
                fileEntity.setStoredContent(fileEntity.getFileId() + suffix);
            });
        } catch (IOException e) {
            throw new BizException("上传文件出错", e);
        } finally {
            IOKit.close(inputStream);
        }
        return 1;
    }

    @GetMapping("/downloadPersonFile/{fileId}")
    public void downloadPersonFile(@PathVariable("fileId") String fileId,
                                   HttpServletResponse response, HttpServletRequest request) {
        FileEntity fileEntity = fileManService.getFileEntity(fileId);
        InputStream inputStream = fileManService.openFileInputStream(fileEntity);
        HttpKit.renderStream(response, inputStream, fileEntity.getContentType(), null);
    }

    /*@PostMapping("/updatePersonFileByChunk/{personId}/{fileId}")
    public MapObject updatePersonFileByChunk(UploadFileChunk chunk,
                                             @PathVariable("personId") String personId,
                                             @PathVariable("fileId") String fileId) {
        MapObject ret = null;

        return ret;
    }*/


}
