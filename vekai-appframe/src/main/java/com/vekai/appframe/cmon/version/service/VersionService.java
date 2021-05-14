package com.vekai.appframe.cmon.version.service;

import cn.fisok.raw.kit.MapKit;
import cn.fisok.sqloy.autoconfigure.SqloyProperties;
import cn.fisok.sqloy.core.BeanCruder;
import cn.fisok.sqloy.core.DBType;
import com.vekai.appframe.cmon.version.entity.CmonVersion;
import com.vekai.appframe.cmon.version.entity.CmonVersionChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by luyu on 2018/9/30.
 */
@Component
public class VersionService {

    @Autowired
    SqloyProperties sqloyProperties;
    @Autowired
    BeanCruder beanCruder;

    /*
     * 获取最新的版本完整信息
     */
    public CmonVersion getVersionInfo() {
        String versionSql = "SELECT * FROM CMON_VERSION";
        if (beanCruder.getDBType() == DBType.ORACLE) {
            versionSql += " WHERE rownum = 1 ORDER BY RELEASE_TIME DESC";
        } else if (beanCruder.getDBType() == DBType.MYSQL) {
            versionSql += " ORDER BY RELEASE_TIME DESC LIMIT 1";
        }
        List<CmonVersion> cmonVersions = beanCruder.selectList(CmonVersion.class,versionSql);
        if (cmonVersions == null || cmonVersions.size() == 0)
            return null;
        CmonVersion cmonVersion = cmonVersions.get(0);
        String versionChangeSql = "SELECT * FROM CMON_VERSION_CHANGE WHERE VERSION_ID=:versionId";
        List<CmonVersionChange> cmonVersionChanges = beanCruder.selectList(CmonVersionChange.class,versionChangeSql,
                MapKit.mapOf("versionId",cmonVersion.getVersionId()));
        cmonVersion.setVersionChanges(cmonVersionChanges);
        return cmonVersion;
    }



}
