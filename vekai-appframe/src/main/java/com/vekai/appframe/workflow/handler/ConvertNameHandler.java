package com.vekai.appframe.workflow.handler;

import com.vekai.appframe.base.aspect.DataFormControllerUserNameAspect;
import com.vekai.dataform.handler.impl.MapDataListHandler;
import com.vekai.dataform.model.DataForm;
import org.springframework.stereotype.Component;

@Component
public class ConvertNameHandler extends MapDataListHandler {

    @Override
    public void initDataForm(DataForm dataForm) {
        if (null!=dataForm.getElement("sponsor")){
            dataForm.getProperties().put(DataFormControllerUserNameAspect.USER_FIELDS, "sponsor");
        }
        if (null!=dataForm.getElement("assignee")){
            dataForm.getProperties().put(DataFormControllerUserNameAspect.USER_FIELDS, "assignee");
        }
        super.initDataForm(dataForm);
    }
}
