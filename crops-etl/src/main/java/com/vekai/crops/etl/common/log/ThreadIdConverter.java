package com.vekai.crops.etl.common.log;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.jboss.logging.MDC;

public class ThreadIdConverter extends ClassicConverter {
    /**
       * 当需要显示线程ID的时候，返回当前调用线程的ID
       */
    @Override
    public String convert(ILoggingEvent event) {
        String threadId = String.valueOf(Thread.currentThread().getId());

        MDC.put("threadId", threadId);

        return threadId;
    }
}