package com.vekai.batch.support;

import com.vekai.batch.entity.ApnsBatchLog;

import java.util.List;

public interface BatchLogDao {
    int insertBatchLogList(List<ApnsBatchLog> logList);
    int insertBatchLog(ApnsBatchLog logItem);
    List<ApnsBatchLog> getBatchLogList(Long jobInstanceId);
}
