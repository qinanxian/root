package com.vekai.crops.common.service;

import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.crops.common.entity.AuthOrg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AuthOrgService {

    @Autowired
    private BeanCruder beanCruder;

    public AuthOrg getAuthOrgByOrgId(String orgId){

        String sql = "select * from AUTH_ORG where ID = :orgId";
        AuthOrg authOrg = beanCruder.selectOne(AuthOrg.class, sql, "orgId", orgId);

        return authOrg;
    }
}
