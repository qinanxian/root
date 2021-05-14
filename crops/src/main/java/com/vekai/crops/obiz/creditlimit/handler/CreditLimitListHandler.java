package com.vekai.crops.obiz.creditlimit.handler;

import cn.fisok.raw.lang.MapObject;
import com.vekai.base.dict.model.DictEntry;
import com.vekai.base.dict.model.DictItemEntry;
import com.vekai.base.dict.service.DictService;
import com.vekai.dataform.handler.impl.MapDataListHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.kit.ValidateKit;
import cn.fisok.raw.lang.BizException;
import cn.fisok.sqloy.core.MapObjectCruder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class CreditLimitListHandler extends MapDataListHandler {
    @Autowired
    private MapObjectCruder mapObjectCruder;
    @Autowired
    private DictService dictService;


    @Override
    public Integer delete(DataForm dataForm, List<MapObject> dataList) {
        for(MapObject row : dataList){
            String limitId = row.getValue("limitId").strValue();
            String limitStatus = row.getValue("limitStatus").strValue();
            String limitStatusName = limitStatus;

            //对状态的标签值
            DictEntry dictEntry = dictService.getDict("CreditLimitStatus");
            if(dictEntry!=null){
                DictItemEntry dictItemEntry = dictEntry.getItem(limitStatus);
                if(dictItemEntry!=null){
                    limitStatusName = dictItemEntry.getName();
                }
            }

            if(!"DRAFT".equals(limitStatus)){
                throw new BizException("额度{0}的状态为{1}，不是草稿状态，不能删除",limitId,limitStatusName);
            }
        }
        return super.delete(dataForm, dataList);
    }

    private int updateCreditLimit(String limitId, String limitStatus,boolean copyAmt){
        ValidateKit.notNull(limitId,"额度ID不能为空:{0}",limitId);
        ValidateKit.notNull(limitStatus,"额度状态不能为空:{0}",limitStatus);

        Map<String,String> param = MapKit.mapOf("limitId",limitId,"limitStatus",limitStatus);
        String sql = "UPDATE OBIZ_CREDIT_LIMIT SET LIMIT_STATUS=:limitStatus WHERE LIMIT_ID=:limitId";
        if(copyAmt){
            sql = "UPDATE OBIZ_CREDIT_LIMIT SET LIMIT_STATUS=:limitStatus,AVAILABLE_AMT=LIMIT_SUM WHERE LIMIT_ID=:limitId";
        }
        return mapObjectCruder.execute(sql,param);
    }

    public int startAction(DataForm dataForm, MapObject row){
        String limitId = row.getValue("limitId").strValue();
        String limitStatus = row.getValue("limitStatus").strValue();
        String beginDate = row.getValue("beginDate").strValue();
        String expiryDate = row.getValue("expiryDate").strValue();
        ValidateKit.notNull(limitId,"额度ID不能为空:{0}",limitId);
        ValidateKit.notNull(limitStatus,"额度状态不能为空:{0}",limitStatus);
        ValidateKit.notNull(beginDate,"额度[起始日]不能为空",limitStatus);
        ValidateKit.notNull(expiryDate,"额度[到期日]不能为空",limitStatus);

        if("DRAFT".equals(limitStatus)){
            return updateCreditLimit(limitId,"NORMAL",true);
        }
        return 0;
    }


    public int toggleFrozeAction(DataForm dataForm, MapObject row){
        String limitId = row.getValue("limitId").strValue();
        String limitStatus = row.getValue("limitStatus").strValue();
        ValidateKit.notNull(limitId,"额度ID不能为空:{0}",limitId);
        ValidateKit.notNull(limitStatus,"额度状态不能为空:{0}",limitStatus);

        //额度只能在交结解冻两个状态之间切换
        String toggleStatus = limitStatus;
        if("NORMAL".equals(limitStatus)){
            toggleStatus = "FORZEN";
        }else if("FORZEN".equals(limitStatus)){
            toggleStatus = "NORMAL";
        }
        if(!toggleStatus.equals(limitStatus)){
            return updateCreditLimit(limitId,toggleStatus,false);
        }else{
            return 0;
        }

    }
}
