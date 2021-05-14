package com.vekai.fiscal.event.dao;

import com.vekai.fiscal.event.model.def.FiscalEventParamDef;
import com.vekai.fiscal.event.model.def.FiscalEventDef;
import com.vekai.fiscal.event.model.def.FiscalEventEntryDef;
import cn.fisok.sqloy.annotation.SQLDao;
import cn.fisok.sqloy.annotation.SqlParam;

import java.util.List;

@SQLDao
public interface ConfFiscalDao {
    FiscalEventDef getFiscalEventDef(@SqlParam("eventDef") String eventDef);

    List<FiscalEventParamDef> getEventParamDefList(@SqlParam("eventDef") String eventDef);

    List<FiscalEventEntryDef> selectEventEntryDefList(@SqlParam("eventDef") String eventDef);
}
