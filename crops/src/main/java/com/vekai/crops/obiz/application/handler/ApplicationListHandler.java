package com.vekai.crops.obiz.application.handler;

import cn.fisok.raw.lang.MapObject;
import com.vekai.appframe.base.aspect.DataFormControllerUserNameAspect;
import com.vekai.dataform.handler.impl.MapDataListHandler;
import com.vekai.dataform.model.DataForm;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApplicationListHandler extends MapDataListHandler {
    @Override
    public void initDataForm(DataForm dataForm) {
        super.initDataForm(dataForm);
        dataForm.getProperties().put(DataFormControllerUserNameAspect.USER_FIELDS, "operator");
    }

    @Override
    public Integer delete(DataForm dataForm, List<MapObject> dataList) {
        return super.delete(dataForm, dataList);
    }

    /**
     * 提交一笔申请进入流程
     * @param dataForm
     * @param applicationId
     * @return
     */
    public String submitApplyToProcess(DataForm dataForm,String applicationId){
        return "";
    }
}
