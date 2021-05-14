getFiscalEventDef
===
* 查询单个财务事件定义
```sql
SELECT * 
FROM CONF_FISC_EVENT 
WHERE EVENT_DEF=:eventDef
```

getEventParamDefList
===
* 查询单个财务事件定义的参数
```sql
SELECT * 
FROM CONF_FISC_EVENT_PARAM 
WHERE EVENT_DEF=:eventDef 
```

selectEventEntryDefList
===
* 查询财务事件定义的科目分录
```sql
SELECT * 
FROM CONF_FISC_EVENT_ENTRY 
WHERE EVENT_DEF=:eventDef 
ORDER BY BOOK_CODE ASC,ENTRY_CODE ASC
```
