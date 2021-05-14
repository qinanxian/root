package com.vekai.workflow.nopub.conf.bom;

import cn.fisok.raw.kit.MapKit;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.workflow.nopub.bom.BomAttribute;
import com.vekai.workflow.nopub.conf.entity.WorkflowConfigureBom;
import com.vekai.workflow.nopub.bom.BomLoader;
import com.vekai.workflow.nopub.bom.BomObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 流程业务参数加载类
 * @auther  左晓敏 <xmzuo@amarsoft.com>
 * @date 2018-02-06
 */
@Component
public class BomFromWorkflowConfBomLoader implements BomLoader{
    @Autowired
    private BeanCruder beanCruder;

    @Override
    public BomObject load(String bomDefinitionKey) {
        BomObject bomObject = new BomObject();

        List<WorkflowConfigureBom> bomList = beanCruder.selectList(WorkflowConfigureBom.class,
                "select * from WKFL_CONF_BOM where PROC_DEF_KEY=:procDefKey",
                MapKit.mapOf("procDefKey", bomDefinitionKey));
        for(WorkflowConfigureBom workflowConfigureBom : bomList){
            bomObject.appendAttribute(new BomAttribute(workflowConfigureBom.getName(),"",workflowConfigureBom.getValue()));
        }
        return bomObject;
    }
}
