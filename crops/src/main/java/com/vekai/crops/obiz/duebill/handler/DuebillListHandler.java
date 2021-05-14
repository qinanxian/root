package com.vekai.crops.obiz.duebill.handler;

import cn.fisok.raw.lang.MapObject;
import com.vekai.crops.obiz.duebill.entity.ObizDuebill;
import com.vekai.crops.obiz.duebill.model.DuebillStatus;
import com.vekai.crops.obiz.duebill.service.DuebillService;
import com.vekai.appframe.base.aspect.DataFormControllerUserNameAspect;
import com.vekai.dataform.handler.impl.BeanDataListHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.ValidateKit;
import cn.fisok.raw.lang.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DuebillListHandler extends BeanDataListHandler<ObizDuebill> {
    @Override
    public void initDataForm(DataForm dataForm) {
        super.initDataForm(dataForm);
        dataForm.getProperties().put(DataFormControllerUserNameAspect.USER_FIELDS, "operator");
    }

    @Autowired
    DuebillService duebillService;

    public ObizDuebill makeLoanCharge(DataForm dataForm, MapObject params){
        String duebillId = params.getValue("duebillId").strValue();
        ValidateKit.notBlank(duebillId,"参数，借据号({0})不能为空",duebillId);
        return duebillService.makeLoanCharge(duebillId);
    }

    public ObizDuebill reverseLoanCharge(DataForm dataForm, MapObject params){
        String duebillId = params.getValue("duebillId").strValue();
        ValidateKit.notBlank(duebillId,"参数，借据号({0})不能为空",duebillId);
        return duebillService.reverseLoanCharge(duebillId);
    }

    @Override
    public Integer delete(DataForm dataForm, List<ObizDuebill> dataList) {
        dataList.forEach(duebill -> {
            if(!DuebillStatus.READY.name().equals(duebill.getDuebillStatus())){
                throw new BizException("借据{0}已经放款，不得删除",duebill.getDuebillId());
            }
        });
        return super.delete(dataForm, dataList);
    }
}
