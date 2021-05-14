package com.vekai.crops.dossier.controller;

import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.crops.dossier.model.Dossier;
import com.vekai.crops.dossier.service.DossierService;
import com.vekai.common.fileman.entity.FileEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/common/dossier")
public class DossierController {
    @Autowired
    private DossierService dossierServicer;
    @Autowired
    BeanCruder beanCruder;


    @GetMapping("/byId/{dossierId}")
    public Dossier getDossier(@PathVariable("dossierId") String dossierId) {
        return dossierServicer.getDossier(dossierId);
    }

    @GetMapping("/queryDossierByObjectType/{objectType}/{objectId}")
    public String getDossierId(@PathVariable("objectType") String objectType,@PathVariable("objectId") String objectId) {
        Dossier dossier = dossierServicer.queryDossierByObjectType(objectType,objectId);
        if(dossier != null)return dossier.getDossierId();
        return "";
    }

    @PostMapping("/upload/{dossierItemId}")
    public FileEntity uploadAndSaveFile(@PathVariable("dossierItemId") String dossierItemId, StandardMultipartHttpServletRequest request) {
        FileEntity fileEntity = dossierServicer.execUpload(dossierItemId,request);
        addFileToItem(fileEntity.getFileId(),dossierItemId);
        return fileEntity;
    }

    @GetMapping("/showFile/{showCode}")
    public void showFile(@PathVariable("showCode") String showCode, HttpServletResponse response){
        dossierServicer.renderOutputStream(showCode,response);
    }

    @PostMapping("/remove/{dossierItemId}/{fileId}")
    public int removeDossierItemFile(@PathVariable("dossierItemId") String dossierItemId, @PathVariable("fileId") String fileId) {
        return removeFileFromItem(fileId,dossierItemId);
    }

    @PostMapping("/remove/{dossierItemId}")
    public int removeDossierItemFileList(@PathVariable("dossierItemId") String dossierItemId, @RequestBody List<String> fileList) {
        return removeFileListFromItem(fileList, dossierItemId);
    }

    public int addFileToItem(String fileEntityId, String dossierItemId) {
        return dossierServicer.addFileToItem(fileEntityId, dossierItemId);
    }

    public int removeFileFromItem(String fileId,String dossierItemId) {
        return dossierServicer.removeFileFromItem(fileId,dossierItemId);
    }
    public int removeFileListFromItem(List<String> fileList, String dossierItemId) {
        return dossierServicer.removeFileListFromItem(fileList, dossierItemId);
    }
}
