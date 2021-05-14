package com.vekai.appframe.cmon.notice.handler;

import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.appframe.constant.AppframeConst;
import com.vekai.dataform.handler.impl.MapDataListHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.MapKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by luyu on 2018/9/14.
 */
@Component
public class PublicNoticeListHandler extends MapDataListHandler {

    @Autowired
    BeanCruder beanCruder;

    @Transactional
    public Integer updatePublicNoticeStatus(DataForm dataForm, MapObject object) {
        String publicNoticeId = object.getValue("publicNoticeId").strValue();
        String sql = "UPDATE CMON_PUBLIC_NOTICE SET STATUS =:status WHERE PUBLIC_NOTICE_ID=:publicNoticeId";
        return beanCruder.execute(sql, MapKit.mapOf("status",AppframeConst.EFFECT_STATUS_INVALID,"publicNoticeId",publicNoticeId));
    }
}
