package com.vekai.batch.logger;

import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.classic.spi.ThrowableProxy;
import ch.qos.logback.core.AppenderBase;
import com.vekai.batch.support.BatchLogDao;
import com.vekai.batch.support.LogAppenderSupportHolder;
import com.vekai.batch.entity.ApnsBatchLog;
import cn.fisok.raw.kit.DateKit;
import cn.fisok.raw.kit.IOKit;
import cn.fisok.raw.kit.StringKit;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoggerDBAppender extends AppenderBase<LoggingEvent> {

    @Override
    protected void append(LoggingEvent event) {
        Map<String, String> mdcMap = event.getMDCPropertyMap();
        String batchJobId = mdcMap.get("BATCH_JOB_ID");
        String batchJobKey = mdcMap.get("BATCH_JOB_KEY");
        String batchJobName = mdcMap.get("BATCH_JOB_NAME");
        if(StringKit.isBlank(batchJobName))return;
        if(StringKit.isBlank(batchJobId))return;
//        System.out.println("XXXX-->"+event.getMessage());
        //如果拿不到beanCruder，则说明不需要记录到数据表中去
        BatchLogDao batchLogDao = LogAppenderSupportHolder.getBatchLogDao();
        if(batchLogDao==null)return;

        String messageText = event.getMessage();

        List<String> messageList = new ArrayList<String>();

        StringReader reader = new StringReader(messageText);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String str = null;
        // 按行读取字符串
        try{
            while ((str = bufferedReader.readLine()) != null) {
                if(StringKit.isBlank(str))continue;
                String line = StringKit.maxLenLimit(str,2000);
//                if(line.indexOf("分钟=")>=0){
//                    System.out.println("<<<<<<<<<<---"+line);
//                }
                messageList.add(line);//最大长度限制
            }
            reader.close();
            bufferedReader.close();

            //把异常部分也输出到日志
            IThrowableProxy throwableProxy = event.getThrowableProxy();
            if(throwableProxy != null && throwableProxy instanceof ThrowableProxy){
                //把异常输出到输出流
                Throwable throwable = ((ThrowableProxy)throwableProxy).getThrowable();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                throwable.printStackTrace(new PrintStream(outputStream));

                bufferedReader = new BufferedReader(new InputStreamReader(IOKit.convertToInputStream(outputStream.toByteArray())));
                while ((str = bufferedReader.readLine()) != null) {
                    if(StringKit.isBlank(str))continue;
                    String line = StringKit.maxLenLimit(str,2000);
                    messageList.add(line);//最大长度限制
                }
                bufferedReader.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }


        List<ApnsBatchLog> logList = new ArrayList<ApnsBatchLog>();
        Long lastLineNumber = LogAppenderSupportHolder.lastLogLineNumber();
        for(int i=0;i<messageList.size();i++){
            lastLineNumber += (long)1;
            if(lastLineNumber>=64){
                System.out.println("lastLineNumber:"+lastLineNumber);
            }

            ApnsBatchLog logItem = new ApnsBatchLog();
            logItem.setJobInstanceId(Long.valueOf(batchJobId));
            logItem.setBatchJobKey(batchJobKey);
            logItem.setBatchJobName(batchJobName);
            logItem.setLoggerName(event.getLoggerName());
            logItem.setLogLevel(event.getLevel().toString());
            logItem.setThreadName(event.getThreadName());
            logItem.setLogTimestamp(DateKit.now().getTime());
            logItem.setLineNumber(lastLineNumber);
            logItem.setLogMessage(messageList.get(i));

            logList.add(logItem);
//            batchLogDao.insertBatchLog(logItem);
        }
        LogAppenderSupportHolder.lastLogLineNumber(lastLineNumber);
        batchLogDao.insertBatchLogList(logList);

    }
}


