package com.vekai.crops.codetodo.handler;

import cn.fisok.raw.lang.MapObject;
import com.vekai.crops.codetodo.businesstype.entity.MsbBusinessType;
import com.vekai.dataform.handler.impl.MapDataOneHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.lang.BizException;
import cn.fisok.sqloy.core.BeanCruder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BusinessTypeHandler extends MapDataOneHandler {

    @Autowired
    private BeanCruder beanCruder;

    @Override
    public int insert(DataForm dataForm, MapObject object) {

        String businessTypeName = object.getValue("businessTypeName").strValue();

        String sql = "select * from MSB_BUSINESS_TYPE where BUSINESS_TYPE_NAME = :businessTypeName";
        MsbBusinessType businessType = beanCruder.selectOne(MsbBusinessType.class, sql, "businessTypeName", businessTypeName);

        if (businessType != null){
            throw new BizException("当前业务类型名称重复，无法新增");
        }else {
            return super.save(dataForm, object);
        }

    }
}
