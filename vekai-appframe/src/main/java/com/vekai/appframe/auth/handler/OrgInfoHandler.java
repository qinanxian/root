package com.vekai.appframe.auth.handler;

import com.vekai.auth.entity.Org;
import com.vekai.dataform.handler.impl.BeanDataOneHandler;
import com.vekai.dataform.model.DataForm;
import com.vekai.dataform.model.DataFormElement;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class OrgInfoHandler extends BeanDataOneHandler<Org> {

    public Org query(DataForm dataForm, Map<String, ?> queryParameters) {
        Org org = super.query(dataForm, queryParameters);
        if(org != null){
            Optional.ofNullable(dataForm.getElement("id"))
                    .orElseGet(DataFormElement::new)
                    .getElementUIHint().setReadonly(true);
        }
        return org;
    }
}
