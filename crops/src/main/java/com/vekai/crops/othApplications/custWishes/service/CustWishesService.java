package com.vekai.crops.othApplications.custWishes.service;

import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.crops.codetodo.image.entity.MsbImageRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustWishesService {

    protected static Logger logger = LoggerFactory.getLogger(CustWishesService.class);

    @Autowired
    private BeanCruder beanCruder;

    /**
     * 根据realId获取客户意愿缩略图
     * @param realId-----关联id
     * @param colName-----列名
     */
    public List<MsbImageRecord> getImageRecordByRealId(String realId, String colName){

        String sql = "select * from MSB_IMAGE_RECORD where REAL_ID = :realId and REAL_TYPE = '0004'" +
                " and  BIG_IMAGE_ID=(select " +colName+ " from ELECTRONIC_INTEND WHERE ID = :realId)";
        List<MsbImageRecord> imageRecordList = beanCruder.selectList(MsbImageRecord.class, sql, "realId", realId,"colName",colName,"realId",realId);

        return imageRecordList;
    }

    /**
     * 根据realId获取客户意愿缩略图
     * @param realId-----关联id
     * @param bigFileId-----大图id
     */
    public List<MsbImageRecord> getImageRecordByBigFileId(String realId, String bigFileId){

        String sql = "SELECT * FROM MSB_IMAGE_RECORD WHERE REAL_TYPE = '0004' AND REAL_ID  =:realId AND BIG_IMAGE_ID =:bigFileId" ;

        List<MsbImageRecord> imageRecordList = beanCruder.selectList(MsbImageRecord.class, sql, "realId", realId,"bigFileId",bigFileId);

        return imageRecordList;
    }
}
