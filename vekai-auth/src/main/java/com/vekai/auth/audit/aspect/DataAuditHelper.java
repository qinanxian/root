package com.vekai.auth.audit.aspect;

import cn.fisok.raw.kit.*;
import cn.fisok.raw.lang.MapObject;
import cn.fisok.web.kit.HttpKit;
import com.vekai.auth.autoconfigure.AuditProperties;
import com.vekai.auth.autoconfigure.AuthProperties;
import com.vekai.auth.audit.entity.DataAuditEntity;
import com.vekai.auth.audit.entity.DataAuditItemEntity;
import com.vekai.auth.entity.User;
import com.vekai.auth.holder.AuditHolder;
import com.vekai.auth.holder.AuthHolder;
import com.vekai.auth.audit.types.ActionType;
import cn.fisok.raw.holder.ApplicationContextHolder;
import cn.fisok.raw.lang.ValueObject;

import java.util.*;
import java.util.regex.Pattern;

public class DataAuditHelper {
    private static int STR_MAX_LEN = 3000;
    /**
     * 是否审计数据对象自己本身
     * @param object
     * @return
     */
    public static boolean isAuditDataSelf(Object object){
        if(object instanceof DataAuditEntity)return true;          //审计对象自己的插入，不审计，否则：死递归
        if(object instanceof DataAuditItemEntity)return true;      //审计对象自己的插入，不审计，否则：死递归
        return false;
    }

    private static DataAuditEntity createDataAudit(ActionType actionType){
        DataAuditEntity dataAudit = new DataAuditEntity();

        dataAudit.setRequestId(AuditHolder.getRequestId());
        dataAudit.setSessionId(AuditHolder.getSessionId());
        dataAudit.setActionType(actionType.toString());
        dataAudit.setActionSummary(AuditHolder.getActionSummary());
        dataAudit.setOriginLocation(AuditHolder.getOriginLocation());
        dataAudit.setDataAuditId(StringKit.uuid());
        dataAudit.setClientAddress(cn.fisok.web.kit.HttpKit.getClientAddr());
        dataAudit.setUserAgent(cn.fisok.web.kit.HttpKit.getUserAgent());
        dataAudit.setContextPath(cn.fisok.web.kit.HttpKit.getContextPath());
        dataAudit.setRequestUri(cn.fisok.web.kit.HttpKit.getRequestURI());
        dataAudit.setRequestQueryString(HttpKit.getRequestQueryString());
        dataAudit.setHostAddress(NetKit.getHostAddress());
        dataAudit.setHostName(NetKit.getHostName());

        //记录下操作人
        User user = AuthHolder.getUser();
        String userId = user!=null?user.getId():"system-auto";
        dataAudit.setRevision(1);
        dataAudit.setCreatedBy(userId);
        dataAudit.setCreatedTime(DateKit.now());
        dataAudit.setUpdatedBy(userId);
        dataAudit.setUpdatedTime(DateKit.now());

        return dataAudit;

    }

    /**
     * 把BEAN中插入的数据记录下来
     * @param actionType
     * @param object
     * @return
     */
    public static DataAuditEntity createDataAuditByBean(ActionType actionType, Object object) {
        DataAuditEntity dataAudit = createDataAudit(actionType);
        dataAudit.setDataTable(JpaKit.getTableName(object.getClass()));
        dataAudit.setDataClass(object.getClass().getName());
        //记录主键信息
        fillAuditEntityPrimaryKey(dataAudit, object);

        return dataAudit;
    }

    /**
     * 把MAP中插入的数据记录下来
     * @param actionType
     * @param table
     * @return
     */
    public static DataAuditEntity createDataAuditByMap(ActionType actionType, String table){
        DataAuditEntity dataAudit = createDataAudit(actionType);
        dataAudit.setDataTable(table);
        return dataAudit;
    }

    public static void fillAuditEntityPrimaryKey(DataAuditEntity dataAudit,Object object){
        //把主键记录下来,主键，最多取四个
        Map<String,String> keyMap = JpaKit.getIdMappedFields(object.getClass());

        Iterator<String> keyIterator = keyMap.keySet().iterator();
        int cursor = 0;
        while(keyIterator.hasNext()&&cursor<5){
            String column = keyIterator.next();
            String field = keyMap.get(column);
            ValueObject valueObject = ValueObject.valueOf(BeanKit.getPropertyValue(object,field));

            setDataAuditPrimaryKeyAndValue(dataAudit,cursor,column,valueObject);

            cursor ++;

        }
    }

    public static void fillAuditEntityPrimaryKeyByMap(DataAuditEntity dataAudit, MapObject keyMap){
        //把主键记录下来,主键，最多取四个
        Iterator<String> keyIterator = keyMap.keySet().iterator();
        int cursor = 0;
        while(keyIterator.hasNext()&&cursor<5){
            String field = keyIterator.next();
            String column = StringKit.camelToUnderline(field);
            ValueObject valueObject = keyMap.getValue(field);

            setDataAuditPrimaryKeyAndValue(dataAudit,cursor,column,valueObject);

            cursor ++;

        }
    }

    private static void setDataAuditPrimaryKeyAndValue(DataAuditEntity dataAudit,int cursor,String column,ValueObject valueObject){
        switch (cursor){
            case 0:
                dataAudit.setPrimaryKey(column);
                dataAudit.setPrimaryKeyValue(valueObject.strValue());
                break;
            case 1:
                dataAudit.setPrimaryKey1(column);
                dataAudit.setPrimaryKey1Value(valueObject.strValue());
                break;
            case 2:
                dataAudit.setPrimaryKey2(column);
                dataAudit.setPrimaryKey2Value(valueObject.strValue());
                break;
            case 3:
                dataAudit.setPrimaryKey3(column);
                dataAudit.setPrimaryKey3Value(valueObject.strValue());
                break;
            case 4:
                dataAudit.setPrimaryKey4(column);
                dataAudit.setPrimaryKey4Value(valueObject.strValue());
                break;
        }
    }



    public static AuditProperties getAuditProperties(){
        AuthProperties authProperties = ApplicationContextHolder.getBean(AuthProperties.class);
        return authProperties.getAudit();
    }

    public static boolean auditEnable(){
        AuditProperties auditProperties = getAuditProperties();
        return auditProperties.isEnable();
    }

    /**
     * 根据AuditProperties的设置，判断某个表是否需要被审计
     * @param tableName
     * @param actionType
     * @return
     */
    public static boolean isAuditTable(String tableName,ActionType actionType){
        AuditProperties auditProperties = getAuditProperties();
        Map<String,String> actionSetting = auditProperties.getActionSetting();

        //禁用的表直接不是审计对象
        List<String> disableTables = auditProperties.getDisableTables();
        if(null==disableTables||disableTables.contains(tableName))return false;

        if(null==actionSetting||actionSetting.isEmpty()) return false;
        Iterator<String> iterator = actionSetting.keySet().iterator();
        while (iterator.hasNext()){
            String expr = iterator.next();
            String action = actionSetting.get(expr);
            Pattern pattern = Pattern.compile(expr);
            if(pattern.matcher(tableName).find() && action.indexOf(actionType.name()) >= 0){
                return true;
            }
        }
        return false;
    }

    public static boolean isAuditObject(Class<?> classType,ActionType actionType){
        String table = JpaKit.getTableName(classType);
        return isAuditTable(table,actionType);
    }
    public static boolean isIgnoreColumn(String column){
        AuditProperties auditProperties = getAuditProperties();
        Set<String> columns = new HashSet<>();
        columns.addAll(auditProperties.getIgonreColumns());
        return columns.contains(column)||columns.contains(StringKit.camelToUnderline(column));
    }
    public static boolean isIgnoreField(String field){
        return isIgnoreColumn(StringKit.camelToUnderline(field));
    }

//    public static DataAuditItemEntity lookupItemByField(List<DataAuditItemEntity> itemEntities,String field){
//        for(DataAuditItemEntity item: itemEntities){
//            if(field.equals(item.getItemCode()))return item;
//        }
//        return null;
//    }
//
//    public static DataAuditItemEntity lookupItemByColumn(List<DataAuditItemEntity> itemEntities,String column){
//        for(DataAuditItemEntity item: itemEntities){
//            if(column.equals(item.getColumnName()))return item;
//        }
//        return null;
//    }

    /**
     * 创建数据对象
     * @param object
     * @return
     */
    public static List<DataAuditItemEntity> createAuditItemsByBean(final Object object){
        Map<String,String> mappedFields = null;
        mappedFields = JpaKit.getMappedFields(object.getClass());    //取得字段名和属性名之间的映射关系

        List<DataAuditItemEntity> itemList = new ArrayList<>(mappedFields.size());

        mappedFields.forEach((column, field) -> {
            if(isIgnoreField(field))return; //如果不需要检查的字段，则忽略它
            DataAuditItemEntity item = new DataAuditItemEntity();
            ValueObject value = ValueObject.valueOf(BeanKit.getPropertyValue(object,field));
            if(value.isNull()||value.isBlank()||value.isEmpty())return; //空的字段，就不要记了
            fillItem(item,column,field,value);
            itemList.add(item);
        });

        return itemList;
    }

    /**
     * 创建数据对象
     * @return
     */
    public static List<DataAuditItemEntity> createAuditItemsByMap(final MapObject mapObject){
        List<DataAuditItemEntity> itemList = new ArrayList<>(mapObject.size());

        mapObject.forEach((field, value) -> {
            if(isIgnoreColumn(field))return; //如果不需要检查的字段，则忽略它
            DataAuditItemEntity item = new DataAuditItemEntity();

            ValueObject valueObject = mapObject.getValue(field);
            if(valueObject.isNull()||valueObject.isBlank()||valueObject.isEmpty())return; //空的字段，就不要记了
            String column = StringKit.camelToUnderline(field);
            fillItem(item,column,field,valueObject);
            itemList.add(item);
        });

        return itemList;
    }

    public static List<DataAuditItemEntity> createDiffAuditItemsByBean(final Object newObject,final Object oldObject){
        Map<String,String> mappedFields = null;
        mappedFields = JpaKit.getMappedFields(newObject.getClass());    //取得字段名和属性名之间的映射关系
        List<DataAuditItemEntity> itemList = new ArrayList<>(mappedFields.size());

        mappedFields.forEach((column, field) -> {
            if(isIgnoreField(field))return; //如果不需要检查的字段，则忽略它
            DataAuditItemEntity item = new DataAuditItemEntity();
            ValueObject value1 = ValueObject.valueOf(BeanKit.getPropertyValue(newObject,field));
            ValueObject value2 = ValueObject.valueOf(BeanKit.getPropertyValue(oldObject,field));
            //不相同的时候，存放差异
            if(!value1.strValue("").equals(value2.strValue(""))){
                fillDiffItem(item,column,field,value1,value2);
                itemList.add(item);
            }
        });

        return itemList;
    }

    public static List<DataAuditItemEntity> createDiffAuditItemsByMap(final MapObject newObject, final MapObject oldObject){
        //取字段名称的并集
        Set<String> fieldList = new LinkedHashSet<String>();
        if(newObject!=null)fieldList.addAll(newObject.keySet());
        if(oldObject!=null)fieldList.addAll(oldObject.keySet());

        List<DataAuditItemEntity> itemList = new ArrayList<>(fieldList.size());
        fieldList.forEach(field->{
            if(isIgnoreField(field))return; //如果不需要检查的字段，则忽略它
            String column = StringKit.camelToUnderline(field);
            DataAuditItemEntity item = new DataAuditItemEntity();
            ValueObject value1 = newObject==null?ValueObject.valueOf(""):newObject.getValue(field);
            ValueObject value2 = oldObject==null?ValueObject.valueOf(""):oldObject.getValue(field);
            //不相同的时候，存放差异
            if(!value1.strValue("").equals(value2.strValue(""))){
                fillDiffItem(item,column,field,value1,value2);
                itemList.add(item);
            }

        });

        return itemList;
    }


    public static void fillAuditItemByMap(DataAuditItemEntity item,Map<String,Object> object){

        object.forEach((column, field) -> {
            ValueObject value = ValueObject.valueOf(object.get(column));
            fillItem(item,column,column,value);
        });
    }

    public static Map<String, Map<String, String>> getDictMap(){
        Map<String,Object> auditProperties = AuditHolder.getProperies();
        if(auditProperties == null) return null;
        Map<String, Map<String, String>> dictMap = (Map<String, Map<String, String>>)auditProperties.get("dictMap");
        return dictMap;
    }

    public static Map<String, String> getNameMap(){
        Map<String,Object> auditProperties = AuditHolder.getProperies();
        if(auditProperties == null) return null;
        Map<String, String> nameMap = (Map<String, String>)auditProperties.get("nameMap");
        return nameMap;
    }

    public static String getName(String column, String code){
        Map<String, String> nameMap = getNameMap();
        if(nameMap == null || nameMap.size()==0)return null;
        String name = nameMap.get(code);
        if(StringKit.isBlank(name))name = nameMap.get(column);
        return name;
    }

    public static List<String> getMapDataPrimaryKeyCodeList(){
        Map<String,Object> auditProperties = AuditHolder.getProperies();
        if(auditProperties == null) return null;
        List<String> primaryKeyCodeList = (List<String>)auditProperties.get("primaryKeyCodeList");
        return primaryKeyCodeList;
    }

    public static MapObject getMapDataKeyMap(MapObject object){
        MapObject mapObject = new MapObject();

        List<String> pkCodeList = getMapDataPrimaryKeyCodeList();
        pkCodeList.forEach(pkCode->{
            mapObject.put(pkCode,object.getValue(pkCode).strValue());
        });

        return mapObject;
    }

    private static String getDictExplainText(String column, String code, String strValue) {
        if (StringKit.isBlank(strValue)) return "";

        String explainText = "";
        Map<String, Map<String, String>> dictMap = getDictMap();
        if(dictMap == null || dictMap.size()==0)return "";
        if (dictMap != null && dictMap.size() > 0) {
            Map<String, String> kvMap = dictMap.get(code);
            if (kvMap == null) kvMap = dictMap.get(column);


            if (kvMap != null && kvMap.size() > 0) {
                List<String> namesList = ListKit.newArrayList();
                String[] values = strValue.split(",");
                for (String v : values) {
                    String name = kvMap.get(v);
                    if (StringKit.isBlank(name)) name = v;
                    namesList.add(name);
                }
                return StringKit.join(namesList, ",");
            }
        }
        explainText = StringKit.nvl(explainText,strValue);

        return explainText;

    }

    private static void fillItem(DataAuditItemEntity item,String column,String code,ValueObject value){
        String itemName = StringKit.nvl(getName(column,code),"");
        item.setItemName(itemName);
        item.setColumnName(column);
        item.setItemCode(code);
        item.setDataType(value.getValueType().toString());
        item.setValueStr(StringKit.maxLenLimit(value.strValue(),STR_MAX_LEN));
        item.setValueExplain(StringKit.maxLenLimit(getDictExplainText(column,code,value.strValue()),STR_MAX_LEN));
//        Map<String, Map<String, String>> dictMap = getDictMap();


        switch (value.getValueType()){
            case Number:
            case Double:
            case Integer:
            case Long:
                item.setValueNumber(value.doubleValue());break;
            case Date:
                item.setValueDate(value.dateValue());
        }
    }

    private static void fillDiffItem(DataAuditItemEntity item,String column,String code,ValueObject newValue,ValueObject oldValue){
        fillItem(item,column,code,newValue);
        item.setValueStrOld(StringKit.maxLenLimit(oldValue.strValue(),STR_MAX_LEN));
        item.setValueExplainOld(StringKit.maxLenLimit(getDictExplainText(column,code,oldValue.strValue()),STR_MAX_LEN));
        switch (newValue.getValueType()){
            case Number:
            case Double:
            case Integer:
            case Long:
                item.setValueNumberOld(oldValue.doubleValue());break;
            case Date:
                item.setValueDateOld(oldValue.dateValue());
        }
    }

}
