package com.vekai.showcase;

import com.vekai.batch.core.BatchJobRegister;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class AppTest extends BaseTest {
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
    public void testRegister(){
        Assert.assertNotNull(jobRegister);
        Assert.assertNull(jobRegister.getJob("EmptyObject"));
        Assert.assertNotNull(jobRegister.getJob("PersonYearIncomeJob"));
        String title = jobRegister.getJob("PersonYearIncomeJob").getJobTitle();
        Assert.assertEquals("年收入计算",title);
    }




}
