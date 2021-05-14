package com.vekai.batch.service;

import cn.fisok.raw.lang.MapObject;
import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.batch.schedule.ScheduleExecutor;
import com.vekai.batch.service.lock.BatchLauncherLockService;
import com.vekai.batch.core.BatchJobRegister;
import com.vekai.batch.core.JobWrapper;
import com.vekai.batch.entity.TimerEntity;
import cn.fisok.raw.kit.DateKit;
import cn.fisok.raw.kit.ListKit;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.kit.StringKit;
import cn.fisok.sqloy.core.MapObjectCruder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import java.util.*;
import java.util.concurrent.ScheduledFuture;

//@Service
public class BatchJobService {
    public static final String BATCH_TIMER_TYPE="BatchJob";

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    BatchJobRegister jobRegister;
    @Autowired
    BeanCruder beanCruder;
    @Autowired
    MapObjectCruder mapObjectCruder;
    @Autowired
    TaskScheduler taskScheduler;
    @Autowired
    JobLauncher jobLauncher;
    @Autowired
    BatchLauncherLockService lockService;

    private Map<String,ScheduledFuture<?>> futureMap = new HashMap<>();

    /**
     * 更新注册表中注册的最新批量程序信息
     * @param jobWrappers
     * @return
     */
    public int registerSynchronize(List<JobWrapper> jobWrappers){
        Set<String> remainJobSet = new HashSet<String>();
        Set<String> clearJobSet = new HashSet<String>();
        jobWrappers.forEach(jobWrapper -> {
            remainJobSet.add(jobWrapper.getJobName());
        });

        //1.找出已经注册的JOB,并且和目前注册在内存中的JOB进行比对，如果出现了多余的，则多注册表中删除掉
        String sql = "select * from FOWK_TIMER where TIMER_TYPE=:timerType order by TIMER_KEY ASC";
        List<TimerEntity> timerEntities = beanCruder.selectList(TimerEntity.class,sql,MapKit.mapOf("timerType",BATCH_TIMER_TYPE));
        Map<String,TimerEntity> registerTimerMap = MapKit.newHashMap();
        if(timerEntities!=null){
            timerEntities.forEach(timerEntity -> {
                registerTimerMap.put(timerEntity.getTimerKey(),timerEntity);
                clearJobSet.add(timerEntity.getTimerKey());
            });
        }
        //从所有的里面把保留的移除了，剩下的就是需要被清除的
        clearJobSet.removeAll(remainJobSet);
        if(!clearJobSet.isEmpty()){
            sql = "delete from FOWK_TIMER where TIMER_KEY IN (:timeKeys)";
            List<String> clearKeyList = ListKit.newArrayList();
            clearKeyList.addAll(clearJobSet);
            beanCruder.execute(sql,
                    MapKit.mapOf("timerType",BATCH_TIMER_TYPE
                            ,"timeKeys",clearKeyList));
        }

        //2. 重新组织，更新数据表中的记录
        List<TimerEntity> insertEntites = ListKit.newArrayList();
        List<TimerEntity> updateEntites = ListKit.newArrayList();
        jobWrappers.forEach(jobWrapper -> {
            TimerEntity timerEntity = registerTimerMap.get(jobWrapper.getJobName());
            if(timerEntity==null){
                timerEntity = new TimerEntity();
                timerEntity.setTimerStatus("1");
                timerEntity.setTimerKey(jobWrapper.getJobName());
                timerEntity.setTimerName(jobWrapper.getJobTitle());
                timerEntity.setExecScript(jobWrapper.getJobName());
                timerEntity.setCronExpr(jobWrapper.getDefaultCrontab());
                timerEntity.setTimerType(BATCH_TIMER_TYPE);
                insertEntites.add(timerEntity);
            }else{
                timerEntity.setIsRunning("N");
                timerEntity.setTimerName(jobWrapper.getJobTitle());
                timerEntity.setExecScript(jobWrapper.getJobName());
                updateEntites.add(timerEntity);
            }
        });

        int r = 0;
        if(!insertEntites.isEmpty()){
            r += beanCruder.insert(insertEntites);
        }
        if(!updateEntites.isEmpty()){
            r += beanCruder.update(updateEntites);
        }

        //同步之后，重新注册调度器
        insertEntites.forEach(entry->{
            refreshJobSchedule(entry);
        });
        updateEntites.forEach(entry->{
            refreshJobSchedule(entry);
        });

        return r;

    }

    public TimerEntity getTimerEntity(String timerKey){
        String sql = "select * from FOWK_TIMER where TIMER_KEY=:timerKey";
        return beanCruder.selectOne(TimerEntity.class,sql,MapKit.mapOf("timerKey",timerKey));
    }

    public int saveTimerEntity(TimerEntity timerEntity){
        return beanCruder.save(timerEntity);
    }

    public List<MapObject> getJobExecutions(String jobName){
        StringBuffer sql = new StringBuffer();
        sql.append("select E.JOB_EXECUTION_ID,E.STATUS FROM BATCH_JOB_EXECUTION E,BATCH_JOB_INSTANCE I");
        sql.append(" where E.JOB_INSTANCE_ID=I.JOB_INSTANCE_ID ");
        sql.append(" and I.JOB_NAME=:jobName");
        sql.append(" order by E.CREATE_TIME ASC");

        return mapObjectCruder.selectList(sql.toString(),"jobName",jobName);
    }

    /**
     * 根据Batch中的Long型job-id,取Timer表中的String型jobName
     * @return
     */
    public String getJobName(Long id){
        return beanCruder.selectOne(String.class,"SELECT JOB_NAME FROM BATCH_JOB_INSTANCE WHERE JOB_INSTANCE_ID=:id","id",id);
    }

    /**
     * 刷新工作调度
     * @param timerEntity
     */
    public void refreshJobSchedule(TimerEntity  timerEntity){
        final String jobName = timerEntity.getTimerKey();
        final String cronExpr = timerEntity.getCronExpr();
        if(timerEntity == null){
            return;
        }

        final JobWrapper jobWrapper = jobRegister.getJob(jobName);
        if(jobWrapper == null||jobWrapper.getJob() == null){
            return;
        }
        //注册调度
        //先取消之前的设置
        ScheduledFuture<?> future = futureMap.get(jobName);
        if(future != null){
            future.cancel(false);
            futureMap.remove(jobName);
        }

        if(StringKit.isBlank(cronExpr))return;
        if("0".equals(timerEntity.getTimerStatus())) return ;

        logger.debug("任务{},时间表达式:{}",jobName,timerEntity.getCronExpr());
        //注册的时候把锁解开
        lockService.unlock(jobName);

        future = taskScheduler.schedule(()->{
            //执行JOB
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("startTime", DateKit.format(DateKit.now()))
                    .toJobParameters();
            ScheduleExecutor scheduleExecutor = new ScheduleExecutor(jobLauncher,lockService);
            scheduleExecutor.exec(jobWrapper,jobParameters);

        },new CronTrigger(timerEntity.getCronExpr()));
        futureMap.put(jobName,future);
    }

    public void enableJob(String jobName){
        TimerEntity  timerEntity = getTimerEntity(jobName);
        if(timerEntity == null)return;
        timerEntity.setTimerStatus("1");
        saveTimerEntity(timerEntity);
        refreshJobSchedule(timerEntity);
    }

    public void disableJob(String jobName){
        TimerEntity  timerEntity = getTimerEntity(jobName);
        if(timerEntity == null)return;
        timerEntity.setTimerStatus("0");
        saveTimerEntity(timerEntity);
        refreshJobSchedule(timerEntity);
    }

    public void refreshJobSchedule(String jobName){
        TimerEntity  timerEntity = getTimerEntity(jobName);
        refreshJobSchedule(timerEntity);
    }

    public List<TimerEntity> getTimers(){
        String sql = "select * from FOWK_TIMER ORDER BY TIMER_KEY ASC";
        return beanCruder.selectList(TimerEntity.class,sql);
    }

}
