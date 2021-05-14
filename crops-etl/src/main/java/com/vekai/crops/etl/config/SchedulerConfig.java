package com.vekai.crops.etl.config;

import com.vekai.crops.etl.job.QuartzJobFactory;
import com.vekai.crops.etl.job.ScheduleJob;
import com.vekai.crops.etl.service.ScheduleJobDBService;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;


@Configuration
@DependsOn
public class SchedulerConfig {
    @Autowired
    private ScheduleJobDBService scheduleJobDBService;

    @Bean
    public JobFactory jobFactory(ApplicationContext applicationContext)
    {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }

    @Bean
    public Scheduler schedulerFactoryBean(DataSource dataSource, JobFactory jobFactory) throws Exception {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();

        factory.setOverwriteExistingJobs(true);
        factory.setDataSource(dataSource);

        factory.setJobFactory(jobFactory);

        factory.setQuartzProperties(quartzProperties());
        factory.afterPropertiesSet();

        Scheduler scheduler = factory.getScheduler();
        scheduler.setJobFactory(jobFactory);

        /**
         * 每次启动时清空Quartz中所有的任务
         */
        GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
        Set<JobKey> jobKeySet = scheduler.getJobKeys(matcher);
        List<JobKey> deleteJobKeys = new ArrayList<>(jobKeySet);
        scheduler.deleteJobs(deleteJobKeys);

        // 将所有的任务设置到Quartz中
        List<ScheduleJob> jobs = scheduleJobDBService.getAllJob();
        for (ScheduleJob job : jobs) {
            // 查找触发器Trigger是否存在
        	TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobId(), job.getJobGroup());
        	CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

        	// 有则更新 无则创建
        	if (null == trigger) {
        	    // 整个系统只有一个任务定义QuartzJobFactory
        		JobDetail jobDetail = JobBuilder.newJob(QuartzJobFactory.class)
        				.withIdentity(job.getJobId(), job.getJobGroup())
        				.withDescription(job.getJobDesc()).build();
        		// 将自定义的scheduleJob放入JobDetail
        		jobDetail.getJobDataMap().put("scheduleJob", job);
        		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job
        			.getCronExp());
        		trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobId(), job.getJobGroup()).withSchedule(scheduleBuilder).build();
        		scheduler.scheduleJob(jobDetail, trigger);
			}else {
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job
					.getCronExp());
				trigger = trigger.getTriggerBuilder().withIdentity(triggerKey)
					.withSchedule(scheduleBuilder).build();

				scheduler.rescheduleJob(triggerKey, trigger);
			}
		}

        scheduler.start();
        return scheduler;
    }

    @Bean
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }

}
