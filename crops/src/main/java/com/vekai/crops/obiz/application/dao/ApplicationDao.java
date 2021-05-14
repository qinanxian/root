package com.vekai.crops.obiz.application.dao;

import com.vekai.crops.obiz.application.entity.ObizApplication;
import cn.fisok.sqloy.annotation.SQLDao;
import cn.fisok.sqloy.annotation.SqlParam;

@SQLDao
public interface ApplicationDao {
    /**
     * 取申请的流程定义KEY，资料清单等配置参数
     * @param applicationId
     * @return
     */
    ObizApplication queryApplicationProfile(@SqlParam("applicationId") String applicationId);
    ObizApplication queryApplication(@SqlParam("applicationId") String applicationId);
}
