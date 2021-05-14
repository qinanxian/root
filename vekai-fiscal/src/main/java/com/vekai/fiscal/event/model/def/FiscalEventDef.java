package com.vekai.fiscal.event.model.def;

import com.vekai.fiscal.event.entity.ConfFiscEventPO;

import java.io.Serializable;
import java.util.List;

/**
 * 财务事件模型
 * Created by 炮弹侠<yangsong158@qq.com> on 2018-06-10
 */
public class FiscalEventDef extends ConfFiscEventPO implements Serializable,Cloneable{
    protected List<FiscalEventParamDef> params;
    protected List<FiscalEventEntryDef> entries;

    public List<FiscalEventParamDef> getParams() {
        return params;
    }

    public void setParams(List<FiscalEventParamDef> params) {
        this.params = params;
    }

    public List<FiscalEventEntryDef> getEntries() {
        return entries;
    }

    public void setEntries(List<FiscalEventEntryDef> entries) {
        this.entries = entries;
    }

}
