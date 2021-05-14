package com.vekai.fiscal.event.listener;

import com.vekai.fiscal.event.model.FiscalEvent;
import com.vekai.fiscal.event.wrapper.FiscalEventWrapperEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 订阅财务事件，记会计分录(使用继承方式）
 *  * Created by 杨松<yangsong158@qq.com> on 2018-06-14
 */
@Component
public class FiscalEventListenerExt implements ApplicationListener<FiscalEventWrapperEvent> {
    public void onApplicationEvent(FiscalEventWrapperEvent eventWrapper) {
        FiscalEvent event = eventWrapper.getEvent();
        //记录会计分录
        System.out.println("财务事件1："+event.getEventName());
    }

}
