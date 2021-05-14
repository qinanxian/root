package com.vekai.appframe.cmon.message.dao;

import com.vekai.appframe.cmon.message.model.UserMessage;
import cn.fisok.sqloy.annotation.SQLDao;
import cn.fisok.sqloy.annotation.SqlParam;

import java.util.List;

@SQLDao
public interface CmonMessageDao {
    List<UserMessage> getUserMessages(@SqlParam("haveRead") String haveRead, @SqlParam("userId") String userId);

    int updateUserMessageStatus(@SqlParam("haveRead") String haveRead, @SqlParam("receiverId") String receiverId, @SqlParam("receiverUserId") String receiverUserId);
}
