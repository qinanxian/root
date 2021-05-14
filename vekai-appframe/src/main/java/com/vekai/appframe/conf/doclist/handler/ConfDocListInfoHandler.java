package com.vekai.appframe.conf.doclist.handler;

import com.vekai.appframe.conf.doclist.entity.ConfDocListPO;
import com.vekai.appframe.constant.AppframeConst;
import com.vekai.dataform.handler.impl.MapDataOneHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.holder.MessageHolder;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.lang.BizException;
import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.BeanCruder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ConfDocListInfoHandler extends MapDataOneHandler {

    @Autowired
    BeanCruder accessor;

    @Override
    @Transactional
    public int save(DataForm dataForm, MapObject object) {
        String operation = object.getValue("operation").strValue();
        if (StringKit.equals(AppframeConst.OPERATION_ADD, operation)) {
            String doclistCode = object.getValue("doclistCode").strValue();
            ConfDocListPO confDocListPO = accessor.selectOneById(ConfDocListPO.class, doclistCode);
            if (null != confDocListPO) {
                throw new BizException(MessageHolder.getMessage("", "conf.doclist.already.exist"));
            }
        }
        return super.save(dataForm, object);
    }
}
