package com.vekai.appframe.cmon.message.controller;

import com.vekai.appframe.cmon.message.model.UserMessage;
import com.vekai.appframe.cmon.message.entity.CmonMessage;
import com.vekai.appframe.cmon.message.entity.CmonMessageReceiver;
import com.vekai.appframe.cmon.message.service.CmonMessageService;
import com.vekai.auth.entity.User;
import com.vekai.auth.holder.AuthHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;
import java.util.List;

@RestController
@RequestMapping("common/message")
public class CmonMessageController {
    @Autowired
    protected CmonMessageService cmonMessageService;

    public int sendMessage(CmonMessage message, List<CmonMessageReceiver> receivers) {
        return cmonMessageService.sendMessage(message, receivers);
    }

    @GetMapping("/unreadMessages")
    public List<UserMessage> getUnreadMessages() {
        User user = AuthHolder.getUser();
        if(user == null)return null;
        return cmonMessageService.getUnreadMessages(user.getId());
    }

    @GetMapping("/readMessages")
    public List<UserMessage> getReadMessages() {
        User user = AuthHolder.getUser();
        if(user == null)return null;
        return cmonMessageService.getReadMessages(user.getId());
    }

    @PostMapping("/readMessage/{receiverId}")
    public int readMessage(@PathParam("receiverId") String receiverId) {
        User user = AuthHolder.getUser();
        if(user == null)return 0;
        return cmonMessageService.readMessage(user.getId(), receiverId);
    }
}
