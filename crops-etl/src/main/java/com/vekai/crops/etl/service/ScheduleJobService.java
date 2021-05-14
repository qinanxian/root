package com.vekai.crops.etl.service;

import com.google.common.base.Preconditions;
import com.vekai.crops.etl.job.QuartzJobFactory;
import com.vekai.crops.etl.job.ScheduleJob;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
public class ScheduleJobService {

	@Autowired
	private Scheduler scheduler;
    @Autowired
    private ScheduleJobDBService scheduleJobDBService;
	
    public List<ScheduleJob> getAllJobList(){
        List<ScheduleJob> jobList = new ArrayList<>();  
        try {  
            GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
            Set<JobKey> jobKeySet = scheduler.getJobKeys(matcher);
            for (JobKey jobKey : jobKeySet){
                List<? extends Trigger> triggers = scheduler.getTriggersOfJob(jobKey);
                for (Trigger trigger : triggers){
                    ScheduleJob scheduleJob = new ScheduleJob();  
                    this.wrapScheduleJob(scheduleJob,scheduler,jobKey,trigger);  
                    jobList.add(scheduleJob);  
                }  
            }  
        } catch (SchedulerException e) {
        	e.printStackTrace();   
        }
        return jobList;  
    } 
	
	
    public List<ScheduleJob> getRunningJobList() throws SchedulerException {
        List<JobExecutionContext> executingJobList = scheduler.getCurrentlyExecutingJobs();
        List<ScheduleJob> jobList = new ArrayList<>(executingJobList.size());  
        for(JobExecutionContext executingJob : executingJobList){
            ScheduleJob scheduleJob = new ScheduleJob();  
            JobDetail jobDetail = executingJob.getJobDetail();
            JobKey jobKey = jobDetail.getKey();
            Trigger trigger = executingJob.getTrigger();
            this.wrapScheduleJob(scheduleJob,scheduler,jobKey,trigger);  
            jobList.add(scheduleJob);  
        }  
        return jobList;  
    }
    
    
    public void saveOrupdate(ScheduleJob scheduleJob) throws Exception {
		Preconditions.checkNotNull(scheduleJob, "job is null");
        TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobId(), scheduleJob.getJobGroup());
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);


        if(trigger == null){
			addJob(scheduleJob);
		}else {
			updateJobCronExpression(scheduleJob);
		}
	} 
    
    private void addJob(ScheduleJob scheduleJob) throws Exception{  
		Preconditions.checkNotNull(StringUtils.isEmpty(scheduleJob.getCronExp()), "CronExpression is null");

        TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobId(), scheduleJob.getJobGroup());
        CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        if(trigger != null){
            throw new Exception("job already exists!");
        }

        JobDetail jobDetail = JobBuilder.newJob(QuartzJobFactory.class).withIdentity(scheduleJob.getJobId(),scheduleJob.getJobGroup()).build();
        jobDetail.getJobDataMap().put("scheduleJob", scheduleJob);

        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExp());
        trigger = TriggerBuilder.newTrigger().withIdentity(scheduleJob.getJobId(), scheduleJob.getJobGroup()).withSchedule(cronScheduleBuilder).build();

        scheduler.scheduleJob(jobDetail, trigger);
  
    } 
    
    
    public void pauseJob(String jobId, String jobGroup) throws SchedulerException {
        JobKey jobKey = findJobKey(jobId, jobGroup);
        scheduler.pauseJob(jobKey);
        scheduleJobDBService.updateJobStatus(jobId, "PAUSED");
    }  
    
    public void resumeJob(String jobId, String jobGroup) throws SchedulerException {
        JobKey jobKey = findJobKey(jobId, jobGroup);
        scheduler.resumeJob(jobKey);
        scheduleJobDBService.updateJobStatus(jobId, "NORMAL");
    }
    
    public void deleteJob(String jobId, String jobGroup) throws SchedulerException {
        JobKey jobKey = findJobKey(jobId, jobGroup);
        scheduler.deleteJob(jobKey);  
    }
    
    public void runJobOnce(String jobId, String jobGroup) throws SchedulerException {
        JobKey jobKey = findJobKey(jobId, jobGroup);
        scheduler.triggerJob(jobKey);  
    } 
    
    
    private void updateJobCronExpression(ScheduleJob scheduleJob) throws SchedulerException {
		Preconditions.checkNotNull(StringUtils.isEmpty(scheduleJob.getCronExp()), "CronExpression is null");

        TriggerKey triggerKey = TriggerKey.triggerKey(scheduleJob.getJobId(), scheduleJob.getJobGroup());
        CronTrigger cronTrigger = (CronTrigger)scheduler.getTrigger(triggerKey);
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExp());
        cronTrigger = cronTrigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(cronScheduleBuilder).build();
        scheduler.rescheduleJob(triggerKey, cronTrigger);
    }
    
    private void wrapScheduleJob(ScheduleJob scheduleJob, Scheduler scheduler, JobKey jobKey, Trigger trigger){
        try {  
            scheduleJob.setJobName(jobKey.getName());  
            scheduleJob.setJobGroup(jobKey.getGroup()); 
  
            JobDetail jobDetail = scheduler.getJobDetail(jobKey);
            ScheduleJob job = (ScheduleJob)jobDetail.getJobDataMap().get("scheduleJob");  
            scheduleJob.setJobDesc(job.getJobDesc());
            scheduleJob.setJobId(job.getJobId());
  
            Trigger.TriggerState triggerState = scheduler.getTriggerState(trigger.getKey());
            scheduleJob.setJobStatus(triggerState.name());
            if(trigger instanceof CronTrigger){
                CronTrigger cronTrigger = (CronTrigger)trigger;
                String cronExpression = cronTrigger.getCronExpression();
                scheduleJob.setCronExp(cronExpression);
            }  
        } catch (SchedulerException e) {
            e.printStackTrace(); 
        }  
    }


    private JobKey findJobKey(String jobId, String jobGroup){
        checkNotNull(jobId, jobGroup);
        JobKey jobKey = JobKey.jobKey(jobId, jobGroup);
        return jobKey;
    }

    private void checkNotNull(String jobId, String jobGroup) {
		Preconditions.checkNotNull(StringUtils.isEmpty(jobId), "jobId is null");
		Preconditions.checkNotNull(StringUtils.isEmpty(jobGroup), "jobGroup is null");
	}
    
    
	public SchedulerMetaData getMetaData() throws SchedulerException {
		SchedulerMetaData metaData = scheduler.getMetaData();
		return metaData;
	}


}
