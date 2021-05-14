package com.vekai.batch.support.impl;

import cn.fisok.sqloy.core.BeanCruder;
import cn.fisok.sqloy.core.BeanQuery;
import cn.fisok.sqloy.core.BeanUpdater;
import com.vekai.batch.entity.ApnsBatchLog;
import com.vekai.batch.support.BatchLogDao;
import com.vekai.batch.support.LogAppenderSupportHolder;
import cn.fisok.raw.kit.MapKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;


public class BatchLogDaoDBImpl implements BatchLogDao {

    @Autowired
    protected DataSource dataSource;


    public BeanCruder getbeanCruder() {
        //考虑到一个线程中多次被调用的情况，
        /**
         * 这里不能直接使用容器注入的DataUpdater，否则业务发生异常，会导致日志写入数据库连接跟着回溯的情况
         * 因此，这里重新申请一个新的DataUpdater,并且绑定到当前线程
         */
        BeanCruder beanCruder = LogAppenderSupportHolder.getbeanCruder();
        if(beanCruder == null){
            NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
            BeanUpdater dataUpdater = new BeanUpdater();
            BeanQuery dataQuery = new BeanQuery();
            dataUpdater.setJdbcTemplate(jdbcTemplate);
            dataQuery.setJdbcTemplate(jdbcTemplate);

            beanCruder = new BeanCruder(dataQuery,dataUpdater);
            LogAppenderSupportHolder.setLogDaoDataUpdater(beanCruder);
        }
        return beanCruder;
    }


    @Transactional(isolation= Isolation.READ_UNCOMMITTED,propagation= Propagation.REQUIRES_NEW)
    public int insertBatchLogList(final List<ApnsBatchLog> logList) {
        BeanCruder beanCruder = getbeanCruder();
        return beanCruder.insert(logList);
    }



    @Transactional(isolation= Isolation.READ_UNCOMMITTED,propagation= Propagation.REQUIRES_NEW)
    public int insertBatchLog(final ApnsBatchLog logItem) {
        //使用另一个线程写数据，防止阻塞
        BeanCruder beanCruder = getbeanCruder();
        return beanCruder.insert(logItem);
    }

    @Transactional(isolation= Isolation.READ_UNCOMMITTED,propagation= Propagation.REQUIRES_NEW)
    public List<ApnsBatchLog> getBatchLogList(Long jobInstanceId) {
        BeanCruder beanCruder = getbeanCruder();
        return beanCruder.selectList(ApnsBatchLog.class,
                "select * from APNS_BATCH_LOG where JOB_INSTANCE_ID=:jobId order by LINE_NUMBER ASC",
                MapKit.mapOf("jobId",jobInstanceId));
    }
}
