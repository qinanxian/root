//package com.vekai.crops.appframe;
//
//import com.vekai.crops.BaseTest;
//import com.vekai.appframe.cmon.message.entity.CmonMessage;
//import com.vekai.appframe.cmon.message.entity.CmonMessageReceiver;
//import com.vekai.appframe.cmon.message.model.UserMessage;
//import com.vekai.appframe.cmon.message.service.CmonMessageService;
//import com.vekai.runtime.kit.DateKit;
//import com.vekai.runtime.kit.JSONKit;
//import org.junit.Assert;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.List;
//
//public class CmonMessageServiceTest extends BaseTest {
//
//    @Autowired
//    CmonMessageService cmonMessageService;
//
//    @Test
//    public void sendMessageTest(){
//        Assert.assertNotNull(cmonMessageService);
//
//        CmonMessage message = new CmonMessage();
//        CmonMessageReceiver receiver = new CmonMessageReceiver();
//        message.setSender("admin");
//        message.setTitle("测试消息标题");
//        message.setContent("测试消息内容"+ DateKit.format(DateKit.now()));
//        receiver.setReceiveUserId("syang");
//
//
//        cmonMessageService.sendMessage(message,receiver);
//
//        List<UserMessage> msgList = cmonMessageService.getUnreadMessages("syang");
//        System.out.println(JSONKit.toJsonString(msgList));
//
//    }
//}
