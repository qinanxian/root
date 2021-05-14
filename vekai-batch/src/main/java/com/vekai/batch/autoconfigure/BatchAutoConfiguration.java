package com.vekai.batch.autoconfigure;


import cn.fisok.raw.holder.ApplicationContextHolder;
import cn.fisok.sqloy.autoconfigure.SqloyProperties;
import com.vekai.batch.listener.BatchLoggerMonitorListener;
import com.vekai.batch.support.impl.BatchLogDaoRedisImpl;
import com.vekai.batch.core.BatchJobRegister;
import com.vekai.batch.core.JobWrapper;
import com.vekai.batch.service.BatchJobService;
import com.vekai.batch.service.lock.BatchLauncherLockService;
import com.vekai.batch.service.lock.impl.BatchLauncherLockServiceByCache;
import com.vekai.batch.support.BatchLogDao;
import com.vekai.batch.support.impl.BatchLogDaoDBImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.DefaultManagedTaskScheduler;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
@EnableConfigurationProperties(BatchProperties.class)
@ComponentScan(basePackages = {"com.vekai.batch"})
@EnableBatchProcessing // 开启批处理
public class BatchAutoConfiguration {
    private static Logger logger = LoggerFactory.getLogger(BatchAutoConfiguration.class);

    private BatchProperties properties;
    @Autowired
    protected SqloyProperties sqloyProperties;

    public BatchAutoConfiguration(BatchProperties properties) {
        this.properties = properties;
    }


    /**
     * 事务管理器
     * @return
     */
    @ConditionalOnMissingBean
//    @Bean("batchTransactionManager")
    public PlatformTransactionManager getTransactionManager(){
        ResourcelessTransactionManager transactionManager = new ResourcelessTransactionManager();
        return transactionManager;

    }

    /**
     * 任务仓库
     * @param dataSource
     * @param transactionManager
     * @return
     * @throws Exception
     */
//    @Bean("jobRepository")
//    public JobRepository jobRepository(DataSource dataSource, PlatformTransactionManager transactionManager)
//            throws Exception {
//        JobRepositoryFactoryBean jobRepository = new JobRepositoryFactoryBean();
//        jobRepository.setDataSource(dataSource);
//        jobRepository.setTransactionManager(transactionManager);
//        jobRepository.setDatabaseType(sqloyProperties.getSqlDialectType().toString().toLowerCase());
//        jobRepository.setMaxVarCharLength(2000);
//        jobRepository.setIsolationLevelForCreate("ISOLATION_READ_COMMITTED");   //事务级别，不能使用默认的，否则会报ORA-08177: 无法连续访问此事务处理
//
//        return jobRepository.getObject();
//    }

    /**
     * 任务加载器
     * @param dataSource
     * @param transactionManager
     * @return
     * @throws Exception
     */
//    @Bean("jobLauncher")
//    public JobLauncher jobLauncher(DataSource dataSource, PlatformTransactionManager transactionManager)
//            throws Exception {
//        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
//        jobLauncher.setJobRepository(jobRepository(dataSource, transactionManager));
//        return jobLauncher;
//    }

    @Bean("stepBuilderFactory")
    public StepBuilderFactory getStepBuilderFactory(JobRepository jobRepository,PlatformTransactionManager transactionManager){
        return new StepBuilderFactory(jobRepository,transactionManager);
    }

    @Bean("jobBuilderFactory")
    public JobBuilderFactory getJobBuilderFactory(JobRepository jobRepository){
        return new JobBuilderFactory(jobRepository);
    }

    @Bean("batchJobRegister")
    public BatchJobRegister getBatchJobRegister(){
        return new BatchJobRegister();
    }

    @Bean
    public TaskScheduler getTaskScheduler(){
        DefaultManagedTaskScheduler taskScheduler = new DefaultManagedTaskScheduler();
        taskScheduler.setJndiName(properties.getTaskSchedulerProperties().getJndiName());
        return taskScheduler;
    }

    @Bean
    public ApplicationStartup getApplicationStartup(){
        return new ApplicationStartup();
    }

    @Bean
    public BatchJobService getBatchJobService(){
        return new BatchJobService();
    }
    /**
     * JOB运行时间记录
     * @return
     */
    @Bean
    public BatchLoggerMonitorListener getBatchLoggerMonitorListener(BatchJobService batchJobService){
        BatchLoggerMonitorListener keepListener = new BatchLoggerMonitorListener();
        return keepListener;
    }

    @Bean
    public BatchLauncherLockService getBatchLauncherLockService(){
        BatchLauncherLockService lockService = new BatchLauncherLockServiceByCache();
        return lockService;
    }

    @Bean("dataTableBatchLogDao")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "com.vekai.batch", name = "logdao", havingValue = "DataTable", matchIfMissing = true)
    public BatchLogDao getDataTableBatchLogDao(){
        return new BatchLogDaoDBImpl();
    }

    @Bean("batchLogDaoRedisTemplate")
    @ConditionalOnProperty(prefix = "com.vekai.batch", name = "logdao", havingValue = "Redis")
    public RedisTemplate<String, Object> getBatchLogDaoRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        if(redisConnectionFactory instanceof JedisConnectionFactory){
//            ((JedisConnectionFactory)redisConnectionFactory).setDatabase(properties.getRedisDatabase());
//        }
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();

        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(stringSerializer);
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setDefaultSerializer(new JdkSerializationRedisSerializer());

        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    @Bean("redisBatchLogDao")
    @DependsOn("batchLogDaoRedisTemplate")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "com.vekai.batch", name = "logdao", havingValue = "Redis")
    public BatchLogDao getRedisBatchLogDao(@Autowired @Qualifier("batchLogDaoRedisTemplate") RedisTemplate<String, Object> redisTemplate){
        BatchLogDaoRedisImpl logDaoRedis = new BatchLogDaoRedisImpl();
        logDaoRedis.setBatchProperties(properties);
        logDaoRedis.setRedisTemplate(redisTemplate);

        return logDaoRedis;
    }


    /**
     * 批量模块自己启动后，把信息注册到数据表中，如果配置了表达式，则按表达式启动
     */
    public static class ApplicationStartup implements ApplicationListener<ContextRefreshedEvent> {
        @Override
        public void onApplicationEvent(ContextRefreshedEvent event) {
            BatchJobRegister jobRegister = ApplicationContextHolder.getBean(BatchJobRegister.class);
            BatchJobService batchJobService = ApplicationContextHolder.getBean(BatchJobService.class);
            List<JobWrapper> jobWrappers = jobRegister.getJobs();
            batchJobService.registerSynchronize(jobWrappers);
            logger.info("vekai-BATCH批量处理模块加载完成");
        }
    }




}