package com.vekai.auth.audit.aspect;

import cn.fisok.raw.lang.MapObject;
import com.vekai.auth.audit.AutoFieldProperties;
import com.vekai.auth.entity.User;
import com.vekai.auth.holder.AuthHolder;
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
public class MapDataUpdaterWhoDidAspect {
    @Autowired
    protected AutoFieldProperties autoFieldProperties;

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
        User user = AuthHolder.getUser();
        if(user == null)return;

        Object[] args = joinPoint.getArgs();
        if(args==null||args.length<2)return;
        Object object = args[1];

        if(user != null){
            fillProperty(object, autoFieldProperties.getCreatedByPropertyName(),user.getId());
            fillProperty(object, autoFieldProperties.getUpdatedByPropertyName(),user.getId());
        }
    }



    @Before("updatePointCut()")
    public void doUpdateBefore(JoinPoint joinPoint){
        User user = AuthHolder.getUser();
        if(user == null)return;

        Object[] args = joinPoint.getArgs();
        if(args==null||args.length<2)return;
        Object object = args[1];
        // 在更新模式下，如果创建人为空，也给它填上去
        // 暂时无法判断，创建人是否为空，代码暂且注释
//        Object v = BeanKit.getPropertyValue(object, auditProperties.getCreatedByPropertyName());
//        if(v==null){
//            fillProperty(object, auditProperties.getCreatedByPropertyName(),user.getId());
//        }
        fillProperty(object, autoFieldProperties.getUpdatedByPropertyName(),user.getId());

    }


}
