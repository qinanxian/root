package com.vekai.fiscal.event.wrapper;

import com.vekai.fiscal.event.model.FiscalEvent;
import org.springframework.context.ApplicationEvent;

/**
 * 财务记账应用事件
 * Created by 炮弹侠<yangsong158@qq.com> on 2018-06-13
 */
public class FiscalEventWrapperEvent extends ApplicationEvent {

    private FiscalEvent event;

    public FiscalEventWrapperEvent(Object object,FiscalEvent event) {
        super(object);
        this.event = event;
    }

    public FiscalEvent getEvent() {
        return event;
    }
}
