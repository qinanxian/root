package com.vekai.batch;

import cn.fisok.sqloy.core.BeanCruder;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;

public class BatchStarterTest extends BaseTest{
    @Autowired
    protected BeanCruder beanCruder;

    @Autowired
    PlatformTransactionManager transactionManager;
    @Autowired
    JobRepository jobRepository;
    @Autowired
    JobLauncher jobLauncher;
    @Autowired
    JobExplorer jobExplorer;
    @Autowired
    JobOperator jobOperator;
    @Autowired
    StepBuilderFactory stepBuilderFactory;
    @Autowired
    JobBuilderFactory jobBuilderFactory;

    @Test
    public void pingTest(){
        Assert.assertNotNull(beanCruder);
        Assert.assertNotNull(transactionManager);
        Assert.assertNotNull(jobRepository);
        Assert.assertNotNull(jobLauncher);
        Assert.assertNotNull(jobExplorer);
        Assert.assertNotNull(jobOperator);
        Assert.assertNotNull(stepBuilderFactory);
        Assert.assertNotNull(jobBuilderFactory);
    }
}
