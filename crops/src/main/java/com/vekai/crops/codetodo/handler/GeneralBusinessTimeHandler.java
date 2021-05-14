package com.vekai.crops.codetodo.handler;

import cn.fisok.raw.lang.BizException;
import cn.fisok.raw.lang.MapObject;
import cn.fisok.raw.lang.ValueObject;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.crops.codetodo.entity.MsbBusinessTime;
import com.vekai.crops.util.CommonUtil;
import com.vekai.dataform.handler.impl.MapDataOneHandler;
import com.vekai.dataform.model.DataForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class GeneralBusinessTimeHandler extends MapDataOneHandler {

    @Autowired
    private BeanCruder beanCruder;

    /**
     * 避免插入冲突的数据
     *
     * @param dataForm
     * @param object
     * @return
     */
    @Override
    public int save(DataForm dataForm, MapObject object) {


        //网点id
        String networkNo = object.getValue("networkNo").strValue();
        //业务类型
        String businessType = object.getValue("businessType").strValue();
        //时间段
        String workTime = object.getValue("workTime").strValue();
        String startTime = object.getValue("startTime").strValue();
        String endTime = object.getValue("endTime").strValue();

        String sql = "select * from MSB_BUSINESS_TIME where NETWORK_NO=:networkNo and BUSINESS_TYPE=:businessType and WORK_TIME=:workTime";

        MsbBusinessTime businessTime = beanCruder.selectOne(MsbBusinessTime.class, sql, "networkNo", networkNo, "businessType", businessType, "workTime", workTime);
        if (startTime == null && endTime == null) {
            if (businessTime == null) {
                return super.save(dataForm, object);
            } else {
                if (businessTime.getWorkTime().equals(workTime) && businessTime.getBusinessType().equals(businessType) && businessTime.getNetworkNo().equals(networkNo)) {
                    return super.update(dataForm, object);

                } else {
                    throw new BizException("该配置已存在,新增失败");
                }
            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
        if (CommonUtil.ifIsNotEmpty(startTime)) {
            String[] workMorningHeightArray = startTime.split(",");
            Date workMorningHeightStart = new ValueObject(workMorningHeightArray[0]).dateValue();
            Date workMorningHeightEnd = new ValueObject(workMorningHeightArray[1]).dateValue();
            if (Integer.valueOf(sdf.format(workMorningHeightStart).substring(0, 2)) > 12
                    || Integer.valueOf(sdf.format(workMorningHeightEnd).substring(0, 2)) > 12) {
                throw new BizException("上午工作营业时间有误");
            }
        }

        if (CommonUtil.ifIsNotEmpty(endTime)) {
            String[] workAfternoonHeightArray = endTime.split(",");
            Date workAfternoonHeightStart = new ValueObject(workAfternoonHeightArray[0]).dateValue();
            Date workAfternoonHeightEnd = new ValueObject(workAfternoonHeightArray[1]).dateValue();
            if (Integer.valueOf(sdf.format(workAfternoonHeightStart).substring(0, 2)) < 12
                    || Integer.valueOf(sdf.format(workAfternoonHeightEnd).substring(0, 2)) < 12) {
                throw new BizException("下午工作营业时间有误");
            }
        }


        int result = startTime.compareTo(endTime);
        //如果businessTime为空，说明没有冲突数据，
        if (businessTime == null) {

            if (result < 0) {
                return super.save(dataForm, object);
            } else {
                throw new BizException("开始时间应小于结束时间");
            }

        } else {
            if (businessTime.getWorkTime().equals(workTime) && businessTime.getBusinessType().equals(businessType) && businessTime.getNetworkNo().equals(networkNo)) {
                if (result < 0) {
                    return super.update(dataForm, object);
                } else {
                    throw new BizException("开始时间应小于结束时间");
                }

            } else {
                throw new BizException("该配置已存在,新增失败");
            }
        }
    }
}
