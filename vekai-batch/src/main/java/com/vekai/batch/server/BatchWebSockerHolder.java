package com.vekai.batch.server;

import cn.fisok.raw.kit.JSONKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.Session;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BatchWebSockerHolder {
    private static Logger logger = LoggerFactory.getLogger(BatchWebSockerHolder.class);

    public static final Map<String, Session> sessionPool = new ConcurrentHashMap<String, Session>();//保存所有连接上的session

    /**
     * 群发消息
     *
     * @param message
     */
    public static synchronized void sendBroadcastMessage(String message) {
        Collection<Session> sessions = sessionPool.values();
        sessions.forEach(session -> {
            sendMessage(session, message);
        });
    }

    //统一的发送消息方法

    /**
     * @param session
     * @param message
     */
    public static synchronized void sendMessage(Session session, String message) {
        if(session==null)return;
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            logger.error("服务端发送消息出错", e);
        }
    }

    /**
     * @param sessionId
     * @param message
     */
    public static synchronized void sendMessage(String sessionId, String message) {
        Session session = sessionPool.get(sessionId);
        sendMessage(session,message);
    }

    public static ResponseObject responseObject(String type,Object body){
        return new ResponseObject(type,body);
    }

    public static class ResponseObject{
        private String type;
        private Object body;

        public ResponseObject() {
        }

        public ResponseObject(String type, Object body) {
            this.type = type;
            this.body = body;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Object getBody() {
            return body;
        }

        public void setBody(Object body) {
            this.body = body;
        }

        public String jsonText(){
            return JSONKit.toJsonString(this);
        }
    }
}
