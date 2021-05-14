package com.vekai.crops.etl.service;

import com.vekai.crops.etl.job.QuartzJobFactory;
import com.vekai.crops.etl.job.ScheduleJob;
import com.vekai.crops.etl.job.ScheduleJobHist;
import cn.fisok.raw.kit.ValidateKit;
import cn.fisok.sqloy.core.BeanCruder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScheduleJobDBService {
    private static Logger logger = LoggerFactory.getLogger(QuartzJobFactory.class);
    @Autowired
    private BeanCruder beanCruder;


    public List<ScheduleJob> getAllJob() {
        List<ScheduleJob> jobList = beanCruder.selectList(ScheduleJob.class, "select * from ETL_SCHEDULE_JOB");
        return jobList;
    }

    public ScheduleJob getJob(String jobId) {
        ValidateKit.notBlank(jobId, "JobId未传入");
        ScheduleJob job = beanCruder.selectOne(ScheduleJob.class, "select * from ETL_SCHEDULE_JOB where JOB_ID=:jobId",
                "jobId", jobId);
        ValidateKit.notNull(job, "JobId:{0}不存在对应的Job", jobId);
        return job;
    }

    public int updateJob(ScheduleJob scheduleJob) {
        ValidateKit.notNull(scheduleJob, "传入JOB为null");
        ValidateKit.notBlank(scheduleJob.getJobId(), "JobId为空");
        int update = beanCruder.update(scheduleJob);
        return update;
    }

    public int updateJobStatus(String jobId, String status) {
        ValidateKit.notBlank(jobId, "JobId未传入");
        ValidateKit.notBlank(status, "JobStatus未传入");
        Map paramMap = new HashMap<String, Object>();
        paramMap.put("status", status);
        paramMap.put("jobId", jobId);

        return beanCruder.execute("update ETL_SCHEDULE_JOB set JOB_STATUS=:status where JOB_ID=:jobId", paramMap);
    }

    public int addJobHist(ScheduleJobHist scheduleJobHist){
        ValidateKit.notNull(scheduleJobHist, "传入历史JOB为null");
        int insert = beanCruder.insert(scheduleJobHist);
        return insert;
    }
}
