package com.vekai.workflow.autoconfigure;

import org.activiti.spring.SpringAsyncExecutor;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.AbstractProcessEngineAutoConfiguration;
import org.activiti.spring.boot.ActivitiProperties;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;

@EnableConfigurationProperties(WorkflowProperties.class)
@ComponentScan(basePackages = "com.vekai.workflow")
@Configuration
@AutoConfigureAfter(DataSourceTransactionManagerAutoConfiguration.class)
public class WorkflowAutoConfiguration {

    private WorkflowProperties properties;

    public WorkflowAutoConfiguration(WorkflowProperties properties) {
        this.properties = properties;
    }

    @Configuration
    @EnableConfigurationProperties(ActivitiProperties.class)
    public static class ProcessEngineConfiguration extends AbstractProcessEngineAutoConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public SpringProcessEngineConfiguration springProcessEngineConfiguration(
                DataSource dataSource,
                PlatformTransactionManager transactionManager,
                SpringAsyncExecutor springAsyncExecutor) throws IOException {

            return this.baseSpringProcessEngineConfiguration(dataSource, transactionManager, springAsyncExecutor);
        }
    }

}
