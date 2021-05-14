package com.vekai.fiscal.event.service;

import com.vekai.fiscal.event.model.FiscalEvent;
import com.vekai.fiscal.event.model.def.FiscalEventDef;

/**
 * 财务事件操作服务接口
 * Created by 炮弹侠<yangsong158@qq.com> on 2018-06-10
 */
public interface FisicalEventService {
    /**
     * 获取一个财务事件的定义模板
     * @param eventDef
     * @return
     */
    FiscalEventDef getFiscalEventDef(String eventDef);

    /**
     * 获取一个财务事件实例
     * @param eventId
     * @return
     */
    FiscalEvent getFiscalEvent(String eventId);

    /**
     * 根据业务参数，触发一个财务事件
     * 1.记录到事件表
     * 2.通过spring容器发布事件对象 context.publishEvent(event);
     * @param eventDef
     * @param eventData
     * @return
     */
    FiscalEvent publishEvent(String eventDef,EventData eventData);
}
