package com.vekai.batch.controller;

import cn.fisok.raw.lang.MapObject;
import com.vekai.batch.schedule.ScheduleExecutor;
import com.vekai.batch.core.BatchJobRegister;
import com.vekai.batch.core.JobWrapper;
import com.vekai.batch.entity.ApnsBatchLog;
import com.vekai.batch.entity.TimerEntity;
import com.vekai.batch.server.BatchWebSockerHolder;
import com.vekai.batch.service.BatchJobService;
import com.vekai.batch.service.lock.BatchLauncherLockService;
import com.vekai.batch.support.BatchLogDao;
import cn.fisok.raw.kit.DateKit;
import org.quartz.CronExpression;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/batch")
public class BatchController {
    @Autowired
    BatchJobRegister jobRegister;
    @Autowired
    BatchJobService batchJobService;
    @Autowired
    JobLauncher jobLauncher;
    @Autowired
    JobOperator jobOperator;
    @Autowired
    BatchLauncherLockService lockService;
    @Autowired
    BatchLogDao batchLogDao;


    @RequestMapping("/timers")
    public List<TimerEntity> getTimers(){
        return batchJobService.getTimers();
    }

    @PostMapping("/jobs/start/{jobName}")
    public void startJob(@PathVariable("jobName") String jobName) throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        JobWrapper jobWrapper = jobRegister.getJob(jobName);
        if(jobWrapper == null)return;
        //执行JOB
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("startTime", DateKit.format(DateKit.now()))
                .toJobParameters();
        ScheduleExecutor scheduleExecutor = new ScheduleExecutor(jobLauncher,lockService);
        scheduleExecutor.exec(jobWrapper,jobParameters);
        //广播一下
        BatchWebSockerHolder.sendBroadcastMessage(BatchWebSockerHolder.responseObject("start",getTimers()).jsonText());
    }
    @PostMapping("/jobs/stop/{jobName}")
    public void stopJob(@PathVariable("jobName") String jobName) throws NoSuchJobExecutionException, JobExecutionNotRunningException {
        List<MapObject> executions = batchJobService.getJobExecutions(jobName);
        for(MapObject mapRow:executions){
            Long jobExecutionId=mapRow.getValue("jobExecutionId").longValue();
            String status=mapRow.getValue("status").strValue();
            if(BatchStatus.valueOf(status) == BatchStatus.STARTING || BatchStatus.valueOf(status) == BatchStatus.STARTED){
                jobOperator.stop(jobExecutionId);
            }
        }
        //广播一下
        BatchWebSockerHolder.sendBroadcastMessage(BatchWebSockerHolder.responseObject("stop",getTimers()).jsonText());
    }

    @PostMapping("/jobs/update-cronexpr")
    public int updateConExpr(@RequestParam("jobName") String jobName,@RequestParam("cronExpr")String cronExpr){
        TimerEntity timerEntity = batchJobService.getTimerEntity(jobName);
        if(timerEntity == null)return 0;
        timerEntity.setCronExpr(cronExpr);
        int r= batchJobService.saveTimerEntity(timerEntity);

        //重新根据设置的时间，调整批量调度
        batchJobService.refreshJobSchedule(timerEntity);

        return r;
    }

    @GetMapping("/jobs/status/{jobName}")
    public void getJobStatus(@PathVariable("jobName") String jobName){
    }

    @PostMapping("/jobs/register/synchronize")
    public int registerSynchronize(){
        List<JobWrapper> jobWrappers = jobRegister.getJobs();
        return batchJobService.registerSynchronize(jobWrappers);
    }

    @PostMapping("/jobs/enable/{jobName}")
    public void enableJob(@PathVariable("jobName") String jobName){
        batchJobService.enableJob(jobName);
        //广播一下
        BatchWebSockerHolder.sendBroadcastMessage(BatchWebSockerHolder.responseObject("switchEnable",getTimers()).jsonText());
    }

    @PostMapping("/jobs/disable/{jobName}")
    public void disableJob(@PathVariable("jobName") String jobName){
        batchJobService.disableJob(jobName);
        //广播一下
        BatchWebSockerHolder.sendBroadcastMessage(BatchWebSockerHolder.responseObject("switchEnable",getTimers()).jsonText());
    }

    @PostMapping("/crontab/predictor")
    public List<Date> crontabPredict(@RequestParam("expr") String expr, @RequestParam("times") Integer times) throws ParseException {

        Date nextTime  = DateKit.now();
        List<Date> predictList = new ArrayList<>(times);
//        CronExpression cronExpression = new CronExpression("0/10 * * * * ? ");
        CronExpression cronExpression = new CronExpression(expr);
        for(int i=0;i<times;i++){
            nextTime = cronExpression.getTimeAfter(nextTime);
            predictList.add(nextTime);
        }
        return predictList;
    }

    @GetMapping("/logs/{jobId}")
    public List<ApnsBatchLog> getLogsByJobId(@PathVariable("jobId") Long jobId){
        return batchLogDao.getBatchLogList(jobId);
    }
}
