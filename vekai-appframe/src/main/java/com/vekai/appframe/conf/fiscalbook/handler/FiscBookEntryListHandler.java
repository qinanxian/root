package com.vekai.appframe.conf.fiscalbook.handler;

import cn.fisok.raw.lang.MapObject;
import com.vekai.dataform.handler.impl.MapDataListHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.sqloy.core.BeanCruder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by luyu on 2018/6/5.
 */
@Component
public class FiscBookEntryListHandler extends MapDataListHandler {

    @Autowired
    BeanCruder beanCruder;

    @Override
    @Transactional
    public Integer delete(DataForm dataForm, List<MapObject> dataList) {
        for (MapObject mapObject : dataList) {
            String bookEntryId = mapObject.getValue("bookEntryId").strValue();
            String sql = "DELETE FROM FISC_BOOK_ASSIST WHERE BOOK_ENTRY_ID=:bookEntryId";
            beanCruder.execute(sql, MapKit.mapOf("bookEntryId",bookEntryId));
        }
        return super.delete(dataForm, dataList);
    }
}
