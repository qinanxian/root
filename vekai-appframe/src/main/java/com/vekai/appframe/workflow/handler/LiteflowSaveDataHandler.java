package com.vekai.appframe.workflow.handler;

import cn.fisok.raw.lang.MapObject;
import com.vekai.dataform.handler.impl.MapDataOneHandler;
import com.vekai.dataform.model.DataForm;
import com.vekai.workflow.liteflow.service.LiteFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @Author: qyyao
 * @Description:
 * @Date: Created in 19:46 25/04/2018
 */
@Component
public class LiteflowSaveDataHandler extends MapDataOneHandler {

    @Autowired
    private LiteFlowService liteFlowService;


    @Override
    public int save(DataForm dataForm, MapObject object) {
        String templateId = (String) object.get("templateId");
        String name = "";
        String defImplicit = "";

        if (null ==templateId){
            name=(String) object.get("name");
            defImplicit = (String) object.get("defImplicit");
            liteFlowService.saveTemplate(name,defImplicit);
        }
        if (null!=templateId){
            defImplicit=(String) object.get("defImplicit");
            liteFlowService.updateTemplate(templateId,defImplicit);
        }
        return 0;
    }
}
