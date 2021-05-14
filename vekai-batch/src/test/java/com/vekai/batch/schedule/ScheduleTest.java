package com.vekai.batch.schedule;

import org.junit.Test;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.scheduling.concurrent.DefaultManagedTaskScheduler;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.support.CronTrigger;

import java.text.ParseException;
import java.util.concurrent.ScheduledFuture;

public class ScheduleTest {

    @Test
    public void test1() throws ParseException, InterruptedException {
//        CronTriggerImpl trigger = new CronTriggerImpl();
//        trigger.setCronExpression("*/1 * * * * ?");
//        trigger.setJob

        //JobDetail
        MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
        /*
         *  是否并发执行
         *  例如每5s执行一次任务，但是当前任务还没有执行完，就已经过了5s了，
         *  如果此处为true，则下一个任务会执行，如果此处为false，则下一个任务会等待上一个任务执行完后，再开始执行
         */
//        ScheduleExecutor executor = new ScheduleExecutor();
//        executor.setData("data1");

        jobDetail.setConcurrent(false);
        jobDetail.setName("srd-chhliu");// 设置任务的名字
        jobDetail.setGroup("srd");// 设置任务的分组，这些属性都可以存储在数据库中，在多任务的时候使用
//        jobDetail.setTargetObject(executor);
        jobDetail.setTargetMethod("exec");

        //Trigger
        CronTriggerFactoryBean tigger = new CronTriggerFactoryBean();
        tigger.setCronExpression("*/1 * * * * ?");
        tigger.setName("trigger1");
        tigger.setJobDetail(jobDetail.getObject());



        SchedulerFactoryBean bean = new SchedulerFactoryBean();
        // 用于quartz集群,QuartzScheduler 启动时更新己存在的Job
        bean.setOverwriteExistingJobs(true);
        // 延时启动，应用启动1秒后
//        bean.setStartupDelay(1);
        // 注册触发器
        bean.setTriggers(tigger.getObject());
        bean.start();


        Thread.sleep(10000);


    }

    @Test
    public void test2() throws InterruptedException {
        DefaultManagedTaskScheduler taskScheduler = new DefaultManagedTaskScheduler();

        final ScheduledFuture<?> future = taskScheduler.schedule(new Runnable() {
            public void run() {
                System.out.println("------111-----");
            }
        },new CronTrigger("0/1 * * * * ? "));


        Thread.sleep(4000);
        future.cancel(false);
        Thread.sleep(2000);
    }
}
