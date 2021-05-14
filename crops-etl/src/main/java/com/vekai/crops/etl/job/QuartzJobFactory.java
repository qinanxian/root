package com.vekai.crops.etl.job;

import com.vekai.crops.etl.model.JavaResult;
import com.vekai.crops.etl.model.KettleResult;
import com.vekai.crops.etl.model.KettleStepStatus;
import com.vekai.crops.etl.service.JavaExecuteService;
import com.vekai.crops.etl.service.KettleExecuteService;
import com.vekai.crops.etl.service.ScheduleJobDBService;
import cn.fisok.raw.kit.BeanKit;
import cn.fisok.raw.kit.DateKit;
import cn.fisok.raw.kit.JSONKit;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

public class QuartzJobFactory implements Job {
	private static Logger logger = LoggerFactory.getLogger(QuartzJobFactory.class);
	@Autowired
	private KettleExecuteService kettleExecuteService;
	@Autowired
	private JavaExecuteService javaExecuteService;
	@Autowired
	private ScheduleJobDBService scheduleJobDBService;

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    	ScheduleJob scheduleJobMeta = (ScheduleJob)jobExecutionContext.getMergedJobDataMap().get("scheduleJob");
        String jobId = scheduleJobMeta.getJobId();
        // 以数据库中的为准
		ScheduleJob scheduleJob = scheduleJobDBService.getJob(jobId);
		String jobStatus = scheduleJob.getJobStatus();

		if("RUNNING".equals(jobStatus)) throw new RuntimeException("Job"+jobId+"已在运行中");

		logger.info("Excute job start:" +scheduleJob.toString());
		long startTime = System.currentTimeMillis();
		String startTimeStr = DateKit.format(DateKit.parse(startTime));

		// 记录历史信息
		ScheduleJobHist scheduleJobHist = new ScheduleJobHist();
		BeanKit.copyProperties(scheduleJob, scheduleJobHist);
		scheduleJobHist.setStartTime(startTimeStr);
		scheduleJobHist.setResult("succeed");

		scheduleJob.setLatestTime(startTimeStr);
		scheduleJob.setLatestRst("succeed");

        scheduleJobDBService.updateJobStatus(jobId, "RUNNING");

		/**
		 * 此处可根据类型判断
		 * 1. 触发etl
		 * 2. 执行update刷数
		 */
		String jobType = scheduleJob.getJobType();
		try {
			if("java".equals(jobType)){
				JavaResult javaResult = javaExecuteService.run(scheduleJob);
				scheduleJobHist.setStepStatus(javaResult.getMsg());
			} else {
				KettleResult rst = kettleExecuteService.run(scheduleJob);
				if(rst!=null) {
					List<KettleStepStatus> kettleStepStatuses = rst.getStepStatus();
					if(kettleStepStatuses!=null&&kettleStepStatuses.size()>0) scheduleJobHist.setStepStatus(JSONKit.toJsonString(kettleStepStatuses));
					if(rst.getErrors()>0) {
						scheduleJobHist.setResult("failed");
						scheduleJob.setLatestRst("failed");
						scheduleJobHist.setZzErrorMsg(rst.getErrorMsg());
					}
				}
			}
		} catch (Exception e) {
			String errorInfoFromException = getErrorInfoFromException(e);
			logger.error("Excute job error:", e);
			scheduleJobHist.setZzErrorMsg(errorInfoFromException);
			scheduleJobHist.setResult("failed");
			scheduleJob.setLatestRst("failed");
		}

		logger.info("Excute job end:" +scheduleJob.toString());
		long endTime = System.currentTimeMillis();
		String endTimeStr = DateKit.format(DateKit.parse(endTime));
		scheduleJobHist.setEndTime(endTimeStr);
		scheduleJobHist.setTotalTime(String.valueOf(endTime-startTime));

		scheduleJob.setJobStatus("NORMAL");
		scheduleJobDBService.addJobHist(scheduleJobHist);
		scheduleJobDBService.updateJob(scheduleJob);
	}

	private String getErrorInfoFromException(Exception e) {
		try {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			String s = sw.toString();
			sw.close();
			pw.close();
			return "\r\n" + s + "\r\n";
		} catch (Exception ex) {
			return "获得Exception信息的工具类异常";
		}
	}

}
