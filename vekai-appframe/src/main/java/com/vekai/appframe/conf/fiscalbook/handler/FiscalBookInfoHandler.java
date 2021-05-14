package com.vekai.appframe.conf.fiscalbook.handler;

import cn.fisok.raw.lang.MapObject;
import com.vekai.dataform.handler.impl.MapDataOneHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.lang.ValueObject;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by luyu on 2018/6/5.
 */
@Component
public class FiscalBookInfoHandler extends MapDataOneHandler{
    public static final String FISC_BOOK_ENTRY_PO = "FISC_BOOK_ENTRY_PO";

    @Override
    public MapObject query(DataForm dataForm, Map<String, ?> queryParameters) {
        String bookCode = ValueObject.valueOf(queryParameters.get("bookCode")).strValue();
        MapObject mapObject = super.query(dataForm,queryParameters);
        if (!StringKit.isBlank(bookCode)) {
            dataForm.getElements().forEach(dataFormElement -> {
                if ("bookCode".equals(dataFormElement.getCode())) {
                    dataFormElement.getElementUIHint().setReadonly(Boolean.TRUE);
                }
            });
        }
        return mapObject;
    }

}
