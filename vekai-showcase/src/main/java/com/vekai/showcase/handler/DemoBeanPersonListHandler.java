package com.vekai.showcase.handler;

import cn.fisok.raw.kit.DateKit;
import cn.fisok.raw.lang.MapObject;
import cn.fisok.raw.lang.ValueObject;
import cn.fisok.sqloy.core.PaginResult;
import com.vekai.appframe.base.aspect.DataFormControllerUserNameAspect;
import com.vekai.dataform.handler.impl.BeanDataListHandler;
import com.vekai.dataform.model.DataForm;
import com.vekai.dataform.model.DataFormElement;
import com.vekai.dataform.model.types.ElementDataDictCodeMode;
import com.vekai.dataform.model.types.ElementDataEditStyle;
import com.vekai.showcase.entity.DemoPerson;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DemoBeanPersonListHandler extends BeanDataListHandler<DemoPerson> {

    @Override
    public void initDataForm(DataForm dataForm) {
        if (null!=dataForm.getElement("sponsor")){
            dataForm.getProperties().put(DataFormControllerUserNameAspect.USER_FIELDS, "sponsor");
        }
        if (null!=dataForm.getElement("assignee")){
            dataForm.getProperties().put(DataFormControllerUserNameAspect.USER_FIELDS, "assignee");
        }
        super.initDataForm(dataForm);
    }

    public DemoPerson createNewPerson(DataForm dataForm, MapObject param){
        DemoPerson person = new DemoPerson();

        person.setCode(param.getValue("code").strValue());
        person.setChnName(param.getValue("name").strValue()+":后台程序设置的时间:"+DateKit.format(DateKit.now()));

        return person;
    }

    public PaginResult<DemoPerson> query(DataForm dataForm, Map<String, ?> queryParameters, Map<String, ?> filterParameters, Map<String, ?> sortMap, int pageSize, int pageIndex) {
        ValueObject status = getFilterValue(dataForm,filterParameters,"status");
        System.out.println("--------->"+status);
        //2.修改下拉框
        DataFormElement element = dataForm.getElement("companyIndustry");
        if(element!=null){
            element.getElementUIHint().setEditStyle(ElementDataEditStyle.DictCodePicker)
                    .setDictCodeMode(ElementDataDictCodeMode.SQLQuery)
                    .setDictCodeExpr("SELECT CODE as CODE,NAME as NAME FROM FOWK_DICT_ITEM WHERE DICT_CODE='Currency'");
//                    .setDictCodeExpr("SELECT CODE as CODE,NAME as NAME FROM FOWK_DICT_ITEM WHERE DICT_CODE='Currency' and CREATED_BY=:CUR_USER");
        }

        return super.query(dataForm, queryParameters, filterParameters, sortMap, pageSize, pageIndex);
    }

}
