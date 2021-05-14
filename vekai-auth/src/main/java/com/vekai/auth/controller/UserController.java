package com.vekai.auth.controller;


import cn.fisok.sqloy.core.BeanCruder;
import com.fasterxml.jackson.databind.JsonNode;
import com.vekai.auth.autoconfigure.AuthProperties;
import com.vekai.auth.entity.PwdDTO;
import com.vekai.auth.entity.User;
import com.vekai.auth.entity.UserBehavior;
import com.vekai.auth.holder.AuthHolder;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.kit.ValidateKit;
import com.vekai.auth.service.AuthService;
import io.swagger.annotations.Api;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Map;

@Api(description = "用户相关信息处理")
@RestController
@RequestMapping("/auth/user/")
public class UserController {
    @Autowired
    AuthService authService;
    @Autowired
    AuthProperties authProperties;
    @Autowired
    protected BeanCruder beanCruder;

    @GetMapping("/my/behaviors")
    public List<UserBehavior> getMyBehaviors(){
        User user = AuthHolder.getUser();
        if(user != null){
            return beanCruder.selectList(UserBehavior.class,
                    "select * from AUTH_USER_BEHAVIOR where USER_ID=:userId",
                    "userId",user.getId()
            );
        }else{
            return null;
        }
    }

    @GetMapping("/my/behaviors/{objectType}")
    public UserBehavior getMyBehaviorsByObjectType(@PathParam("objectType") String objectType){
        User user = AuthHolder.getUser();
        Map<String,String> param = MapKit.mapOf("userId",user.getId(),"objectType",objectType);
        if(user != null){
            return beanCruder.selectOne(UserBehavior.class,
                    "select * from AUTH_USER_BEHAVIOR where OBJECT_TYPE=:objectType and USER_ID=:userId",param);
        }else{
            return null;
        }
    }

    @PostMapping("/my/saveBehavior")
    public int saveBehavior(@RequestBody UserBehavior behavior){
        User user = AuthHolder.getUser();

        if(user != null){
            ValidateKit.notNull(behavior.getObjectType());
            ValidateKit.notNull(behavior.getObjectId());
            behavior.setUserId(user.getId());

            Map<String,String> param = MapKit.mapOf("userId",user.getId(),"objectType",behavior.getObjectType());
            int count = beanCruder.selectCount("select * from AUTH_USER_BEHAVIOR where USER_ID=:userId and OBJECT_TYPE=:objectType ",param);
            if(count==0){
                return beanCruder.insert(behavior);
            }else{
                return beanCruder.update(behavior);
            }
        }else{
            return 0;
        }
    }

    @DeleteMapping("/my/behaviors/{objectType}/{objectId}")
    public int deleteMyBehavior(@PathParam("objectType") String objectType,@PathParam("objectId") String objectId){

        User user = AuthHolder.getUser();
        if(user != null){
            Map<String,String> param = MapKit.mapOf("userId",user.getId(),"objectType",objectType,"objectId",objectId);
            return beanCruder.execute("delete from AUTH_USER_BEHAVIOR where USER_ID=:userId and OBJECT_TYPE=:objectType and OBJECT_ID=:objectId",
                    param);
        }else{
            return 0;
        }

    }

    @PostMapping("/updatePassword")
    public int updatePassword(@RequestBody PwdDTO param){
        String oldPwd = param.getOldPwd();
        String newPwd = param.getNewPwd();
        User user = AuthHolder.getUser();
        SimpleHash simpleHash = new SimpleHash(authProperties.getPwdHashAlgorithmName(), oldPwd,
                null, authProperties.getHashIterations());
        if(simpleHash.toBase64().equals(user.getPassword())){
            authService.updatePassword(user.getId(), oldPwd, newPwd, false);
            return 1;
        }else {
            return 0;
        }
    }
}
