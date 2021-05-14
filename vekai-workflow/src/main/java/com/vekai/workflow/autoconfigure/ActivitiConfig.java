package com.vekai.workflow.autoconfigure;

import com.vekai.workflow.nopub.resource.form.ButtonFormType;
import com.vekai.workflow.nopub.resource.form.FieldsetFormType;
import com.vekai.workflow.nopub.resource.form.LinkFormType;
import org.activiti.engine.form.AbstractFormType;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.ProcessEngineConfigurationConfigurer;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ActivitiConfig implements ProcessEngineConfigurationConfigurer {

    public void processEngineConfigurationInitialized( SpringProcessEngineConfiguration springProcessEngineConfiguration) {

    }

    public void configure(SpringProcessEngineConfiguration processEngineConfiguration) {

        /**
         * 新增自定义的表单类型(流程资源)
         */
        List<AbstractFormType> customFormTypes =
             Arrays.asList(new ButtonFormType(), new FieldsetFormType(), new LinkFormType());
        processEngineConfiguration.setCustomFormTypes(customFormTypes);


        /**
         * 项目启动不检测Activiti数据表
         */
        processEngineConfiguration.setDatabaseSchemaUpdate("non");

        /**
         * 解决流程图中文乱码问题
         */
        processEngineConfiguration.setActivityFontName("宋体");
        processEngineConfiguration.setLabelFontName("宋体");
        processEngineConfiguration.setAnnotationFontName("宋体");

    }
}