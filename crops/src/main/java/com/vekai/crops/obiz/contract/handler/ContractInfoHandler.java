package com.vekai.crops.obiz.contract.handler;

import com.vekai.crops.obiz.contract.entity.ObizContract;
import com.vekai.crops.obiz.contract.model.ContractStatus;
import com.vekai.appframe.base.aspect.DataFormControllerUserNameAspect;
import com.vekai.dataform.handler.impl.BeanDataOneHandler;
import com.vekai.dataform.model.DataForm;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ContractInfoHandler extends BeanDataOneHandler<ObizContract> {
    @Override
    public void initDataForm(DataForm dataForm) {
        super.initDataForm(dataForm);
        dataForm.getProperties().put(DataFormControllerUserNameAspect.USER_FIELDS, "operator");
    }

    @Override
    public ObizContract query(DataForm dataForm, Map<String, ?> queryParameters) {
        ObizContract contract = super.query(dataForm, queryParameters);
        //非新增状态的合同，不允许编辑
        if(!ContractStatus.READY.name().equals(contract.getContractStatus())){
            dataForm.getElements().forEach(element->{
                element.getElementUIHint().setReading(true);
            });
        }
        return contract;
    }

    @Override
    public int save(DataForm dataForm, ObizContract object) {
        return super.save(dataForm, object);
    }
}
