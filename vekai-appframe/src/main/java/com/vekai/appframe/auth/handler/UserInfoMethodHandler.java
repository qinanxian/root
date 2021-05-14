package com.vekai.appframe.auth.handler;

import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.appframe.auth.service.UserOrgService;
import com.vekai.appframe.constant.AppframeConst;
import com.vekai.auth.autoconfigure.AuthProperties;
import com.vekai.auth.entity.User;
import com.vekai.auth.entity.UserOrg;
import com.vekai.auth.service.AuthService;
import com.vekai.dataform.handler.impl.MapDataOneHandler;
import com.vekai.dataform.model.DataForm;
import cn.fisok.raw.kit.BeanKit;
import cn.fisok.raw.kit.StringKit;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class UserInfoMethodHandler extends MapDataOneHandler {
    @Autowired
    AuthService authService;
    @Autowired
    UserOrgService userOrgService;
    @Autowired
    BeanCruder beanCruder;
    @Autowired
    AuthProperties authProperties;

    @Override
    @Transactional
    public int save(DataForm dataForm, MapObject object){
        String userid = object.getValue("id").strValue();
        String orgid = object.getValue("orgId").strValue();
        User user = null;
//        if (StringKit.isBlank(userid)) {
            user = new User();
            BeanKit.copyProperties(object,user);
        SimpleHash simpleHash = new SimpleHash(authProperties.getPwdHashAlgorithmName(), "urcb"+userid,
                null, authProperties.getHashIterations());
        user.setPassword(simpleHash.toBase64());
            beanCruder.save(user);
//        } else {
//            user = authService.getUser(userid);
//            BeanKit.copyProperties(object,user);
//            authService.saveUser(user);
//        }
        userid = user.getId();
        List<UserOrg> userOrgList = userOrgService.selectUserOrgList(userid);
        for (UserOrg userOrg:userOrgList) {
            userOrg.setIsMain(AppframeConst.YES_NO_N);
        }
        userOrgService.saveUserOrgList(userOrgList);
        UserOrg userOrg = userOrgService.selectUserOrg(userid,orgid);
        if(userOrg == null){
            userOrg = new UserOrg();
            BeanKit.copyProperties(object,userOrg);
            userOrg.setId(null);
        }
        userOrg.setUserId(userid);
        userOrg.setIsMain(AppframeConst.YES_NO_Y);
        userOrgService.saveUserOrg(userOrg);
        return 1;
    }

}
