package com.vekai.batch.support;

import cn.fisok.sqloy.core.BeanCruder;


public abstract class LogAppenderSupportHolder {
    private static final ThreadLocal<BeanCruder> holder = new ThreadLocal<BeanCruder>();
    private static final ThreadLocal<BatchLogDao> batchLogDaoHolder = new ThreadLocal<BatchLogDao>();
    private static final ThreadLocal<Long> lastLogLineNumberHolder = new ThreadLocal<Long>();
    private static final ThreadLocal<BeanCruder> logDaoDataUpdaterHolder = new ThreadLocal<BeanCruder>();


    public static BeanCruder getbeanCruder(){
        return holder.get();
    }

    public static void setbeanCruder(BeanCruder beanCruder){
        holder.set(beanCruder);
    }

    public static BatchLogDao getBatchLogDao(){
        return batchLogDaoHolder.get();
    }

    public static void setBatchLogDao(BatchLogDao batchLogDao){
        batchLogDaoHolder.set(batchLogDao);
    }

    /**
     * 每调一次这个API就表示批次
     * @return
     */
    public static Long lastLogLineNumber(){
        Long batchNumber = lastLogLineNumberHolder.get();
        if(batchNumber == null) batchNumber = 0L;
        lastLogLineNumberHolder.set(batchNumber);
        return batchNumber;
    }
    /**
     * 每调一次这个API就表示批次
     * @return
     */
    public static void lastLogLineNumber(Long lineNumber){
        lastLogLineNumberHolder.set(lineNumber);
    }

    public static BeanCruder getLogDaobeanCruder(){
        return logDaoDataUpdaterHolder.get();
    }

    public static void setLogDaoDataUpdater(BeanCruder beanCruder){
        logDaoDataUpdaterHolder.set(beanCruder);
    }

    public static void clear(){
        holder.remove();
//        batchLogDaoHolder.remove();
        lastLogLineNumberHolder.remove();
        logDaoDataUpdaterHolder.remove();
    }
}
