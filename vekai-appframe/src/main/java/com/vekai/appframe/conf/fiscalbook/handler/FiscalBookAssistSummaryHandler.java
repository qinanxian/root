package com.vekai.appframe.conf.fiscalbook.handler;

import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.dataform.handler.impl.MapDataListHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.MapKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by luyu on 2018/6/12.
 */
@Component
public class FiscalBookAssistSummaryHandler extends MapDataListHandler {

    @Autowired
    BeanCruder beanCruder;

    @Override
    @Transactional
    public int save(DataForm dataForm, List<MapObject> dataList) {
        if (dataList.size() == 0)
            return 1;
        String bookEntryId = dataList.get(0).getValue("bookEntryId").strValue();
        String sql = "DELETE FROM FISC_BOOK_ASSIST WHERE BOOK_ENTRY_ID =:bookEntryId";
        beanCruder.execute(sql, MapKit.mapOf("bookEntryId",bookEntryId));
        return super.save(dataForm,dataList);
    }
}
