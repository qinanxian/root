package com.vekai.crops.obiz.application.handler;

import com.vekai.crops.constant.BizConst;
import com.vekai.crops.obiz.application.entity.ObizApplication;
import com.vekai.appframe.base.aspect.DataFormControllerUserNameAspect;
import com.vekai.common.landmark.service.LandmarkService;
import com.vekai.dataform.handler.impl.BeanDataOneHandler;
import com.vekai.dataform.model.DataForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApplicationInfoHandler extends BeanDataOneHandler<ObizApplication> {
    @Autowired
    LandmarkService landmarkService;

    @Override
    public void initDataForm(DataForm dataForm) {
        super.initDataForm(dataForm);
        super.initDataForm(dataForm);
        dataForm.getProperties().put(DataFormControllerUserNameAspect.USER_FIELDS, "operator");
    }

    @Override
    public int save(DataForm dataForm, ObizApplication object) {
        /**
         * 里程碑信息设置-申请录入
         * 当流程发起流程时-申请完善
         */
        String itemKey = "App1010";
        object.setAppMilestone(itemKey);
        String applicationId = object.getApplicationId();
        if(applicationId!=null&&!"".equals(applicationId)) landmarkService.activeSetp(applicationId, BizConst.APPLICATION, itemKey);

        return super.save(dataForm, object);
    }
}
