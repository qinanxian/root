package com.vekai.crops.etl.service;

import com.vekai.crops.etl.job.ScheduleJob;
import com.vekai.crops.etl.model.JavaExcuter;
import com.vekai.crops.etl.model.JavaResult;
import cn.fisok.raw.holder.ApplicationContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class JavaExecuteService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RestTemplate restTemplate;

    public JavaResult run(ScheduleJob scheduleJob) throws Exception{
        String className = scheduleJob.getDefineFile();
        return execFlowEventAction(className, scheduleJob);
    }


    private JavaResult execFlowEventAction(String className, ScheduleJob scheduleJob){
        LOGGER.info("执行Java执行器Class:"+className);
        JavaExcuter javaExcuter = ApplicationContextHolder.getBeanByClassName(className);
        return javaExcuter.execute(scheduleJob);
    }
}
