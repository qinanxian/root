package com.vekai.crops.etl.service.excuter;

import com.vekai.crops.etl.common.micro.MicroServerName;
import com.vekai.crops.etl.job.ScheduleJob;
import com.vekai.crops.etl.model.JavaExcuter;
import com.vekai.crops.etl.model.JavaResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * 测试定时任务添加数据
 */
@Component
public class HttpAutoMoveCredit implements JavaExcuter {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @Autowired
    com.vekai.crops.etl.autoconfigure.CropsETLProperties etlProperties;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public JavaResult execute(ScheduleJob scheduleJob) {
        JavaResult javaResult = new JavaResult();

        String rstMsg = restTemplate.getForObject(MicroServerName.risk_service_http+"/file/etl/moveCredit", String.class);

        javaResult.setMsg(rstMsg);
        LOGGER.info("迁移征信授权文件："+rstMsg);

        return javaResult;
    }
}
