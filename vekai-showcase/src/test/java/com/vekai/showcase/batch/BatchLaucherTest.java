package com.vekai.showcase.batch;

import cn.fisok.raw.kit.DateKit;
import com.vekai.batch.core.BatchJobRegister;
import com.vekai.batch.core.JobWrapper;
import com.vekai.batch.service.lock.BatchLauncherLockService;
import com.vekai.showcase.BaseTest;
import org.junit.Test;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import java.util.concurrent.ScheduledFuture;

public class BatchLaucherTest extends BaseTest {

    @Autowired
    JobLauncher jobLauncher;
    @Autowired
    BatchJobRegister jobRegister;
    @Autowired
    TaskScheduler taskScheduler;
    @Autowired
    BatchLauncherLockService lockService;

    @Test
    public void personYearIncomeTest() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {

        JobWrapper jobWrapper = jobRegister.getJob("PersonYearIncomeJob");
        //执行JOB
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("execTime", DateKit.format(DateKit.now()))
                .toJobParameters();
        jobLauncher.run(jobWrapper.getJob(), jobParameters);
    }
    @Test
    public void demoPersonImportJobTest() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {

        JobWrapper jobWrapper = jobRegister.getJob("DemoPersonImportJob");
        //执行JOB
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("execTime", DateKit.format(DateKit.now()))
                .toJobParameters();
        jobLauncher.run(jobWrapper.getJob(), jobParameters);
    }

    @Test
    public void cronScheduleTest() throws InterruptedException {
        ScheduledFuture<?> future = taskScheduler.schedule(()->{
            System.out.println("------111-----");
        },new CronTrigger("0/1 * * * * ? "));


        Thread.sleep(4000);
        future.cancel(false);
        Thread.sleep(2000);
    }

    @Test
    public void testEmpty(){
        System.out.println("lock-1:"+lockService.lock("test1"));
        System.out.println("lock-2:"+lockService.lock("test1"));
        System.out.println("lock-3:"+lockService.lock("test1"));
        System.out.println("lock-4:"+lockService.lock("test1"));
        lockService.unlock("test1");
        System.out.println("lock-5:"+lockService.lock("test1"));
    }
}
