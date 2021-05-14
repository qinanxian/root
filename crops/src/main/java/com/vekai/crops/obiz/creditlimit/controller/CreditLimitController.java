package com.vekai.crops.obiz.creditlimit.controller;

import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.MapObjectCruder;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/creditlimit")
public class CreditLimitController {
    @Autowired
    private MapObjectCruder mapObjectCruder;

    @ApiOperation(value = "取额度的控制参数")
    @RequestMapping("/byId/{limitId}")
    public MapObject getCreditLimitProfile(@PathVariable("limitId") String limitId){
        String sql = "SELECT LIMIT_STATUS,DATAFORM_ID,WORKFLOW_KEY FROM OBIZ_CREDIT_LIMIT WHERE LIMIT_ID=:limitId";
        return mapObjectCruder.selectOne(sql,"limitId",limitId);
    }
}
