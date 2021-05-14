package com.vekai.showcase.handler;

import cn.fisok.raw.kit.DateKit;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.lang.ValueObject;
import cn.fisok.sqloy.core.PaginResult;
import com.vekai.dataform.handler.impl.BeanDataListHandler;
import com.vekai.dataform.model.DataForm;
import com.vekai.showcase.entity.DemoPerson;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class CustomizeFilterBeanPersonListHandler extends BeanDataListHandler<DemoPerson> {
    public PaginResult<DemoPerson> query(DataForm dataForm, Map<String, ?> queryParameters, Map<String, ?> filterParameters, Map<String, ?> sortMap, int pageSize, int pageIndex) {
        ValueObject filterAge = getFilterValue(dataForm,filterParameters,"age");
        //根据传入的年龄值，重新计算生日区间
        StringBuffer where = new StringBuffer(StringKit.nvl(dataForm.getQuery().getWhere(),""));
        if(where.length()>0){
            where.append(" AND");
        }
        if(filterAge.isArray() && filterAge.intArray().length==2){
            where.append(" BIRTH BETWEEN :startBirth AND :endBirth");
            dataForm.getQuery().setWhere(where.toString());
            Date date = DateKit.now();
            Date startDate = DateKit.minusYears(date,filterAge.intArray()[1]);  //第二个值是年龄大的，日期在前面
            Date endDate = DateKit.minusYears(date,filterAge.intArray()[0]);    //第一个值是年龄小的，日期在后面
            MapKit.merge(queryParameters,MapKit.mapOf("startBirth",startDate,"endBirth",endDate));
        }

        return super.query(dataForm, queryParameters, filterParameters, sortMap, pageSize, pageIndex);
    }

}
