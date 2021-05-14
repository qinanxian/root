package com.vekai.workflow.nopub.resource;


import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.kit.ValidateKit;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.workflow.nopub.resource.model.enums.WorkflowResourceRight;
import com.vekai.workflow.nopub.resource.model.enums.YesOrNo;
import com.vekai.workflow.nopub.resource.model.WorkflowResource;
import org.activiti.engine.form.FormProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 流程任务资源处理类
 * 查找当前流程节点的流程资源
 */
@Component
public class WorkflowResourceProcessor {

    @Autowired
    private BeanCruder beanCruder;

    private final static String WKFL_CONF_RESOURCE_TABLE = "WKFL_CONF_RESOURCE";
    private Logger logger = LoggerFactory.getLogger(this.getClass());



    public List<WorkflowResource> getTaskResources(String definitionKey,
        List<FormProperty> formProperties) {
        List<WorkflowResource> resources = getWorkflowResources(definitionKey);
        return filterTaskResourcesByFormProperties(resources, formProperties);
    }

    /**
     * 获取流程实例资源
     * 要求开发人员在流程发起时传入流程实例资源所需的参数,否则有可能出现资源打开找不到参数的情况
     *
     * @param definitionKey
     * @return
     */
    public List<WorkflowResource> getProcInstResources(String definitionKey) {
        ValidateKit.notNull(definitionKey, "传入的参数definitionKey为空!");
        return beanCruder
            .selectList(WorkflowResource.class,
                "SELECT RESOURCE_ID as id,NAME,PROC_DEF_KEY,TYPE_ as type,SORT_CODE,ACTION as "
                    + "action,ACTION_HINT,ICON_ as icon,STYLE_ as style,IS_EXPANDED,"
                    + "IS_INST_RESOURCE,RESOURCE_LOCATION FROM " + WKFL_CONF_RESOURCE_TABLE
                    + " where PROC_DEF_KEY=:procDefKey and IS_INST_RESOURCE='Y' order by SORT_CODE",
                MapKit.mapOf("procDefKey", definitionKey));
    }

    /**
     * 根据当前流程节点与流程资源之间的关联关系查找该节点流程资源的具体数据
     */
    private List<WorkflowResource> filterTaskResourcesByFormProperties(
        List<WorkflowResource> resources, List<FormProperty> formProperties) {
        ValidateKit.notNull(resources, "该流程下的流程资源为空");
        ValidateKit.notNull(formProperties, "该流程表单列表为空");
        List<WorkflowResource> resourceList = new ArrayList<>();
        if (formProperties.isEmpty()) {
            logger.warn("传入的activiti流程表单列表为空");
        }
        for (FormProperty formProperty : formProperties) {
            for (WorkflowResource resource : resources) {
                if (formProperty.getId().equals(resource.getId())) {
                    if (!formProperty.isWritable()) {
                        resource.setRight(WorkflowResourceRight.Readonly);
                    }
                    if (!formProperty.isRequired()){
                        resource.setIsExpanded(YesOrNo.N.toString());
                    }else {
                        resource.setIsExpanded(YesOrNo.Y.toString());
                    }
                    if (resource.getId().equals("SRSignComment")||resource.getId().equals("SRSignApplyComment")){
                        resource.setIsExpanded(YesOrNo.Y.toString());
                    }
                    resourceList.add(resource);
                }
            }
        }
        // 按sortCode排序
        Collections.sort(resourceList, (lhs, rhs) -> {
            if(null==lhs.getSortCode()||"".equals(lhs.getSortCode())){
                return 1;
            }else if (null==rhs.getSortCode()||"".equals(rhs.getSortCode())){
                return -1;
            } else {
                return lhs.getSortCode().compareTo(rhs.getSortCode());
            }
        });

        return resourceList;
    }

    private List<WorkflowResource> getWorkflowResources(String definitionKey) {
        ValidateKit.notNull(definitionKey, "传入的参数definitionKey为空!");
        return beanCruder
            .selectList(WorkflowResource.class,
                "SELECT RESOURCE_ID as id,NAME,PROC_DEF_KEY,TYPE_ as type,SORT_CODE,ACTION as "
                    + "action,ACTION_HINT,ICON_ as icon,STYLE_ as style,IS_EXPANDED,"
                    + "IS_INST_RESOURCE,RESOURCE_LOCATION FROM " + WKFL_CONF_RESOURCE_TABLE
                    + " where PROC_DEF_KEY=:procDefKey order by SORT_CODE",
                MapKit.mapOf("procDefKey", definitionKey));
    }
}
