package com.vekai.crops.othApplications.qcfq.handler;

import cn.fisok.raw.lang.BizException;
import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.crops.othApplications.qcfq.entity.Apply;
import com.vekai.dataform.handler.impl.MapDataOneHandler;
import com.vekai.dataform.model.DataForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApplyHandler extends MapDataOneHandler {
    @Autowired
    private BeanCruder beanCruder;

    @Override
    public int save(DataForm dataForm, MapObject object) {
        String productName = object.getValue("productName").strValue();
        String sql="select * from QCFQ_APPLY_PRODUCT where PRODUCT_NAME = :productName";
        Apply app=beanCruder.selectOne(Apply.class,sql,"productName",productName);
        if(app!=null){
            throw new BizException("当前产品名称重复，无法新增");
        }else {
            return super.save(dataForm, object);
        }
    }

    @Override
    public int insert(DataForm dataForm, MapObject object) {
        return super.insert(dataForm, object);
    }
}
