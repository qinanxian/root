package com.vekai.workflow.nopub.service.impl;

import com.vekai.workflow.exception.WorkflowException;
import com.vekai.workflow.nopub.service.WorkflowIdentityService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户及用户组操作
 * @author  陈文楷 <wkchen@amarsoft.com>
 * @Date 2017/12/26
 */
@Component
public class WorkflowIdentityServiceImpl implements WorkflowIdentityService {

    @Autowired
    private IdentityService identityService;

    @Override
    public User getUser(String userId) {
        User user = identityService.createUserQuery().userId(userId).singleResult();
        if(null == user){
            throw new WorkflowException("[0]该用户不存在",userId);
        }
        return user;
    }

    @Override
    public List<User> getUsers(List<String> userIds) {
        List<User> userList = new ArrayList<>();
        for (String userId:userIds) {
            User user = identityService.createUserQuery().userId(userId).singleResult();
            if(null == user){
                throw new WorkflowException("[0]该用户不存在",userId);
            }
            userList.add(user);
        }
        if(0 == userList.size()){
            throw new WorkflowException("不存在用户");
        }
        return userList;
    }

    @Override
    public Group getGroup(String groupId) {
        Group group = identityService.createGroupQuery().groupId(groupId).singleResult();
        if(null == group){
            throw new WorkflowException("[0]该用户组不存在",groupId);
        }
        return group;
    }

    @Override
    public List<User> getGroupUsers(String groupId) {
        List<User> users = identityService.createUserQuery().memberOfGroup(groupId).list();
        if(0 == users.size()){
            throw new WorkflowException("[0]该用户组下不存在用户");
        }
        return users;
    }
}
