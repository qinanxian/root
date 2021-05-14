package com.vekai.appframe.base.param.handler;

import com.vekai.base.param.service.impl.po.ParamPO;
import com.vekai.dataform.handler.impl.BeanDataListHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.sqloy.core.BeanCruder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ParamListHandler extends BeanDataListHandler<ParamPO> {

    @Autowired
    BeanCruder accessor;

    public Integer delete(DataForm dataForm, List<ParamPO> dataList) {
        Integer result = super.delete(dataForm, dataList);
        if (result > 0) {
            for (ParamPO param : dataList) {
                String paramCode = param.getCode();
                result = result + accessor.execute("DELETE FROM FOWK_PARAM_ITEM WHERE PARAM_CODE=:paramCode", MapKit
                        .mapOf("paramCode", paramCode));
            }
        }
        return result;
    }
}
