package com.vekai.batch.listener;

import com.vekai.batch.core.BatchException;
import com.vekai.batch.entity.TimerEntity;
import com.vekai.batch.server.BatchWebSockerHolder;
import com.vekai.batch.service.BatchJobService;
import com.vekai.batch.support.BatchLogDao;
import com.vekai.batch.support.LogAppenderSupportHolder;
import cn.fisok.raw.holder.GlobalSupportHolder;
import cn.fisok.raw.kit.DateKit;
import cn.fisok.raw.kit.JSONKit;
import cn.fisok.raw.kit.StringKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.batch.core.*;
import org.springframework.batch.core.listener.ItemListenerSupport;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * 最近一次执行情况
 */
//@Component
public class BatchLoggerMonitorListener extends ItemListenerSupport implements ChunkListener, StepExecutionListener, JobExecutionListener {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BatchJobService batchJobService;
    @Autowired
    private BatchLogDao batchLogDao;

    /**
     * 启动之前，记一下
     * @param jobExecution
     */
    public void beforeJob(JobExecution jobExecution) {
        Date startTime = DateKit.now();
//        Date startTime = jobExecution.getCreateTime();
        BatchStatus status = jobExecution.getStatus();

        Long jobId = jobExecution.getJobId();
        String jobName = batchJobService.getJobName(jobId);
        TimerEntity timerEntity = batchJobService.getTimerEntity(jobName);
        if(timerEntity == null){
            throw new BatchException("jobId=[{0}],jobName=[{1}]没有找到",jobId,jobName);
        }

        MDC.put("BATCH_JOB_NAME",timerEntity.getTimerName());  //这个参数，日志自定义的Appender会用得上
        MDC.put("BATCH_JOB_KEY",timerEntity.getTimerKey());  //这个参数，日志自定义的Appender会用得上
        MDC.put("BATCH_JOB_ID",""+jobExecution.getJobId());
        LogAppenderSupportHolder.setBatchLogDao(batchLogDao);

        logger.info(StringKit.format("开始-运行JOB:{0}-{1}[{2,number,#}]",timerEntity.getTimerKey(),timerEntity.getTimerName(),jobId));
        if(timerEntity != null){
            timerEntity.setLastExecStatus(status.toString());
            timerEntity.setLastExecStartTime(startTime);
            timerEntity.setIsRunning("Y");
            batchJobService.saveTimerEntity(timerEntity);
        }
        BatchWebSockerHolder.sendBroadcastMessage(BatchWebSockerHolder.responseObject("beforeJob",batchJobService.getTimers()).jsonText());

    }

    /**
     * JOB执行完成之后，记录最新运行情况
     * @param jobExecution
     */
    public void afterJob(JobExecution jobExecution) {
        Date endTime = DateKit.now();
        BatchStatus status = jobExecution.getStatus();

        Long jobId = jobExecution.getJobId();
        String jobName = batchJobService.getJobName(jobId);
        TimerEntity timerEntity = batchJobService.getTimerEntity(jobName);
        if(timerEntity != null){
            Date startTime = timerEntity.getLastExecStartTime();
            long timeCost = endTime.getTime() - startTime.getTime();
            timerEntity.setLastExecStatus(status.toString());
//            timerEntity.setLastExecStartTime(startTime);
            timerEntity.setLastExecTimeCost(timeCost);
            timerEntity.setIsRunning("N");
            batchJobService.saveTimerEntity(timerEntity);
        }
        BatchWebSockerHolder.sendBroadcastMessage(BatchWebSockerHolder.responseObject("afterJob",batchJobService.getTimers()).jsonText());

        logger.info(StringKit.format("完成-运行JOB:{0}-{1}[{2,number,#}]",timerEntity.getTimerKey(),timerEntity.getTimerName(),jobId));
        logger.info(StringKit.format("JOB运行状态:{0}",jobExecution.getStatus()));

        //无论任务成功或失败，都会运行到这里来
        MDC.clear();
        LogAppenderSupportHolder.clear();
    }

    @Override
    public void afterRead(Object item) {
//        logger.trace(StringKit.format("完成-读取数据:{0}",JSONKit.toJsonString(item)));
    }

    @Override
    public void onReadError(Exception ex) {
        logger.error("读取数据错误",ex);
    }

    @Override
    public void afterProcess(Object item, Object result) {
        logger.trace(StringKit.format("处理数据-输入:{0}", JSONKit.toJsonString(item)));
        logger.trace(StringKit.format("处理数据-输出:{0}",JSONKit.toJsonString(result)));
    }

    @Override
    public void onProcessError(Object item, Exception e) {
        logger.error(StringKit.format("处理数据出错:{0}",JSONKit.toJsonString(item)),e);
    }


    @Override
    public void beforeWrite(List item) {
        logger.trace(StringKit.format("准备-写入数据:{0}",JSONKit.toJsonString(item)));
    }

    @Override
    public void onWriteError(Exception ex, List item) {
        logger.error(StringKit.format("错误-写入数据:{0}",JSONKit.toJsonString(item)),ex);
    }

    public void beforeChunk(ChunkContext context) {
        String stepName = context.getStepContext().getStepName();
        logger.debug(StringKit.format("开始-运行CHUNK:{0}",stepName));
        GlobalSupportHolder.auditEnable(false);
    }

    public void afterChunk(ChunkContext context) {
        String stepName = context.getStepContext().getStepName();

        ExitStatus exitStatus = context.getStepContext().getStepExecution().getExitStatus();
        logger.debug(StringKit.format("结束-运行CHUNK:{0},状态:{1}",stepName,exitStatus.getExitCode()));
        GlobalSupportHolder.clearAudit();
    }

    public void afterChunkError(ChunkContext context) {
        String stepName = context.getStepContext().getStepName();
        ExitStatus exitStatus = context.getStepContext().getStepExecution().getExitStatus();
        logger.error(StringKit.format("出错-运行CHUNK:{0},状态:{1}",stepName,exitStatus.getExitCode()));
        GlobalSupportHolder.clearAudit();
    }

    public void beforeStep(StepExecution stepExecution) {
        String stepName = stepExecution.getStepName();
        logger.debug(StringKit.format("开始-运行STEP:{0}",stepName));
    }

    public ExitStatus afterStep(StepExecution stepExecution) {
        String stepName = stepExecution.getStepName();
        ExitStatus exitStatus = stepExecution.getExitStatus();
        logger.debug(StringKit.format("结束-运行STEP:{0},状态:{1}",stepName,exitStatus.getExitCode()));
        return exitStatus;
    }
}
