package com.vekai.crops.business.mapper;

import cn.fisok.sqloy.core.BeanCruder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;


@Repository
public class BusinessMapper {

    @Autowired
    private BeanCruder beanCruder;

    public Integer whiteEntry(String applyId){
        String sql = "update BUSINESS_APPLY set ENTER_STATUS = '1' where ID = :applyId";
        Map<String,Object> params = new HashMap<>();
        params.put("applyId",applyId);

        int count = beanCruder.execute(sql,params);

        return count;
    }

}
