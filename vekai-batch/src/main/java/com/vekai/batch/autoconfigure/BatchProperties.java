package com.vekai.batch.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 基础框架本配置类
 */
@ConfigurationProperties(prefix = "com.vekai.batch", ignoreUnknownFields = true)
public class BatchProperties {
    private TaskSchedulerProperties taskSchedulerProperties = new TaskSchedulerProperties();
    private String logdao = "DataTable";
    private int redisDatabase = 1;
    private String logdaoRedisKeyPrefix = "vekai-BATCH";

    public TaskSchedulerProperties getTaskSchedulerProperties() {
        return taskSchedulerProperties;
    }

    public void setTaskSchedulerProperties(TaskSchedulerProperties taskSchedulerProperties) {
        this.taskSchedulerProperties = taskSchedulerProperties;
    }

    public static class TaskSchedulerProperties{
        private String JndiName = null;

        public String getJndiName() {
            return JndiName;
        }

        public void setJndiName(String jndiName) {
            JndiName = jndiName;
        }
    }

    public String getLogdao() {
        return logdao;
    }

    public void setLogdao(String logdao) {
        this.logdao = logdao;
    }

    public int getRedisDatabase() {
        return redisDatabase;
    }

    public void setRedisDatabase(int redisDatabase) {
        this.redisDatabase = redisDatabase;
    }

    public String getLogdaoRedisKeyPrefix() {
        return logdaoRedisKeyPrefix;
    }

    public void setLogdaoRedisKeyPrefix(String logdaoRedisKeyPrefix) {
        this.logdaoRedisKeyPrefix = logdaoRedisKeyPrefix;
    }
}
