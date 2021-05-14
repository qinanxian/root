package com.vekai.appframe.cmon.message.service;

import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.appframe.cmon.message.dao.CmonMessageDao;
import com.vekai.appframe.cmon.message.model.UserMessage;
import com.vekai.appframe.cmon.message.entity.CmonMessage;
import com.vekai.appframe.cmon.message.entity.CmonMessageReceiver;
import cn.fisok.raw.kit.ListKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CmonMessageService {
    @Autowired
    protected CmonMessageDao cmonMessageDao;
    @Autowired
    protected BeanCruder beanCruder;


    public int sendMessage(CmonMessage message, CmonMessageReceiver... receivers){
        return sendMessage(message, ListKit.listOf(receivers));
    }

    @Transactional
    public int sendMessage(CmonMessage message, List<CmonMessageReceiver> receivers){
        int ret = 0;
        ret += beanCruder.insert(message);
        String messageId = message.getMessageId();

        receivers.forEach(receiver->{
            receiver.setHaveRead("N");
            receiver.setMessageId(messageId);
        });

        ret += beanCruder.insert(receivers);

        return ret;
    }

    public List<UserMessage> getUnreadMessages(String userId){
        return cmonMessageDao.getUserMessages("N",userId);
    }

    public List<UserMessage> getReadMessages(String userId){
        return cmonMessageDao.getUserMessages("Y",userId);
    }

    public int readMessage(String userId,String receiverId){
        return cmonMessageDao.updateUserMessageStatus("Y",receiverId,userId);
    }

}
