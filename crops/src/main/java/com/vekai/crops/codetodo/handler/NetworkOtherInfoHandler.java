package com.vekai.crops.codetodo.handler;

import cn.fisok.raw.lang.MapObject;
import com.vekai.crops.util.CommonUtil;
import com.vekai.dataform.handler.impl.MapDataOneHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.lang.BizException;
import cn.fisok.raw.lang.ValueObject;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class NetworkOtherInfoHandler extends MapDataOneHandler {

    @Override
    public int save(DataForm dataForm, MapObject object) {

        String workMorningHeightTime = object.getValue("workMorningHeightTime").strValue();
        String workAfternoonHeightTime = object.getValue("workAfternoonHeightTime").strValue();
        String holidayMorningHeightTime = object.getValue("holidayMorningHeightTime").strValue();
        String holidayAfternoonHeightTime = object.getValue("holidayAfternoonHeightTime").strValue();
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        if(CommonUtil.ifIsNotEmpty(workMorningHeightTime)){
            String[] workMorningHeightArray = workMorningHeightTime.split(",");
            Date workMorningHeightStart = new ValueObject(workMorningHeightArray[0]).dateValue();
            Date workMorningHeightEnd = new ValueObject(workMorningHeightArray[1]).dateValue();
            if (Integer.valueOf(sdf.format(workMorningHeightStart).substring(0,2))>12
                    || Integer.valueOf(sdf.format(workMorningHeightEnd).substring(0,2))>12 ){
                throw new BizException("工作日上午高峰时间段有误");
            }
        }

        if(CommonUtil.ifIsNotEmpty(workAfternoonHeightTime)){
            String[] workAfternoonHeightArray = workAfternoonHeightTime.split(",");
            Date workAfternoonHeightStart = new ValueObject(workAfternoonHeightArray[0]).dateValue();
            Date workAfternoonHeightEnd = new ValueObject(workAfternoonHeightArray[1]).dateValue();
            if (Integer.valueOf(sdf.format(workAfternoonHeightStart).substring(0,2))<12
                    || Integer.valueOf(sdf.format(workAfternoonHeightEnd).substring(0,2))<12){
                throw new BizException("工作日下午高峰时间段有误");
            }
        }

        if(CommonUtil.ifIsNotEmpty(holidayMorningHeightTime)){
            String[] holidayMorningHeightArray = holidayMorningHeightTime.split(",");
            Date holidayMorningHeightStart = new ValueObject(holidayMorningHeightArray[0]).dateValue();
            Date holidayMorningHeightEnd = new ValueObject(holidayMorningHeightArray[1]).dateValue();
            if (Integer.valueOf(sdf.format(holidayMorningHeightStart).substring(0,2))>12
                    || Integer.valueOf(sdf.format(holidayMorningHeightEnd).substring(0,2))>12 ){
                throw new BizException("节假日上午高峰时间段有误");
            }
        }

        if(CommonUtil.ifIsNotEmpty(holidayAfternoonHeightTime)){
            String[] holidayAfternoonHeightArray = holidayAfternoonHeightTime.split(",");
            Date holidayAfternoonHeightStart = new ValueObject(holidayAfternoonHeightArray[0]).dateValue();
            Date holidayAfternoonHeightEnd = new ValueObject(holidayAfternoonHeightArray[1]).dateValue();
            if (Integer.valueOf(sdf.format(holidayAfternoonHeightStart).substring(0,2))<12
                    || Integer.valueOf(sdf.format(holidayAfternoonHeightEnd).substring(0,2))<12){
                throw new BizException("节假日下午高峰时间段有误");
            }
        }

        return super.save(dataForm, object);
    }
}
