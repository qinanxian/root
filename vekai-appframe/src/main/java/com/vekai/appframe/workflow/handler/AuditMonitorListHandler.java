package com.vekai.appframe.workflow.handler;

import cn.fisok.raw.lang.MapObject;
import com.vekai.dataform.handler.impl.MapDataListHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.DateKit;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.lang.ValueObject;
import cn.fisok.sqloy.core.BeanCruder;
import cn.fisok.sqloy.core.PaginResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class AuditMonitorListHandler extends MapDataListHandler {

    @Autowired
    private BeanCruder beanCruder;


    @Override
    public PaginResult<MapObject> query(DataForm dataForm, Map<String, ?> queryParameters, Map<String, ?> filterParameters, Map<String, ?> sortMap, int pageSize, int pageIndex) {
        ValueObject filterDate = getFilterValue(dataForm,filterParameters,"updatedTime");
        StringBuffer where = new StringBuffer(StringKit.nvl(dataForm.getQuery().getWhere(),""));
        if(where.length()>0){
            where.append(" AND");
        }
        if(filterDate.isArray() && filterDate.strArray().length==2){
            where.append(" CREATED_TIME BETWEEN :startTime AND :endTime");
            dataForm.getQuery().setWhere(where.toString());

            Date startDate = DateKit.parse(filterDate.strArray()[0]); //第一个值日期小，在前面
            Date endDate = DateKit.parse(filterDate.strArray()[1]+" 23:59:59");   //第二个值日期大，在后面

            MapKit.merge(queryParameters,MapKit.mapOf("startTime",startDate,"endTime",endDate));
        }
        return super.query(dataForm, queryParameters, filterParameters, sortMap, pageSize, pageIndex);
    }
}
