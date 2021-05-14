package com.vekai.workflow.nopub.param;

import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.kit.ValidateKit;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.workflow.nopub.param.model.WorkflowConfBom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by apachechen on 2018/3/1.
 */
@Component
public class WorkflowTaskConfBomProcessor {

    @Autowired
    private BeanCruder beanCruder;

    private final static String WKFL_CONF_BOM_TABLE = "WKFL_CONF_BOM";
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public List<WorkflowConfBom> getWorkflowBom(String definitionKey) {
        ValidateKit.notNull(definitionKey, "传入的参数definitionKey为空!");
        return beanCruder
            .selectList(WorkflowConfBom.class,
                "SELECT * from " + WKFL_CONF_BOM_TABLE
                    + " where PROC_DEF_KEY=:procDefKey",
                MapKit.mapOf("procDefKey", definitionKey));
    }

}
