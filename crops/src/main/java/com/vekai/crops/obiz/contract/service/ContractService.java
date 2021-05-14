package com.vekai.crops.obiz.contract.service;

import cn.fisok.raw.lang.MapObject;
import com.vekai.crops.obiz.contract.dao.ContractDao;
import com.vekai.crops.obiz.contract.entity.ObizContract;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.sqloy.core.MapObjectCruder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContractService {
    @Autowired
    ContractDao contractDao;
    @Autowired
    MapObjectCruder mapObjectCruder;

    public ObizContract queryContract(String contractId) {
        return contractDao.queryContract(contractId);
    }

    public int refreshContractBalance(String contractId){
        //1.计算合同总余额
        String sumSQL = "SELECT SUM(DUEBILL_AMT) AS CONTRACT_BALANCE FROM OBIZ_DUEBILL WHERE CONTRACT_ID=:contractId";
        MapObject row = mapObjectCruder.selectOne(sumSQL, MapKit.mapOf("contractId",contractId));

        //2.设置并更新合同总余额
        double contractBalance = row.getValue("contractBalance").doubleValue();
        MapObject contractData = new MapObject();
        contractData.put("contractBalance",contractBalance);
        MapObject contractKey = new MapObject();
        contractKey.put("contractId",contractId);

        return mapObjectCruder.update("OBIZ_CONTRACT",contractData,contractKey);
    }
}
