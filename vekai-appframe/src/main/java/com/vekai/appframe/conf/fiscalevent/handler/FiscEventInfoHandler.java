package com.vekai.appframe.conf.fiscalevent.handler;

import cn.fisok.raw.lang.MapObject;
import com.vekai.dataform.handler.impl.MapDataOneHandler;
import com.vekai.dataform.model.DataForm;
import com.vekai.dataform.model.DataFormElement;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.lang.ValueObject;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by luyu on 2018/6/14.
 */
@Component
public class FiscEventInfoHandler extends MapDataOneHandler {

    @Override
    public MapObject query(DataForm dataForm, Map<String, ?> queryParameters) {
        String eventDef = ValueObject.valueOf(queryParameters.get("eventDef")).strValue();
        if (!StringKit.isBlank(eventDef) && !"null".equals(eventDef)) {
            dataForm.getElements().forEach(dataFormElement -> {
                if ("eventDef".equals(dataFormElement.getCode())) {
                    DataFormElement.FormElementUIHint formElementUIHint = dataFormElement.getElementUIHint();
                    formElementUIHint.setReadonly(Boolean.TRUE);
                    dataFormElement.setElementUIHint(formElementUIHint);
                }
            });
        }
        return super.query(dataForm,queryParameters);
    }
}
