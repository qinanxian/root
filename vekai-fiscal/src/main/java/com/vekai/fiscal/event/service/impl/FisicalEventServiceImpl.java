package com.vekai.fiscal.event.service.impl;

import com.vekai.fiscal.exception.FiscalException;
import com.vekai.fiscal.event.dao.ConfFiscalDao;
import com.vekai.fiscal.event.model.FiscalEventEntry;
import com.vekai.fiscal.event.model.FiscalEventParam;
import com.vekai.fiscal.event.model.def.FiscalEventEntryDef;
import com.vekai.fiscal.event.model.def.FiscalEventParamDef;
import com.vekai.fiscal.event.service.EventData;
import com.vekai.fiscal.event.model.FiscalEvent;
import com.vekai.fiscal.event.model.def.FiscalEventDef;
import com.vekai.fiscal.event.service.FisicalEventService;
import com.vekai.fiscal.event.wrapper.FiscalEventWrapperEvent;
import cn.fisok.raw.holder.ApplicationContextHolder;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.raw.lang.ValueObject;
import cn.fisok.sqloy.core.BeanCruder;
import cn.fisok.sqloy.loadmd.SQLCollecter;
import cn.fisok.sqloy.loadmd.SQLTextLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 财务事件服务实现
 * Created by 炮弹侠<yangsong158@qq.com> on 2018-06-13
 */
@Service
public class FisicalEventServiceImpl implements FisicalEventService {

    @Autowired
    private BeanCruder beanCruder;
    @Autowired
    private SQLTextLoader sqlTextLoader;
    @Autowired
    private ConfFiscalDao confFiscalDao;
    @Autowired
    EventEntryCalculatorService eventEntryCalculatorService;

    public BeanCruder getbeanCruder() {
        return beanCruder;
    }

    public void setbeanCruder(BeanCruder beanCruder) {
        this.beanCruder = beanCruder;
    }

    public SQLTextLoader getSqlTextLoader() {
        return sqlTextLoader;
    }

    public void setSqlTextLoader(SQLTextLoader sqlTextLoader) {
        this.sqlTextLoader = sqlTextLoader;
    }

    /**
     * 只用来参考下，以后将不再使用，这样使用太复杂
     * @param defKey
     * @return
     */
    @Deprecated
    public FiscalEventDef getFiscalEventDefOld(String defKey) {
        FiscalEventDef eventDef = null;

        String res = "classpath:com/vekai/fiscal/sql/ConfFiscalDao.sql.md";
        SQLCollecter sqlCollecter = sqlTextLoader.parse(res);

        //1.查询财务事件定义
        String selectOneEventDef = sqlCollecter.sql("selectOneEventDef");
        eventDef = beanCruder.selectOne(FiscalEventDef.class,
                selectOneEventDef,
                "eventDef",defKey);
        if(eventDef == null){
            throw new FiscalException("财务事件定义{0}不存在",defKey);
        }

        //2.查询财务事件的参数集
        String selectListEventParamDef = sqlCollecter.sql("selectListEventParamDef");
        List<FiscalEventParamDef> paramDefs = null;
        paramDefs = beanCruder.selectList(FiscalEventParamDef.class,
                selectListEventParamDef,
                "eventDef",defKey);

        //3.查询财务事件下记账分录配置
        String selectListEventEntryDef = sqlCollecter.sql("selectListEventEntryDef");
        List<FiscalEventEntryDef> entryDefs = null;
        entryDefs = beanCruder.selectList(FiscalEventEntryDef.class,
                selectListEventEntryDef,
                "eventDef",defKey);

        //组装对象
        eventDef.setParams(paramDefs);
        eventDef.setEntries(entryDefs);


        return eventDef;
    }

    public FiscalEventDef getFiscalEventDef(String defKey) {
        //1.查询财务事件定义
        FiscalEventDef eventDef = confFiscalDao.getFiscalEventDef(defKey);
        if(eventDef == null){
            throw new FiscalException("财务事件定义{0}不存在",defKey);
        }
        //2.查询财务事件的参数集
        List<FiscalEventParamDef> paramDefs = confFiscalDao.getEventParamDefList(defKey);
        //3.查询财务事件下记账分录配置
        List<FiscalEventEntryDef> entryDefs = confFiscalDao.selectEventEntryDefList(defKey);

        //组装对象
        eventDef.setParams(paramDefs);
        eventDef.setEntries(entryDefs);


        return eventDef;
    }

    public FiscalEvent getFiscalEvent(String eventId) {
        return null;
    }

    public FiscalEvent publishEvent(String eventDef, EventData data) {
        //0.外币转换
        convertCurrency(data);

        //1.读取制证规则
        FiscalEventDef def = getFiscalEventDef(eventDef);
        if(def == null){
            throw new FiscalException("财务事件定义[{0}]不存在",eventDef);
        }

        //2.填充财务事件对象
        FiscalEvent event = new FiscalEvent();
        //填充参数集
        List<FiscalEventParam> params = parseEventParams(data,def);
        //初始化事件
        initFiscalEvent(event,data,def,params);
        event.setParams(params);
        //填充事件分录
        List<FiscalEventEntry>  entries = parseEventEntries(data,def,params);
        event.setEntries(entries);

        //3.插入事件实例以及分录指导数据表
        saveFiscalEvent(event);

        //4.推送财务事件对象至容器中,如果容器匹配到订阅者，则订阅者根据财务事件对象，自行决定怎么记会计凭证
        publicEvent2Context(event);
        return event;
    }

    private void convertCurrency(EventData data) {
        if (data.getOccurAmt() != null)
            return ;
        data.setOccurAmt(data.getOriginalAmt() * data.getExchangeRateValue());
    }

    protected void initFiscalEvent(FiscalEvent event, EventData data, FiscalEventDef def, List<FiscalEventParam> params){
        Map<String,String> paramMap = new HashMap<>();
        for (FiscalEventParam fiscalEventParam:params) {
            paramMap.put(fiscalEventParam.getItemCode(),fiscalEventParam.getDataText());
        }
        //计算事件摘要
        String eventSummary = "";
        if(StringKit.isNotBlank(def.getEventIntro())){
            eventSummary = StringKit.fillTpl(def.getEventIntro(),paramMap);
        }

        event.setEventDef(def.getEventDef());
        event.setEventName(def.getEventName());
        event.setEventSummary(eventSummary);
        event.setSortCode(def.getSortCode());
        event.setDataModelType(def.getDataModelType());
        event.setDataModel(def.getDataModel());
        event.setOccurTime(data.getOccurTime());
        event.setMainCorpId(data.getMainCorpId());
        event.setObjectType(data.getObjectType());
        event.setObjectId(data.getObjectId());
        event.setCurrency(data.getCurrency());
        event.setOccurAmt(data.getOccurAmt());
        event.setMainCorpId(data.getMainCorpId());
        event.setObjectType(data.getObjectType());
        event.setObjectId(data.getObjectId());
        event.setExchangeRate(data.getExchangeRateValue());
        event.setExchangeRateType(data.getExchangeRateType());
    }

    /**
     * 根据参数设置，从数据中解析出需要的参数
     * @param data
     * @param def
     * @return
     */
    protected List<FiscalEventParam> parseEventParams(EventData data,FiscalEventDef def){
        List<FiscalEventParam> params = new ArrayList<FiscalEventParam>();

        List<FiscalEventParamDef> paramDefs = def.getParams();
        paramDefs.forEach(paramDef->{
            String dataType = paramDef.getDataType();
            String expr = StringKit.nvl(paramDef.getValueExpr(),paramDef.getItemCode());
            String isRequired = paramDef.getIsRequired();
            ValueObject valueObject = data.getParams().getValue(expr);
            Object value = null;
            //参数为必需，但数据中又没有的时候，报错
            if((valueObject.isNull()||valueObject.isEmpty())&&"Y".equalsIgnoreCase(isRequired)){
                throw new FiscalException("财务事件[{0}]调用出错，参数:[{1}-{2}],参数EXPR:[{3}],参数值为空",paramDef.getEventDef(),paramDef.getItemCode(),paramDef.getItemName(),expr);
            }

            FiscalEventParam paramItem = new FiscalEventParam();
            paramItem.setDataText(valueObject.strValue());
            paramItem.setItemCode(paramDef.getItemCode());
            paramItem.setItemName(paramDef.getItemName());
            paramItem.setDataType(paramDef.getDataType());

            if("String".equalsIgnoreCase(dataType)){
                paramItem.setStrValue(valueObject.strValue());
                value = valueObject.strValue();
            }else if("Double".equalsIgnoreCase(dataType)){
                paramItem.setFloatValue(valueObject.doubleValue());
                value = valueObject.doubleValue();
            }else if("Integer".equalsIgnoreCase(dataType)){
                paramItem.setIntValue(valueObject.intValue());
                value = valueObject.intValue();
            }else if("Date".equalsIgnoreCase(dataType)){
                paramItem.setDateValue(valueObject.dateValue());
                value = valueObject.dateValue();
            }

            params.add(paramItem);

        });

        return params;

    }

    protected List<FiscalEventEntry> parseEventEntries(EventData data, FiscalEventDef def, List<FiscalEventParam> params){
        List<FiscalEventEntry> entries = new ArrayList<FiscalEventEntry>();
        List<FiscalEventEntryDef> entryDefs = def.getEntries();
        Map<String,String> paramMap = new HashMap<>();
        for (FiscalEventParam fiscalEventParam:params) {
            paramMap.put(fiscalEventParam.getItemCode(),fiscalEventParam.getDataText());
        }
        entryDefs.forEach(entryDef->{
            FiscalEventEntry entry = new FiscalEventEntry();
            entry.setEventDef(entryDef.getEventDef());
            entry.setEventEntryDef(entryDef.getEventEntryDef());
            entry.setBookCode(entryDef.getBookCode());
            entry.setEntryCode(entryDef.getEntryCode());
            entry.setEntryName(entryDef.getEntryName());
            entry.setDirection(entryDef.getDirection());

            //设置事件摘要
            this.fillEventSummary(entry,entryDef,paramMap);
            String valueExpr = entryDef.getValueFetcher();
            this.fillAmtAndCurrency(entry,data,valueExpr,paramMap);
            entries.add(entry);
        });

        return entries;
    }

    private void fillAmtAndCurrency(FiscalEventEntry entry, EventData data, String valueExpr, Map<String, String> paramMap) {
        entry.setCurrency(data.getCurrency());
        entry.setOriginalCurrency(data.getOriginalCurrency());
        Double occurAmt = 0D;
        //如果不使用本位币
        if (data.getOriginalCurrency() != null && data.getOriginalAmt() != null) {
            //如果没配置取舍方式，直接取金额填充
            if(StringKit.isBlank(valueExpr)){
                entry.setOriginalAmt(data.getOriginalAmt());
                occurAmt = data.getOriginalAmt() * data.getExchangeRateValue();
            } else {
                if (valueExpr.contains("$")) {
                    valueExpr = StringKit.fillTpl(valueExpr,paramMap);
                    entry.setOriginalAmt(Double.parseDouble(eventEntryCalculatorService.calculate(valueExpr)));
                    occurAmt = Double.parseDouble(eventEntryCalculatorService.calculate(valueExpr)) * data.getExchangeRateValue();
                } else {
                    entry.setOriginalAmt(data.getParams().getValue(valueExpr).doubleValue());
                    occurAmt = data.getParams().getValue(valueExpr).doubleValue() * data.getExchangeRateValue();
                }
            }
        } else {
            //如果没配置取舍方式，直接取金额填充
            if(StringKit.isBlank(valueExpr)){
                occurAmt = data.getOccurAmt();
            } else {
                if (valueExpr.contains("$")) {
                    valueExpr = StringKit.fillTpl(valueExpr,paramMap);
                    occurAmt = Double.parseDouble(eventEntryCalculatorService.calculate(valueExpr));
                } else {
                    occurAmt = data.getParams().getValue(valueExpr).doubleValue();
                }
            }
            entry.setOriginalAmt(null);
        }
        entry.setOccurAmt(occurAmt);
    }

    private void fillEventSummary(FiscalEventEntry entry, FiscalEventEntryDef entryDef, Map<String, String> params) {
        String tpl = entryDef.getEventSummary();
        if (!StringKit.isBlank(tpl)) {
            tpl = StringKit.fillTpl(tpl,params);
        }
        entry.setEventSummary(tpl);
    }

    protected int saveFiscalEvent(FiscalEvent event){
        int ret = 0;
        //记录主事件
        ret += beanCruder.save(event);
        //记录事件参数
        List<FiscalEventParam> params = event.getParams();
        params.forEach(param->{
            param.setEventId(event.getEventId());
        });
        ret += beanCruder.save(params);

        //记录会计分录
        List<FiscalEventEntry> entries = event.getEntries();
        entries.forEach(entry->{
            entry.setEventId(event.getEventId());
        });
        ret += beanCruder.save(entries);

        return ret;
    }

    /**
     * 发布事件至容器中
     * @param event
     */
    protected void publicEvent2Context(FiscalEvent event){
        ApplicationContext ctx = ApplicationContextHolder.getContext();
        FiscalEventWrapperEvent wrapperEvent = new FiscalEventWrapperEvent(this,event);
        ctx.publishEvent(wrapperEvent);
//        ctx.publishEvent(event);
    }
}
