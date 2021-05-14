package com.vekai.appframe.base.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;

/**
 * 在DataForm中，用户ID转用户名
 */
//@Aspect
//@Component
public class DataFormUserNameAspect {

    /**
     * 列表查询处理结果中的用户ID部分
     * @param joinPoint
     * @param retData
     */
    @AfterReturning(pointcut = "execution(* com.vekai.dataform.handler.DataListHandler+.query(..))", returning = "retData")
    public void doAfterListQuery(JoinPoint joinPoint, Object retData){

    }

    /**
     * 列表查询处理结果中的用户ID部分
     * @param joinPoint
     * @param retData
     */
    @AfterReturning(pointcut = "execution(* com.vekai.dataform.handler.DataOneHandler+.query(..))", returning = "retData")
    public void doAfterInfoQuery(JoinPoint joinPoint, Object retData){
    }


}
