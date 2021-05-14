package com.vekai.crops.insure.handler;

import cn.fisok.raw.lang.BizException;
import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.dataform.handler.impl.MapDataOneHandler;
import com.vekai.dataform.model.DataForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderManagerHandler extends MapDataOneHandler{

    @Autowired
    private BeanCruder beanCruder;

    /**
     * 新增的保单管理员工号是否存在，如果已存在提示
     *
     * @param dataForm
     * @param object
     * @return
     */
    @Override
    public int save(DataForm dataForm, MapObject object) {

        String manOa="",managerNo="";
        //员工工号
        manOa = object.getValue("manOa").strValue();
        String sql = "SELECT MAN_OA FROM BX_MANAGER_INFO where MAN_OA =:manOa";
        managerNo = beanCruder.selectOne(String.class, sql, "manOa", manOa);

        if (isNotEmpty(managerNo)){
            throw new BizException("保单管理员OA重复，无法新增");
        }else {
            return super.save(dataForm, object);
        }
    }
    private boolean isNotEmpty(String str) {
        boolean result = true ;
        if((str==null) || (str.equals(""))){
            result = false ;
        }
        return  result;
    }
}
