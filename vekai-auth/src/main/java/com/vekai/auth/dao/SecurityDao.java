package com.vekai.auth.dao;

import cn.fisok.sqloy.annotation.SqlParam;
import com.vekai.auth.entity.Org;
import com.vekai.auth.entity.RolePermit;
import cn.fisok.sqloy.annotation.SQLDao;

import java.util.List;

@SQLDao
public interface SecurityDao {
    List<RolePermit> getRolePermits(@SqlParam("roleId") String roleId);
    List<RolePermit> getUserPermits(@SqlParam("userId") String userId);
    List<Org> getSubOrgList(@SqlParam("orgId") String orgId);
}
