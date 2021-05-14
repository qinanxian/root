package com.vekai.workflow.nopub.service;

import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;

import java.util.List;

public interface WorkflowIdentityService {
    User getUser(String userId);
    List<User> getUsers(List<String> userIds);
    Group getGroup(String groupId);
    List<User> getGroupUsers(String groupId);
}
