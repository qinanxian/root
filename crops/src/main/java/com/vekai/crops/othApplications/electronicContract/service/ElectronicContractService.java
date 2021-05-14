package com.vekai.crops.othApplications.electronicContract.service;

import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.crops.codetodo.image.entity.MsbImageRecord;
import com.vekai.crops.othApplications.electronicContract.entity.ElectronicContract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ElectronicContractService {

    protected static Logger logger = LoggerFactory.getLogger(ElectronicContractService.class);

    @Autowired
    private BeanCruder beanCruder;

    /**
     * 根据realId获取客户意愿缩略图
     * @param custId-----关联id
     */
    public ElectronicContract getPdfFileById(String custId){

        String sql ="select * from ELECTRONIC_CONTRACT where id = :id";

        ElectronicContract electronicContract = beanCruder.selectOne(ElectronicContract.class, sql, "id", custId);

        return electronicContract;
    }
}
