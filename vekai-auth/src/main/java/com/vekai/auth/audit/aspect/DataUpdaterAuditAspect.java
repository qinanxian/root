package com.vekai.auth.audit.aspect;

import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.auth.audit.entity.DataAuditItemEntity;
import com.vekai.auth.audit.entity.DataAuditEntity;
import com.vekai.auth.audit.types.ActionType;
import cn.fisok.raw.kit.JpaKit;
import cn.fisok.raw.kit.StringKit;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * vekai-sql模块，作数据插入时，安全审计，记录数据修改情况
 */

@Aspect
@Component
public class DataUpdaterAuditAspect {
    @Autowired
    protected BeanCruder beanCruder;

    @Pointcut("execution(* cn.fisok.sqloy.core.BeanUpdater.insert(..))")
    public void insertPointCut() {
    }
    @Pointcut("execution(* cn.fisok.sqloy.core.BeanUpdater.update(..))")
    public void updatePointCut() {
    }
    @Pointcut("execution(* cn.fisok.sqloy.core.BeanUpdater.delete(..))")
    public void deletePointCut() {
    }

    @After("insertPointCut()")
    public void doInsertAfter(JoinPoint joinPoint){
        if(!DataAuditHelper.auditEnable())return;
        Object[] args = joinPoint.getArgs();
        if(args==null||args.length<1)return;
        Object object = args[0];


        //单个值和多值审计记录方式不一样
        if(object instanceof Iterable){
            Iterable<Object> iterable = (Iterable<Object>)object;
            iterable.forEach(objItem->{
                insertAuditDataByInsertAction(objItem);
            });
        }else{
            insertAuditDataByInsertAction(object);
        }

    }

    private int insertAuditDataByInsertAction(Object object){
        if(object == null)return 0;
        if(DataAuditHelper.isAuditDataSelf(object))return 0;
        if(!DataAuditHelper.isAuditObject(object.getClass(),ActionType.INSERT))return 0; //如果是一个不需要的审计对象，则直接跳过

        DataAuditEntity dataAudit = DataAuditHelper.createDataAuditByBean(ActionType.INSERT,object);

        //数据项
        List<DataAuditItemEntity> itemList = DataAuditHelper.createAuditItemsByBean(object);
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

        if(args==null||args.length<1)return invocation.proceed(args);
        Object object = args[0];
        if(object == null) return invocation.proceed(args);

        Object ret = null;
        //单个值和多值审计记录方式不一样
        if(object instanceof Iterable){
            //1.查出修改前的对象
            //如果是列表更新，则把列表中的每一个老对象都要查出来
            Iterable<Object> iterable = (Iterable<Object>)object;
            List<Object> oldObjectList = new ArrayList<>();
            List<Object> newObjectList = new ArrayList<>();
            iterable.forEach(objItem->{
                Object oldObject = selectOldObject(objItem);
                oldObjectList.add(oldObject);
                newObjectList.add(objItem);
            });
            //执行更新
            ret = invocation.proceed(args);

            //一个一个的计算差异
            for(int i=0;i<newObjectList.size();i++){
                insertAuditDataByUpdatetAction(newObjectList.get(i),oldObjectList.get(i));
            }
        }else{
            //1.查出修改前的对象
            Object oldObject = selectOldObject(object);
            ret = invocation.proceed(args);
            insertAuditDataByUpdatetAction(object,oldObject);
        }

        return ret;


    }


    private Object selectOldObject(Object object){
        Map<String,Object> keyMapData = JpaKit.getIdMap(object);
        Object oldObject = beanCruder.selectOneById(object.getClass(),keyMapData);
        return oldObject;
    }

    private int insertAuditDataByUpdatetAction(Object newObject,Object oldObject){
        if(newObject == null || oldObject == null)return 0;
        if(DataAuditHelper.isAuditDataSelf(newObject))return 0;
        if(!DataAuditHelper.isAuditObject(newObject.getClass(),ActionType.UPDATE))return 0; //如果是一个不需要的审计对象，则直接跳过
         if(DataAuditHelper.isAuditDataSelf(oldObject))return 0;
        if(!DataAuditHelper.isAuditObject(oldObject.getClass(),ActionType.UPDATE))return 0; //如果是一个不需要的审计对象，则直接跳过

        DataAuditEntity dataAudit = DataAuditHelper.createDataAuditByBean(ActionType.UPDATE,newObject);

        //数据项
        List<DataAuditItemEntity> itemList = DataAuditHelper.createDiffAuditItemsByBean(newObject,oldObject);
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
        if (args == null || args.length < 1) return;
        Object object = args[0];

        //单个值和多值审计记录方式不一样
        if(object instanceof Iterable){
            Iterable<Object> iterable = (Iterable<Object>)object;
            iterable.forEach(objItem->{
                insertAuditDataByDeleteAction(objItem);
            });
        }else{
            insertAuditDataByDeleteAction(object);
        }
    }

    private int insertAuditDataByDeleteAction(Object object){
        if(object == null)return 0;
        if(DataAuditHelper.isAuditDataSelf(object))return 0;
        if(!DataAuditHelper.isAuditObject(object.getClass(),ActionType.DELETE))return 0; //如果是一个不需要的审计对象，则直接跳过

        DataAuditEntity dataAudit = DataAuditHelper.createDataAuditByBean(ActionType.DELETE,object);
        int ret = beanCruder.insert(dataAudit);

        return ret;
    }


}
