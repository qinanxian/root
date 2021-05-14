package com.vekai.appframe.conf.dossier.controller;


import cn.fisok.web.kit.HttpKit;
import com.vekai.appframe.conf.dossier.dao.ConfDossierDao;
import com.vekai.appframe.conf.dossier.model.ConfDossierItem;
import com.vekai.appframe.conf.dossier.service.ConfDossierService;
import com.vekai.common.fileman.entity.FileEntity;
import com.vekai.common.mediatext.entity.MediaTextEntity;
import com.vekai.common.mediatext.service.MediaTextService;
import cn.fisok.web.holder.WebHolder;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.kit.ValidateKit;
import cn.fisok.sqloy.core.BeanCruder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/conf/dossier")
public class ConfDossierController {
    @Autowired
    private ConfDossierService confDossierService;
    @Autowired
    private BeanCruder beanCruder;
    @Autowired
    private MediaTextService mediaTextService;
    @Autowired
    ConfDossierDao confDossierDao;

    @PostMapping("/uploadItemTplFile/{itemDefKey}/{fileId}")
    @Transactional
    public FileEntity uploadItemTplFile(StandardMultipartHttpServletRequest request, @PathVariable("itemDefKey") String itemDefKey, @PathVariable("fileId") String fileId) {
        if("undefined".equalsIgnoreCase(fileId))fileId = "";
        if("null".equalsIgnoreCase(fileId))fileId = "";
        FileEntity fileEntity = confDossierService.uploadItemTplFile(request,itemDefKey,fileId);
        Map<String,String> params = MapKit.mapOf("itemDefKey",itemDefKey,"tplFileId",fileEntity.getFileId());
        String sql = "UPDATE CONF_DOSSIER_ITEM SET TPL_FILE_ID=:tplFileId WHERE ITEM_DEF_KEY=:itemDefKey";
        beanCruder.execute(sql,params);
        return fileEntity;
    }

    @GetMapping("/showItemTplFile/{itemDefKey}/{fileId}")
    public void showItemTplFile(@PathVariable("itemDefKey") String itemDefKey,@PathVariable("fileId") String fileId, HttpServletResponse response){
        String tplFileId = beanCruder.selectOne(String.class,"select TPL_FILE_ID FROM CONF_DOSSIER_ITEM WHERE ITEM_DEF_KEY=:itemDefKey", MapKit.mapOf("itemDefKey",itemDefKey));
        FileEntity fileEntity = confDossierService.getFileEntity(tplFileId);
        if(fileEntity != null){
            confDossierService.renderOutputStream(fileEntity,response,null);
        }
    }

    @GetMapping("/downItemTplFile/{itemDefKey}/{fileId}")
    public void downItemTplFile(@PathVariable("itemDefKey") String itemDefKey,@PathVariable("fileId") String fileId, HttpServletResponse response){
        String tplFileId = beanCruder.selectOne(String.class,"select TPL_FILE_ID FROM CONF_DOSSIER_ITEM WHERE ITEM_DEF_KEY=:itemDefKey", MapKit.mapOf("itemDefKey",itemDefKey));
        FileEntity fileEntity = confDossierService.getFileEntity(tplFileId);
        if(fileEntity != null){
            String fileName = HttpKit.iso8859(fileEntity.getName(), WebHolder.getRequest());

            Map<String,String> headers = MapKit.mapOf("Content-Disposition","attachment; filename=" + fileName);
            fileEntity.setContentType(null);    //设置为空之后，会使用默认的:octets/stream
            confDossierService.renderOutputStream(fileEntity,response,headers);
        }
    }

    @PostMapping("/removeTplFile/{itemDefKey}/{fileId}")
    public int removeTplFile(@PathVariable("itemDefKey") String itemDefKey,@PathVariable("fileId") String fileId, HttpServletResponse response){
        return confDossierService.removeTplFile(itemDefKey);
    }

    @GetMapping("/getMediaText/{itemDefId}")
    public MediaTextEntity getMediaTextEntity(@PathVariable("itemDefId") String itemDefId){
        MediaTextEntity ret = new MediaTextEntity();
        ConfDossierItem dossierItem = confDossierDao.queryConfDossierItem(itemDefId);
        if(dossierItem == null) return ret;

        String mediaTextId = dossierItem.getMediaTextId();
        ret = mediaTextService.getMediaText(mediaTextId);
        return ret;
    }

    @PostMapping("/saveMediaText/{itemDefId}")
    public int saveMediaTextEntity(@PathVariable("itemDefId") String itemDefId,@RequestBody MediaTextEntity mediaTextEntity){
        ConfDossierItem dossierItem = confDossierDao.queryConfDossierItem(itemDefId);
        if(dossierItem == null) return 0;
        ValidateKit.notNull(dossierItem,"配置项{0}不存在",itemDefId);
        String mediaTextId = StringKit.nvl(dossierItem.getMediaTextId(),"");

        MediaTextEntity persisObject = new MediaTextEntity();
        persisObject.setMediaTextId(mediaTextId);
        persisObject.setContentRaw(mediaTextEntity.getContentRaw());
        persisObject.setContentData(mediaTextEntity.getContentData());

        int ret = mediaTextService.saveMediaText(persisObject);
        dossierItem.setMediaTextId(persisObject.getMediaTextId());

        ret += beanCruder.save(dossierItem);

        return ret;
    }



}
