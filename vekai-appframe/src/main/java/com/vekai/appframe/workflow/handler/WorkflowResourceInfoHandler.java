package com.vekai.appframe.workflow.handler;

import cn.fisok.raw.lang.MapObject;
import com.vekai.dataform.handler.impl.MapDataOneHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.BeanKit;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.workflow.nopub.conf.entity.WorkflowConfigureResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WorkflowResourceInfoHandler extends MapDataOneHandler {

    @Autowired
    protected BeanCruder beanCruder;

    @Override
    public int save(DataForm dataForm, MapObject object) {
        String resourceId = object.getValue("resourceId").strValue();
        String procDefKey = object.getValue("procDefKey").strValue();
        String sql = "SELECT * FROM WKFL_CONF_RESOURCE WHERE RESOURCE_ID=:resourceId AND PROC_DEF_KEY=:procDefKey";
        WorkflowConfigureResource configureResource = beanCruder.selectOneById(WorkflowConfigureResource.class,
                MapKit.mapOf("resourceId", resourceId, "procDefKey", procDefKey));
        if (null == configureResource) {
            configureResource = new WorkflowConfigureResource();
        }
        BeanKit.copyProperties(object, configureResource);
        return beanCruder.save(configureResource);
    }
}
