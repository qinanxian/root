package com.vekai.batch;

import com.vekai.batch.core.BatchJobRegister;
import com.vekai.batch.core.JobWrapper;
//import com.vekai.batch.creditloan.model.TransferRepayment;
import cn.fisok.raw.kit.DateKit;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class BatchAppTest extends BaseTest {
    @Autowired
    NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Autowired
    StepBuilderFactory stepBuilderFactory;
    @Autowired
    JobBuilderFactory jobBuilderFactory;
    @Autowired
    JobLauncher jobLauncher;
    @Autowired
    BatchJobRegister jobRegister;


    @Test
    public void testLaucher() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException {
        JobWrapper jobWrapper = jobRegister.getJob("importRepaymentJob");


        //执行JOB
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("execTime", DateKit.format(DateKit.now()))
                .toJobParameters();
        jobLauncher.run(jobWrapper.getJob(), jobParameters);

    }


    @Test
    public void testRegister(){
        Assert.assertNotNull(jobRegister);
        Assert.assertNull(jobRegister.getJob("test1"));
        Assert.assertEquals("导入还款计划",jobRegister.getJob("importRepaymentJob").getJobTitle());
    }




}
