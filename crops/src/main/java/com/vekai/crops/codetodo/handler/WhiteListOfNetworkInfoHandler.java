package com.vekai.crops.codetodo.handler;

import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.crops.codetodo.entity.MsbBusiQyyyWhiteList;
import com.vekai.dataform.handler.impl.MapDataOneHandler;
import com.vekai.dataform.model.DataForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class WhiteListOfNetworkInfoHandler extends MapDataOneHandler {

    @Autowired
    private BeanCruder beanCruder;

    @Override
    public int save(DataForm dataForm, MapObject object) {
        String white_Id = object.getValue("whiteId").strValue();
        String orgNo = object.getValue("orgNo").strValue();
        String orgName = object.getValue("orgName").strValue();
        String oaNo = object.getValue("oaNo").strValue();
        String oaName = object.getValue("oaName").strValue();
        String mobileNo = object.getValue("mobileNo").strValue();

        String sql = "select * from MSB_BUSI_QYYY_WHITE_LIST where oa_No = :oa_No";
        MsbBusiQyyyWhiteList msbBusiQyyyWhiteList = beanCruder.selectOne(MsbBusiQyyyWhiteList.class, sql, "oa_No", oaNo);

        if (msbBusiQyyyWhiteList == null) {
            String sqls = "select * from MSB_BUSI_QYYY_WHITE_LIST where white_Id = :white_Id";
            MsbBusiQyyyWhiteList msbBusiQyyyWhite = beanCruder.selectOne(MsbBusiQyyyWhiteList.class, sqls, "white_Id", white_Id);
            if(msbBusiQyyyWhite!=null){
                return super.update(dataForm, object);
            }else{
                String s = UUID.randomUUID().toString().replaceAll("-", "");
                object.putValue("whiteId", s);
                return super.save(dataForm, object);
            }
        } else {
            object.putValue("whiteId", msbBusiQyyyWhiteList.getWhiteId());
            return super.update(dataForm, object);
        }

    }
}
