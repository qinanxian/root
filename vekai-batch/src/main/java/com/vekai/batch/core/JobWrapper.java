package com.vekai.batch.core;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersIncrementer;
import org.springframework.batch.core.JobParametersValidator;

public class JobWrapper implements Job {

    private Job job;
    private String jobName;
    private String jobTitle;
    private String defaultCrontab;

    public JobWrapper(Job job){
        this.job = job;
        setJobName(job.getName());
    }

    public String getJobName() {
        return jobName;
    }

    public JobWrapper setJobName(String jobName) {
        this.jobName = jobName;
        return this;
    }

    public String getDefaultCrontab() {
        return defaultCrontab;
    }

    public JobWrapper setDefaultCrontab(String defaultCrontab) {
        this.defaultCrontab = defaultCrontab;
        return this;
    }

    public Job getJob() {
        return job;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public JobWrapper setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
        return this;
    }

    @Override
    public String getName() {
        return job.getName();
    }

    @Override
    public boolean isRestartable() {
        return job.isRestartable();
    }

    @Override
    public void execute(JobExecution execution) {
        job.execute(execution);
    }

    @Override
    public JobParametersIncrementer getJobParametersIncrementer() {
        return job.getJobParametersIncrementer();
    }

    @Override
    public JobParametersValidator getJobParametersValidator() {
        return job.getJobParametersValidator();
    }
}
