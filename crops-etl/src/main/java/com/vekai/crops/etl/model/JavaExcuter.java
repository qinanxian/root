package com.vekai.crops.etl.model;

import com.vekai.crops.etl.job.ScheduleJob;

public interface JavaExcuter {

    JavaResult execute(ScheduleJob scheduleJob);
}