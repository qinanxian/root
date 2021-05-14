package com.vekai.batch.core;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.*;

/**
 * 批量JOB注册中心，使用单例模式
 */
public class BatchJobRegister implements BeanPostProcessor {
    private Map<String,JobWrapper> registerBook = new TreeMap<String,JobWrapper>();

    public void register(String jobName,JobWrapper job){
        registerBook.put(jobName,job);
    }

    /**
     * 取所有的JOB
     * @return
     */
    public List<JobWrapper> getJobs(){
        List<JobWrapper> jobWrappers = new ArrayList<>();
        jobWrappers.addAll(registerBook.values());
        return jobWrappers;
    }

    /**
     * 取单个JOB
     * @param jobName
     * @return
     */
    public JobWrapper getJob(String jobName){
        return registerBook.get(jobName);
    }

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof JobWrapper){
            JobWrapper jobWrapper = (JobWrapper)bean;
            register(jobWrapper.getJobName(),jobWrapper);
        }
        return bean;
    }
}
