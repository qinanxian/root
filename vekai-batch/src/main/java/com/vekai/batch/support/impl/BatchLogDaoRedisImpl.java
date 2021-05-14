package com.vekai.batch.support.impl;

import com.vekai.batch.autoconfigure.BatchProperties;
import com.vekai.batch.entity.ApnsBatchLog;
import com.vekai.batch.support.BatchLogDao;
import cn.fisok.raw.kit.MapKit;
import cn.fisok.raw.kit.StringKit;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Map;


/**
 * 使用Redis记录批量日志
 */
public class BatchLogDaoRedisImpl implements BatchLogDao {

//    @Autowired
//    @Qualifier("batchLogDaoRedisTemplate")
    protected RedisTemplate redisTemplate;
//    @Autowired
    protected BatchProperties batchProperties;

    public RedisTemplate getRedisTemplate() {
        return redisTemplate;
    }


    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public BatchProperties getBatchProperties() {
        return batchProperties;
    }

    public void setBatchProperties(BatchProperties batchProperties) {
        this.batchProperties = batchProperties;
    }

    private String getJobRedisKey(Long jobInstanceId){
        String jobInstanceIdStr = ""+jobInstanceId;
        Map<String,String> vars = MapKit.mapOf(
                "fixedKey",batchProperties.getLogdaoRedisKeyPrefix(),
                "jobInstanceId",jobInstanceIdStr);
        String redisKey = StringKit.fillTpl("${fixedKey}-${jobInstanceId}",vars);
        return redisKey;
    }
    public int insertBatchLogList(List<ApnsBatchLog> logList) {
        if(logList == null || logList.isEmpty())return 0;

        String redisKey = getJobRedisKey(logList.get(0).getJobInstanceId());
        redisTemplate.opsForList().rightPushAll(redisKey,logList);
        return logList.size();
    }

    public int insertBatchLog(ApnsBatchLog logItem) {
        String redisKey = getJobRedisKey(logItem.getJobInstanceId());
        redisTemplate.opsForList().rightPushAll(redisKey,logItem);
        return 1;
    }

    public List<ApnsBatchLog> getBatchLogList(Long jobInstanceId){
        String redisKey = getJobRedisKey(jobInstanceId);
//        Long size = redisTemplate.opsForList().size(redisKey);
        return redisTemplate.opsForList().range(redisKey,0,-1);
    }

}
