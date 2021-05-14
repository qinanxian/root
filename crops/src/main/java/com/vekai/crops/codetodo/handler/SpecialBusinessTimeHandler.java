package com.vekai.crops.codetodo.handler;

import cn.fisok.raw.lang.BizException;
import cn.fisok.raw.lang.MapObject;
import cn.fisok.raw.lang.ValueObject;
import com.vekai.crops.util.CommonUtil;
import com.vekai.dataform.handler.impl.MapDataOneHandler;
import com.vekai.dataform.model.DataForm;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class SpecialBusinessTimeHandler extends MapDataOneHandler {

    @Override
    public int save(DataForm dataForm, MapObject object) {

        //开始日期
        Date startDate = object.getValue("startDate").dateValue();
        //结束日期
        Date endDate = object.getValue("endDate").dateValue();
        String startTime = object.getValue("startTime").strValue();
        String endTime = object.getValue("endTime").strValue();
        if(startTime==null&&endTime==null){
            return super.save(dataForm, object);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        if(CommonUtil.ifIsNotEmpty(startTime)){
            String[] workMorningHeightArray = startTime.split(",");
            Date workMorningHeightStart = new ValueObject(workMorningHeightArray[0]).dateValue();
            Date workMorningHeightEnd = new ValueObject(workMorningHeightArray[1]).dateValue();
            if (Integer.valueOf(sdf.format(workMorningHeightStart).substring(0,2))>12
                    || Integer.valueOf(sdf.format(workMorningHeightEnd).substring(0,2))>12 ){
                throw new BizException("上午工作营业时间有误");
            }
        }

        if(CommonUtil.ifIsNotEmpty(endTime)){
            String[] workAfternoonHeightArray = endTime.split(",");
            Date workAfternoonHeightStart = new ValueObject(workAfternoonHeightArray[0]).dateValue();
            Date workAfternoonHeightEnd = new ValueObject(workAfternoonHeightArray[1]).dateValue();
            if (Integer.valueOf(sdf.format(workAfternoonHeightStart).substring(0,2))<12
                    || Integer.valueOf(sdf.format(workAfternoonHeightEnd).substring(0,2))<12){
                throw new BizException("下午工作营业时间有误");
            }
        }

        int dateResult = startDate.compareTo(endDate);
        int timeResult = startTime.compareTo(endTime);

        if (dateResult>0 || timeResult>0){
            throw new BizException("请检查时间设置");
        } else {
            return super.save(dataForm, object);
        }

    }
}
