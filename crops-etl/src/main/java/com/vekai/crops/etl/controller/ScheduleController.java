package com.vekai.crops.etl.controller;

import com.vekai.crops.etl.common.Message;
import com.vekai.crops.etl.job.ScheduleJob;
import com.vekai.crops.etl.service.ScheduleJobDBService;
import com.vekai.crops.etl.service.ScheduleJobService;
import org.quartz.SchedulerException;
import org.quartz.SchedulerMetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {
	private static Logger logger = LoggerFactory.getLogger(ScheduleController.class);

	@Autowired
	private ScheduleJobService scheduleJobService;
	@Autowired
	private ScheduleJobDBService scheduleJobDBService;
	
	@RequestMapping("/metaData")
	public Object metaData() throws SchedulerException {
		SchedulerMetaData metaData = scheduleJobService.getMetaData();
		return metaData;
	}
	
	@RequestMapping("/getAllJobs")
	public Object getAllJobs() throws SchedulerException {
		List<ScheduleJob> jobList = scheduleJobService.getAllJobList(); 
		return jobList;
	}
	
	@RequestMapping("/getRunningJobs")
	public Object getRunningJobs() throws SchedulerException {
		List<ScheduleJob> jobList = scheduleJobService.getRunningJobList();
		return jobList;
	}
	
	@PostMapping(value = "/pauseJob")
	public Object pauseJob(@RequestBody JobDTO jobDTO){
		logger.info("pauseJob, jobId = {}", jobDTO.toString());
		Message message = Message.failure();
		try {
			scheduleJobService.pauseJob(jobDTO.getJobId(), jobDTO.getJobGroup());
			message = Message.success();
		} catch (Exception e) {
			message.setMsg(e.getMessage());
			logger.error("pauseJob ex:", e);
		}
		return message;
	}
	
	@PostMapping(value = "/resumeJob")
	public Object resumeJob(@RequestBody JobDTO jobDTO){
        logger.info("resumeJob, jobId = {}", jobDTO.toString());
		Message message = Message.failure();
		try {
			scheduleJobService.resumeJob(jobDTO.getJobId(), jobDTO.getJobGroup());
			message = Message.success();
		} catch (Exception e) {
			message.setMsg(e.getMessage());
			logger.error("resumeJob ex:", e);
		}
		return message;
	}
	
	
	@PostMapping(value = "/deleteJob")
	public Object deleteJob(@RequestBody JobDTO jobDTO){
        logger.info("deleteJob, jobId = {}", jobDTO.toString());
		Message message = Message.failure();
		try {
			scheduleJobService.deleteJob(jobDTO.getJobId(), jobDTO.getJobGroup());
			message = Message.success();
		} catch (Exception e) {
			message.setMsg(e.getMessage());
			logger.error("deleteJob ex:", e);
		}
		return message;
	}
	
	@PostMapping(value = "/runJob")
	public Object runJob(@RequestBody JobDTO jobDTO){
        logger.info("runJob, jobId = {}", jobDTO.toString());
		Message message = Message.failure();
		try {
			scheduleJobService.runJobOnce(jobDTO.getJobId(), jobDTO.getJobGroup());
			message = Message.success();
		} catch (Exception e) {
			message.setMsg(e.getMessage());
			logger.error("runJob ex:", e);
		}
		return message;
	}
	
	
	@PostMapping(value = "/saveOrUpdate")
	public Object saveOrupdate(@RequestBody JobDTO jobDTO){
		ScheduleJob job = scheduleJobDBService.getJob(jobDTO.getJobId());

		logger.info("params, job = {}", job);
		Message message = Message.failure();
		try {
			scheduleJobService.saveOrupdate(job);
			message = Message.success();
		} catch (Exception e) {
			message.setMsg(e.getMessage());
			logger.error("updateCron ex:", e);
		}
		return message;
	}


}
