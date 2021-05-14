package com.vekai.common.fileman.kit;

import com.vekai.common.fileman.entity.FileEntity;
import cn.fisok.raw.kit.FileKit;
import cn.fisok.raw.kit.StringKit;
import org.springframework.web.multipart.MultipartFile;

public abstract class FileManKit {
    public static final FileEntity parseFileEntity(MultipartFile multipartFile){
        //存储文件
        FileEntity fileEntity = new FileEntity();
        fillFileEntity(multipartFile,fileEntity);
        return fileEntity;
    }

    public static final void fillFileEntity(MultipartFile multipartFile,FileEntity fileEntity){
        String fileName = multipartFile.getOriginalFilename();
        String contentType = multipartFile.getContentType();
        String name = multipartFile.getName();
        long size = multipartFile.getSize();

        String suffix = FileKit.getSuffix(fileName);
        if(StringKit.isBlank(suffix))suffix = "";
        else suffix = "."+suffix;
        final String fixedSuffix = suffix;

        //存储文件
//        fileEntity.setStoreServiceName(FileManConsts.STORE_SERVICE_DOSSIER);    //指定文件用什么样的服务来保存
//        fileEntity.setObjectType("DOSSER_ITEM");
//        fileEntity.setObjectId(dossierItemId);
        fileEntity.setShowCode(StringKit.uuid());
        fileEntity.setContentType(contentType);
        fileEntity.setFileSize(size);
        fileEntity.setName(fileName);

    }

    public static final String getSuffix(MultipartFile multipartFile){
        String fileName = multipartFile.getOriginalFilename();
        return getSuffix(fileName);
    }
    public static final String getSuffix(String fileName){
        String suffix = FileKit.getSuffix(fileName);
        if(StringKit.isBlank(suffix))suffix = "";
        else suffix = "."+suffix;
        return suffix;
    }
}
