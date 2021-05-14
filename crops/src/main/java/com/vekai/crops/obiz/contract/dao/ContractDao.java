package com.vekai.crops.obiz.contract.dao;

import com.vekai.crops.obiz.contract.entity.ObizContract;
import cn.fisok.sqloy.annotation.SQLDao;
import cn.fisok.sqloy.annotation.SqlParam;

@SQLDao
public interface ContractDao {
    ObizContract queryContract(@SqlParam("contractId") String contractId);
}
