package com.vekai.auth.audit.aspect;

import cn.fisok.raw.lang.MapObject;
import com.vekai.auth.audit.AutoFieldProperties;
import cn.fisok.raw.kit.DateKit;
import cn.fisok.sqloy.core.MapObjectUpdater;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Iterator;

/**
 * vekai-sql模块，作数据插入时，记录数据的创建时间，更新时间
 */

@Aspect
@Component
public class MapDataUpdaterWhenDidAspect {

    @Autowired
    private AutoFieldProperties autoFieldProperties;

    @Pointcut("execution(* cn.fisok.sqloy.core.MapObjectUpdater.insert(..))")
    public void insertPointCut() {
    }
    @Pointcut("execution(* cn.fisok.sqloy.core.MapObjectUpdater.update(..))")
    public void updatePointCut() {
    }

    protected void fillProperty(Object object,String propName,Object value){
//        propName = StringKit.camelToUnderline(propName);
        if(object instanceof Iterable){
            fillBeans((Iterable)object,propName,value);    //列表集合类的处理
        }else{
            touchBeanValue(object,propName,value);
        }
    }

    protected void fillBeans(Iterable<?> iterable,String propName,Object value){
        Iterator<?> iterator = iterable.iterator();
        while(iterator.hasNext()){
            touchBeanValue(iterator.next(),propName,value);
        }
    }

    private void touchBeanValue(Object object,String name,Object value){
        if(object instanceof MapObject){
            ((MapObject)object).put(name,value);
        }
    }

    @Before("insertPointCut()")
    public void doInsertBefore(JoinPoint joinPoint){
        Object _this = joinPoint.getThis();
        Object[] args = joinPoint.getArgs();
        if(args==null||args.length<2)return;
        MapObjectUpdater updater = null;
        if(_this instanceof MapObjectUpdater){
            updater = (MapObjectUpdater)_this;
        }
        Object object = args[1];

        fillPropertyByConvert(updater,object, autoFieldProperties.getCreatedTimePropertyName(),DateKit.now());
        fillPropertyByConvert(updater,object, autoFieldProperties.getUpdatedTimePropertyName(),DateKit.now());
    }



    @Before("updatePointCut()")
    public void doUpdateBefore(JoinPoint joinPoint){
        Object _this = joinPoint.getThis();
        Object[] args = joinPoint.getArgs();
        if(args==null||args.length<2)return;
        Object object = args[1];
        MapObjectUpdater updater = null;
        if(_this instanceof MapObjectUpdater){
            updater = (MapObjectUpdater)_this;
        }
        //在更新模式下，如果更新日期为空，也给它填上去
        fillPropertyByConvert(updater,object, autoFieldProperties.getUpdatedTimePropertyName(),DateKit.now());

    }

    private void fillPropertyByConvert(MapObjectUpdater updater, Object object, String propName, Object value){
        String fieldName = propName;
//        NameConverter nameConverter = updater.getNameConverter(false);
//        if(nameConverter != null){
//            fieldName = nameConverter.getColumnName(fieldName);
//        }
        fillProperty(object,fieldName,value);
    }


}
