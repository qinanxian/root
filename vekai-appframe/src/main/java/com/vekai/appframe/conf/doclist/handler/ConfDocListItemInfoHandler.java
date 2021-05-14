package com.vekai.appframe.conf.doclist.handler;

import cn.fisok.raw.lang.MapObject;
import com.vekai.appframe.conf.doclist.entity.ConfDocListItemPO;
import com.vekai.appframe.constant.AppframeConst;
import com.vekai.dataform.handler.impl.MapDataOneHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.holder.MessageHolder;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.lang.BizException;
import cn.fisok.sqloy.core.BeanCruder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ConfDocListItemInfoHandler extends MapDataOneHandler {

    @Autowired
    BeanCruder accessor;

    @Override
    @Transactional
    public int save(DataForm dataForm, MapObject object) {
        String operation = object.getValue("operation").strValue();
        if (StringKit.equals(AppframeConst.OPERATION_ADD, operation)) {
            String doclistCode = object.getValue("doclistCode").strValue();
            String itemCode = object.getValue("itemCode").strValue();
            ConfDocListItemPO confDocListItemPO = accessor.selectOneById(ConfDocListItemPO.class, doclistCode, itemCode);
            if (null != confDocListItemPO) {
                throw new BizException(MessageHolder.getMessage("", "conf.doclist.item.already.exist"));
            }
        }
        return super.save(dataForm, object);
    }
}
