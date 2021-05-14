package com.vekai.auth.audit.aspect;

import cn.fisok.raw.lang.MapObject;
import com.vekai.auth.audit.entity.DataAuditItemEntity;
import com.vekai.auth.audit.entity.DataAuditEntity;
import com.vekai.auth.audit.types.ActionType;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.sqloy.core.BeanCruder;
import cn.fisok.sqloy.core.MapObjectCruder;
import cn.fisok.sqloy.kit.MapObjectSQLKit;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * vekai-sql模块，作数据插入时，安全审计，记录数据修改情况
 */

@Aspect
@Component
public class MapDataUpdaterAuditAspect {

    @Autowired
    protected BeanCruder beanCruder;
    @Autowired
    protected MapObjectCruder mapObjectCruder;

    @Pointcut("execution(* cn.fisok.sqloy.core.MapObjectUpdater.insert(..))")
    public void insertPointCut() {
    }
    @Pointcut("execution(* cn.fisok.sqloy.core.MapObjectUpdater.update(..))")
    public void updatePointCut() {
    }
    @Pointcut("execution(* cn.fisok.sqloy.core.MapObjectUpdater.delete(..))")
    public void deletePointCut() {
    }

    @After("insertPointCut()")
    public void doInsertAfter(JoinPoint joinPoint){
        if(!DataAuditHelper.auditEnable())return;

        Object[] args = joinPoint.getArgs();
        if(args==null||args.length<2)return;
        String tableName = (String)args[0];
        Object dataObject = args[1];


        //单个值和多值审计记录方式不一样
        if(dataObject instanceof Iterable){
            Iterable<Object> iterable = (Iterable<Object>)dataObject;
            iterable.forEach(objItem->{
                insertAuditDataByInsertAction(tableName,(MapObject)objItem);
            });
        }else{
            insertAuditDataByInsertAction(tableName,(MapObject)dataObject);
        }
    }

    private int insertAuditDataByInsertAction(String table, MapObject dataObject){
        if(dataObject == null)return 0;
        if(!DataAuditHelper.isAuditTable(table,ActionType.INSERT))return 0; //如果是一个不需要的审计对象，则直接跳过

        MapObject keyMap = DataAuditHelper.getMapDataKeyMap(dataObject);
        DataAuditEntity dataAudit = DataAuditHelper.createDataAuditByMap(ActionType.INSERT,table);
        DataAuditHelper.fillAuditEntityPrimaryKeyByMap(dataAudit,keyMap);   //把主键记录下来

        //数据项
        List<DataAuditItemEntity> itemList = DataAuditHelper.createAuditItemsByMap(dataObject);
        itemList.forEach(item->{
            item.setDataAuditId(dataAudit.getDataAuditId());
            item.setAuditItemId(StringKit.uuid());
        });

        int ret = beanCruder.insert(dataAudit);
        ret += beanCruder.insert(itemList);

        return ret;
    }

    @Around("updatePointCut()")
    public Object doUpdateAround(ProceedingJoinPoint invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        if(!DataAuditHelper.auditEnable())return invocation.proceed(args);

        if(args==null||args.length<3)return invocation.proceed(args);
        String tableName = (String)args[0];
        Object dataObject = args[1];
        Object keyObject = args[2];

        Object ret = null;
        //单个值和多值审计记录方式不一样
        if(dataObject instanceof Iterable){
            Iterable<MapObject> dataIterable = (Iterable<MapObject>)dataObject;
            Iterable<MapObject> keyIterable = (Iterable<MapObject>)keyObject;

            //把新老对象数据暂存起来
            List<MapObject> keyObjectList = new ArrayList<>();
            List<MapObject> oldObjectList = new ArrayList<>(); //存放查出来的老对象
            List<MapObject> newObjectList = new ArrayList<>();
            keyIterable.forEach(keyRow->{
                keyObjectList.add(keyRow);
            });
            dataIterable.forEach(dataRow->{
                newObjectList.add(dataRow);
            });

            keyIterable.forEach(keyMap->{
                String sqlWhere = MapObjectSQLKit.genWhereClause(keyMap,mapObjectCruder.getMapObjectUpdater().getNameConverter());
                StringBuffer sbSQL = new StringBuffer("SELECT * FROM ").append(tableName).append(" WHERE ").append(sqlWhere);
                MapObject oldDataObject = mapObjectCruder.selectOne(sbSQL.toString(),keyMap);
                oldObjectList.add(oldDataObject);
            });
            //执行更新
            ret = invocation.proceed(args);

            //一个一个的计算差异
            for(int i=0;i<newObjectList.size();i++){
                MapObject keyMap = keyObjectList.get(i);
                MapObject newDataObject = newObjectList.get(i);
                MapObject oldDataObject = oldObjectList.get(i);
                insertAuditDataByUpdateAction(tableName,keyMap,newDataObject,oldDataObject);
            }
        }else{
            MapObject newDataObject = (MapObject)dataObject;

            MapObject keyMap = (MapObject)keyObject;
            String sqlWhere = MapObjectSQLKit.genWhereClause(keyMap,mapObjectCruder.getMapObjectUpdater().getNameConverter());
            StringBuffer sbSQL = new StringBuffer("SELECT * FROM ").append(tableName).append(" WHERE ").append(sqlWhere);
            MapObject oldDataObject = mapObjectCruder.selectOne(sbSQL.toString(),keyMap);

            //执行更新
            ret = invocation.proceed(args);

            insertAuditDataByUpdateAction(tableName,keyMap,newDataObject,oldDataObject);
        }

        return ret;
    }

    private int insertAuditDataByUpdateAction(String table, MapObject keyMap, MapObject newDataObject, MapObject oldDataObject){
        if(newDataObject == null)return 0;
        if(!DataAuditHelper.isAuditTable(table,ActionType.UPDATE))return 0; //如果是一个不需要的审计对象，则直接跳过

        DataAuditEntity dataAudit = DataAuditHelper.createDataAuditByMap(ActionType.UPDATE,table);
        DataAuditHelper.fillAuditEntityPrimaryKeyByMap(dataAudit,keyMap);   //把主键记录下来

        //数据项
        List<DataAuditItemEntity> itemList = DataAuditHelper.createDiffAuditItemsByMap(newDataObject,oldDataObject);
        //如果没有差异，则不要记录了
        if(itemList.isEmpty())return 0;
        itemList.forEach(item->{
            item.setDataAuditId(dataAudit.getDataAuditId());
            item.setAuditItemId(StringKit.uuid());
        });

        int ret = beanCruder.insert(dataAudit);
        ret += beanCruder.insert(itemList);

        return ret;
    }

    @After("deletePointCut()")
    public void doDeleteAfter(JoinPoint joinPoint) {
        if(!DataAuditHelper.auditEnable())return;

        Object[] args = joinPoint.getArgs();
        if(args==null||args.length<2)return;
        String tableName = (String)args[0];
        Object dataObject = args[1];


        //单个值和多值审计记录方式不一样
        if(dataObject instanceof Iterable){
            Iterable<MapObject> iterable = (Iterable<MapObject>)dataObject;
            iterable.forEach(objItem->{
                insertAuditDataByDeleteAction(tableName,objItem);
            });
        }else{
            insertAuditDataByDeleteAction(tableName,(MapObject)dataObject);
        }

    }

    private int insertAuditDataByDeleteAction(String table, MapObject keyMap){

        if(keyMap == null)return 0;
        if(!DataAuditHelper.isAuditTable(table,ActionType.DELETE))return 0; //如果是一个不需要的审计对象，则直接跳过

        DataAuditEntity dataAudit = DataAuditHelper.createDataAuditByMap(ActionType.DELETE,table);
        DataAuditHelper.fillAuditEntityPrimaryKeyByMap(dataAudit,keyMap);   //把主键记录下来

        int ret = beanCruder.insert(dataAudit);

        return ret;
    }





}
