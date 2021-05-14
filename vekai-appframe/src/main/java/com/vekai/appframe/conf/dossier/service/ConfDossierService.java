package com.vekai.appframe.conf.dossier.service;

import cn.fisok.raw.kit.*;
import cn.fisok.sqloy.core.BeanCruder;
import cn.fisok.web.kit.HttpKit;
import com.vekai.appframe.conf.dossier.dao.ConfDossierDao;
import com.vekai.common.fileman.FileManConsts;
import com.vekai.common.fileman.entity.FileEntity;
import com.vekai.common.fileman.kit.FileManKit;
import com.vekai.common.fileman.service.FileManService;
import cn.fisok.raw.lang.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Service
public class ConfDossierService {
    @Autowired
    FileManService fileManService;
    @Autowired
    ConfDossierDao confDossierDao;
    @Autowired
    BeanCruder beanCruder;


    public FileEntity getFileEntity(String fileEntityId) {
        return fileManService.getFileEntity(fileEntityId);
    }

    public FileEntity uploadItemTplFile(StandardMultipartHttpServletRequest request, String itemDefKey, String fileId) {

        MultipartFile multipartFile = request.getFileMap().values().iterator().next();
        if (multipartFile.isEmpty()) return null;

        InputStream inputStream = null;
        try {
//            fileEntity = fileManService.saveFile(fileId, multipartFile, true, fe -> {
//                fe.setObjectType("TPL_FORMAT_DOC"); //标识此文件为一个格式化文档模板
//                fe.setObjectId(itemDefKey);
//            });
            inputStream = multipartFile.getInputStream();

            //存储文件
            final String suffix = FileManKit.getSuffix(multipartFile);
            FileEntity fileEntity = null;
            if(StringKit.isNotBlank(fileId)){
                fileEntity = fileManService.getFileEntity(fileId);
            }
            if(fileEntity == null){
                fileEntity = new FileEntity();
                fileEntity.setFileId(fileId);
            }

            FileManKit.fillFileEntity(multipartFile,fileEntity);
            fileEntity.setStoreServiceName(FileManConsts.STORE_SERVICE_DOCTPL);    //指定文件用什么样的服务来保存
            fileEntity.setObjectType("TPL_FORMAT_DOC");
            fileEntity.setObjectId(itemDefKey);


            //直接根据文件ID保存文件内容
            fileManService.saveAndStoreFile(fileEntity, inputStream, true, fe->{
                fe.setStoredContent(fe.getFileId()+suffix);
            });

            return fileEntity;
        } catch (IOException e) {
            throw new BizException("上传文件出错,[" + e.getMessage() + "]");
        } finally {
            IOKit.close(inputStream);
        }
    }

    public void renderOutputStream(String showCode, HttpServletResponse response, Map<String, String> headers) {
        FileEntity fileEntity = fileManService.getFileEntityByShowCode(showCode);
        ValidateKit.notNull(fileEntity, "showCode={0}文件不存在！",showCode);

        renderOutputStream(fileEntity,response,headers);
    }

    public int removeTplFile(String itemDefKey) {
        String sql = "UPDATE CONF_DOSSIER_ITEM SET TPL_FILE_ID='' WHERE ITEM_DEF_KEY=:itemDefKey ";
        Map<String, String> params = MapKit.mapOf("itemDefKey",itemDefKey);
        return beanCruder.execute(sql, params);
    }

    public void renderOutputStream(FileEntity fileEntity, HttpServletResponse response, Map<String, String> headers) {
        ValidateKit.notNull(fileEntity, "文件不存在！",fileEntity);

        InputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;
        try {
            inputStream = fileManService.openFileInputStream(fileEntity);
            String contentType = StringKit.nvl(fileEntity.getContentType(),"octets/stream");
            HttpKit.renderStream(response, inputStream, contentType, headers);
        } catch (Exception e) {
            throw new BizException("打开文件资源失败，请检查文件资源是否存在,fileId=" + fileEntity.getFileId() + ",[" + e.getMessage() + "]");
        } finally {
            IOKit.close(inputStream);
            IOKit.close(outputStream);
        }
    }

}
