package com.vekai.crops.obiz.application.service;

import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.crops.obiz.application.dao.ApplicationDao;
import com.vekai.crops.obiz.application.entity.ObizApplication;
import com.vekai.auth.holder.AuthHolder;
import cn.fisok.raw.kit.DateKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApplicationService {
    @Autowired
    ApplicationDao applicationDao;
    @Autowired
    BeanCruder beanCruder;

    public ObizApplication queryApplicationProfile(String applicationId) {
        return applicationDao.queryApplicationProfile(applicationId);
    }

    public ObizApplication queryApplication(String applicationId) {
        return applicationDao.queryApplication(applicationId);
    }

    public int updateAppMilestone(String applicationId,String milestone){
        String sql = "UPDATE OBIZ_APPLICATION " +
                "SET APP_MILESTONE=:milestone,UPDATED_BY=:updatedBy,UPDATED_TIME=:updatedTime " +
                "WHERE APPLICATION_ID=:applicationId";
        MapObject params = new MapObject();
        params.put("updatedBy", AuthHolder.getUser().getId());
        params.put("updatedTime", DateKit.now());
        params.put("milestone", milestone);
        params.put("applicationId", applicationId);

        return beanCruder.execute(sql,params);
    }
}
