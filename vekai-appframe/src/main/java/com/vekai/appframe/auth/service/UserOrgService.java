package com.vekai.appframe.auth.service;

import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.auth.entity.UserOrg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserOrgService {
    @Autowired
    protected BeanCruder beanCruder;

    /**
     * 获取User_Org表中指定用户数据
     * @param userId
     * @return
     */
    public List<UserOrg> selectUserOrgList(String userId){
        String sql = "select * from AUTH_USER_ORG where USER_ID=:userId";
        return beanCruder.selectList(UserOrg.class,sql,"userId",userId);
    }
    /**
     * 保存User_Org表熟路列表
     * @param userOrgList
     */
    public int saveUserOrgList(List<UserOrg> userOrgList){
        return beanCruder.save(userOrgList);
    }
    /**
     * 获取User_Org表中指定用户，指定机构的数据
     * @param userId orgId
     * @return
     */
    public UserOrg selectUserOrg(String userId,String orgId){
        String sql = "select * from AUTH_USER_ORG where USER_ID=:userId and ORG_ID=:orgId";
        return beanCruder.selectOne(UserOrg.class,sql,"userId",userId,"orgId",orgId);
    }
    /**
     * 保存User_Org表数据
     * @param userOrg
     */
    public int saveUserOrg(UserOrg userOrg){
        return beanCruder.save(userOrg);
    }

}
