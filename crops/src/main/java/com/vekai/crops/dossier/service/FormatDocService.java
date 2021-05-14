package com.vekai.crops.dossier.service;


import cn.fisok.raw.kit.BeanKit;
import cn.fisok.raw.kit.IOKit;
import cn.fisok.raw.kit.StringKit;
import com.vekai.crops.dossier.model.Dossier;
import com.vekai.crops.dossier.model.DossierItem;
import com.vekai.appframe.conf.dossier.dao.ConfDossierDao;
import com.vekai.appframe.conf.dossier.model.ConfDossierItemFileEntity;
import com.vekai.common.fileman.FileManConsts;
import com.vekai.common.fileman.entity.FileEntity;
import com.vekai.common.fileman.kit.FileManKit;
import com.vekai.common.fileman.service.FileManService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
public class FormatDocService{

    @Autowired
    private ConfDossierDao confDossierDao;
    @Autowired
    DossierService dossierService;
    @Autowired
    FileManService fileManService;

    /**
     * 创建格式化文档对象
     * @param dosserDefKey
     * @param objectType
     * @param objectId
     * @return
     */
    public Dossier createFormatDoc(String dosserDefKey, String objectType, String objectId){
        //1. 创建格式化文档记录对象
        Dossier dossier = dossierService.createDossier(dosserDefKey,objectType,objectId);

        //1.在定义模板中找到该定义模板下的文件模板明细
        List<ConfDossierItemFileEntity> itemFileEntities = confDossierDao.queryTplFileEntities(dosserDefKey);
        if(itemFileEntities == null || itemFileEntities.size()==0){
            throw new IllegalStateException(StringKit.format("生成格式化报告异常，{0}-没有任何一个模板文件存在",dosserDefKey));
        }
        //2.使用文件模板创建文件实例
        itemFileEntities.forEach(item -> {
            String tplFileId = item.getFileId();
//            FileEntity tplFileEntity = fileManService.getFileEntity(tplFileId);
            FileEntity tplFileEntity = new FileEntity();
            BeanKit.copyProperties(item,tplFileEntity);
            if(dossier.getItemList() == null || dossier.getItemList().size()==0) return;

            //找到实例中对应的项
            DossierItem dossierItem = dossier.getItemList().stream()
                    .filter(filterItem -> filterItem.getItemDefKey().equals(item.getItemDefKey()))
                    .findFirst()
                    .get();
            if(dossierItem == null) throw new IllegalStateException(StringKit.format("生成格式化报告异常，{0}-{1}对应的实例数据项不存在",dosserDefKey,item.getItemDefKey()));

            //从模板复制文件到实例
            InputStream tplInputStream = null;
            FileEntity fileEntity = null;
            try{
                tplInputStream = fileManService.openFileInputStream(tplFileEntity);

                fileEntity = new FileEntity();
                BeanKit.copyProperties(tplFileEntity,fileEntity);
                fileEntity.setFileId("");
                fileEntity.setStoreServiceName(FileManConsts.STORE_SERVICE_FORMATDOC);

                final String suffix = FileManKit.getSuffix(fileEntity.getName());
                fileManService.saveAndStoreFile(fileEntity,tplInputStream,false,fe -> {
                    fe.setStoredContent(fe.getFileId() + suffix);
                });
            }catch (Exception e){
                throw new IllegalStateException(StringKit.format("生成格式化报告异常，模板文件ID{0}-{1}[{2}]生成实例出错",dosserDefKey,item.getItemDefKey(),item.getItemName()),e);
            }finally {
                IOKit.close(tplInputStream);
            }

            //建立实例文件和明细项的关联
            dossierService.addFileToItem(fileEntity.getFileId(),dossierItem.getItemId());

        });

        return dossier;
    }
}
