package com.vekai.crops.insure.handler;

import cn.fisok.raw.lang.BizException;
import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.dataform.handler.impl.MapDataOneHandler;
import com.vekai.dataform.model.DataForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class StaffWhiteHandler extends MapDataOneHandler{

    @Autowired
    private BeanCruder beanCruder;

    /**
     * 新增的员工工号是否存在，如果已存在提示
     *
     * @param dataForm
     * @param object
     * @return
     */
    @Override
    public int save(DataForm dataForm, MapObject object) {

        String oaNo="",staffNo="";
        //员工工号
        oaNo = object.getValue("oaNo").strValue();
        String sql = "SELECT OA_NO FROM BX_STAFF_WHITE_LIST where OA_NO =:oaNo";
        staffNo = beanCruder.selectOne(String.class, sql, "oaNo", oaNo);

        if (isNotEmpty(staffNo)){
            throw new BizException("当前保险职业人员白名单重复，无法新增");
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
