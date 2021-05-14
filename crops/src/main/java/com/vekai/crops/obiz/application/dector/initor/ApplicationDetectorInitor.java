package com.vekai.crops.obiz.application.dector.initor;

import com.vekai.crops.obiz.application.entity.ObizApplication;
import com.vekai.crops.obiz.application.service.ApplicationService;
import com.vekai.base.detector.DetectorContext;
import com.vekai.base.detector.DetectorContextInitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 业务申请默认检查器
 */
@Component
public class ApplicationDetectorInitor extends DetectorContextInitor {
    @Autowired
    ApplicationService applicationService;

    @Override
    public void init(DetectorContext context) {
        String applicationId = context.getParam("applicationId").strValue();
        ObizApplication obizApplication = applicationService.queryApplication(applicationId);

        this.setContextParam(context,"application",obizApplication);
    }
}
