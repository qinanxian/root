package com.vekai.showcase.batch;

import cn.fisok.raw.kit.DateKit;
import cn.fisok.raw.kit.LogKit;
import com.vekai.batch.core.BatchJobRegister;
import com.vekai.batch.core.JobWrapper;
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

public class TenpayBatchCaseLaucher extends BaseTest {

    @Autowired
    JobLauncher jobLauncher;
    @Autowired
    BatchJobRegister jobRegister;

    @Test
    public void impawnProductFileTest() {
        JobWrapper jobWrapper = jobRegister.getJob("impawnProductFileJob");
        //执行JOB
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("execTime", DateKit.format(DateKit.now()))
                .toJobParameters();

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
        }
    }

    @Test
    public void impawnProductFileFromBuilderTest() {
        JobWrapper jobWrapper = jobRegister.getJob("impawnProductFileJobFromBuilder-remove");
        //执行JOB
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("execTime", DateKit.format(DateKit.now()))
                .toJobParameters();

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
        }
    }

}
