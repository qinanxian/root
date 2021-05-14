package com.vekai.batch.schedule;


import com.vekai.batch.core.BatchException;
import com.vekai.batch.core.JobWrapper;
import com.vekai.batch.service.lock.BatchLauncherLockService;
import cn.fisok.raw.kit.LogKit;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

//@EnableScheduling
public class ScheduleExecutor {
    JobLauncher jobLauncher;
    BatchLauncherLockService lockService;

    public ScheduleExecutor(JobLauncher jobLauncher, BatchLauncherLockService lockService) {
        this.jobLauncher = jobLauncher;
        this.lockService = lockService;
    }

    public void exec(JobWrapper jobWrapper, JobParameters jobParameters){
        String jobName = jobWrapper.getJobName();
        if(!lockService.lock(jobName))throw new BatchException("任务["+jobName+"]启动失败，已经有相同的任务在运行中");   //如果没有获取到锁，则不能启动任务

        try {
            jobLauncher.run(jobWrapper.getJob(), jobParameters);
        } catch (JobExecutionAlreadyRunningException e) {
            LogKit.error("",e);
        } catch (JobRestartException e) {
            LogKit.error("",e);
        } catch (JobInstanceAlreadyCompleteException e) {
            LogKit.error("",e);
        } catch (JobParametersInvalidException e) {
            LogKit.error("",e);
        } catch (Exception e){
            LogKit.error("",e);
        }finally {
            lockService.unlock(jobName);
        }
    }


}
