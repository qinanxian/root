package com.vekai.crops.obiz.application.dector;


import com.vekai.crops.dossier.model.Dossier;
import com.vekai.crops.dossier.service.DossierService;
import com.vekai.crops.obiz.application.entity.ObizApplication;
import com.vekai.base.detector.DetectorContext;
import com.vekai.base.detector.DetectorItemExecutor;
import com.vekai.base.detector.DetectorMessage;
import com.vekai.common.fileman.entity.FileEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 检查影像资料的完整性
 */
@Component
public class DossierRequireCheck implements DetectorItemExecutor {
    @Autowired
    DossierService dossierService;

    public DetectorMessage exec(DetectorContext ctx) {
        DetectorMessage ret = new DetectorMessage();

        ObizApplication obizApplication = ctx.getParam("application").objectVal(ObizApplication.class);
        String dossierId = obizApplication.getDossierId();
        Dossier dossier = dossierService.getDossier(dossierId);
        List<String> messageList = new ArrayList<String>();
        if (dossier == null) {
            messageList.add("资料清单对象不存在");
        } else {
            dossier.getItemList().forEach(dossierItem -> {
                List<? extends FileEntity> fileEntities = dossierItem.getFileEntities();
                if ("MUST".equals(dossierItem.getImportance()) && (fileEntities == null || fileEntities.size() == 0)) {
                    messageList.add("* [" + dossierItem.getItemName() + "] 必需上传");
                }
            });

            if (messageList.size() == 0) {
                ret.setPass(true);
            } else {
                ret.setPass(false);
                ret.setMessage(messageList);
            }
        }


        return ret;
    }
}
