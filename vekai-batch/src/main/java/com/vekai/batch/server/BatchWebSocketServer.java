package com.vekai.batch.server;

import com.vekai.batch.entity.TimerEntity;
import com.vekai.batch.service.BatchJobService;
import cn.fisok.raw.holder.ApplicationContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.Collection;
import java.util.List;

//要加,configurator = SpringConfigurator.class,否则@Autowired会无效,因为默认情况下，它用ServerEndpoint注解初始化了
//@ServerEndpoint(value = "/batch/websocket", configurator = SpringConfigurator.class)
@ServerEndpoint(value = "/batch/websocket")
@Component
public class BatchWebSocketServer {
    private Logger logger = LoggerFactory.getLogger(getClass());

    private BatchJobService batchJobService;

    private static int onlineCount = 0;
    private static BatchWebSocketServer instance = null;

    public BatchWebSocketServer() {
        instance = this;
    }
    public static BatchWebSocketServer getInstance(){
        return instance;
    }


    //构造器-->自动注入-->PostConstrut-->InitializingBean-->xml中配置init方法
    //PreDestroy--》DisposableBean-->xml中destroy-method方法
    @PostConstruct
    public void init() {
//        if(batchJobService==null){
//            batchJobService = ApplicationContextHolder.getBean(BatchJobService.class);
//        }
    }

    /**
     * 连接建立成功调用的方法
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        addOnlineCount();
        BatchWebSockerHolder.sessionPool.put(session.getId(), session);

        logger.debug("【" + session.getId() + "】连接上服务器======当前在线人数【" + getOnlineCount() + "】");
        //连接成功后，返回所有的定时器列表
        List<TimerEntity> timerEntities = getBatchJobService().getTimers();
        BatchWebSockerHolder.sendMessage(session, BatchWebSockerHolder.responseObject("timerList",timerEntities).jsonText());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(Session session) {
        subOnlineCount();
        BatchWebSockerHolder.sessionPool.remove(session.getId());

        logger.debug("【" + session.getId() + "】退出了连接======当前在线人数【" + getOnlineCount() + "】");
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param session
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(Session session, String message) {
        logger.info("来自客户端的消息:" + message);

    }

    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("发生错误", error);
    }


    /**
     * 群发消息
     *
     * @param message
     */
    public synchronized void sendBroadcastMessage(String message) {
        Collection<Session> sessions = BatchWebSockerHolder.sessionPool.values();
        sessions.forEach(session -> {
            BatchWebSockerHolder.sendMessage(session, message);
        });
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        onlineCount--;
    }

    public BatchJobService getBatchJobService() {
        if (batchJobService == null) {
            batchJobService = ApplicationContextHolder.getBean(BatchJobService.class);
        }
        return batchJobService;
    }

}
