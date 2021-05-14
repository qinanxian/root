package com.vekai.crops.etl.service;

import cn.fisok.raw.lang.MapObject;
import cn.hutool.core.io.FileUtil;
import com.vekai.crops.etl.job.ScheduleJob;
import com.vekai.crops.etl.model.KettleResult;
import com.vekai.crops.etl.model.KettleStepStatus;
import com.vekai.crops.etl.service.kettle.ETLSynFlagQuery;
import cn.fisok.raw.kit.DateKit;
import cn.fisok.raw.kit.JSONKit;
import cn.fisok.sqloy.core.MapObjectCruder;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.logging.KettleLogStore;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.core.plugins.PluginFolder;
import org.pentaho.di.core.plugins.StepPluginType;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

@Service
public class KettleExecuteService {
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private MapObjectCruder mapObjectCruder;
    @Autowired
    com.vekai.crops.etl.autoconfigure.CropsETLProperties etlProperties;
    @Autowired
    ETLSynFlagQuery etlSynFlagQuery;

    public KettleResult run(ScheduleJob scheduleJob) throws Exception{
        String kettleSavePath = etlProperties.getKettleSavePath();
        String filePath = scheduleJob.getDefineFile();
        File file = FileUtil.file(kettleSavePath+File.separator+filePath);
        String path = file.getPath();

        /**
         * 加载插件
         */
        File pluginsPath = new File(kettleSavePath + File.separator + "plugins/");
        File[] pluginsFolder = pluginsPath.listFiles();
        for (File pluginFolder:pluginsFolder){
            StepPluginType.getInstance().getPluginFolders().add(new PluginFolder(pluginFolder.getPath(),false,true));
        }
        KettleEnvironment.init();
        TransMeta transMeta = new TransMeta(path);
        Trans trans = new Trans(transMeta);

        Date now = DateKit.now();
        Date lastDay = DateKit.plusDays(now, -1);
        String lastDayString = DateKit.format(lastDay, "yyyy-MM-dd");

        trans.setParameterValue("DATADATE", "'"+lastDayString+"'");
        transMeta.setParameterValue("DATADATE", "'"+lastDayString+"'");
//
//        try {
//            List<String> odsTables = etlSynFlagQuery.queryODSSuccessTables(lastDayString);
//            List<String> erdmTables = etlSynFlagQuery.queryERDMSuccessTables(lastDayString);
//
//            String odsTablesString = tableNameToIn(odsTables);
//            String erdmTablesString = tableNameToIn(erdmTables);
//
//            trans.setParameterValue("ODS_TB", odsTablesString);
//            trans.setParameterValue("ERDM_TB", erdmTablesString);
//            transMeta.setParameterValue("ODS_TB", odsTablesString);
//            transMeta.setParameterValue("ERDM_TB", erdmTablesString);
//        } catch (Exception e) {
//            LOGGER.error("获取ETL同步标识出错", e);
//        }
//
        Map<String, String> vars = queryETLConfig();

        for(String key:vars.keySet()){
            LOGGER.info("Kettle运行加入参数key:"+key+" value:"+vars.get(key));
            transMeta.setParameterValue(key, vars.get(key));
            transMeta.setParameterValue(key, vars.get(key));
        }



        /**
         * Nothing 没有日志 不显示任何输出
         * Error 错误日志 仅仅显示错误信息
         * Minimal 最小日志 使用最小的日志
         * Basic 基本日志 缺省的日志级别
         * Detailed详细日志 给出日志输出的细节
         * Debug 调试日志 调试目的，调试输出
         * Rowlevel行级日志 打印出每一行记录的信息
         */
        String logLevel = vars.get("LogLevel");
        if(null!=logLevel&&!"".equals(logLevel)){
            LogLevel level = LogLevel.getLogLevelForCode(logLevel);
            if(null!=level){
                trans.setLogLevel(level);
            }
        }

        trans.prepareExecution(null);

        trans.startThreads();
        trans.waitUntilFinished();

        KettleResult kettleResult = new KettleResult();
        List<KettleStepStatus> kettleStepStatuses = new ArrayList<>();
        if (trans.isFinished()) {
            for (int i = 0; i < trans.nrSteps(); i++) {
                StepInterface baseStep = trans.getRunThread(i);
                StepStatus stepStatus = new StepStatus(baseStep);

                //fields即为步骤度量信息
                String[] fields = stepStatus.getTransLogFields();
                String stepName = fields[1];
                String copyRow = fields[2];
                String readRow = fields[3];
                String writeNum = fields[4];
                String inputNum = fields[5];
                String outputNum = fields[6];
                String updataNum = fields[7];
                String rejectNum = fields[8];
                String errorNum = fields[9];
                String statues = fields[10];
                String useTime = fields[11];
                String speed = fields[12];

                KettleStepStatus kettleStepStatus = new KettleStepStatus();
                kettleStepStatus.setStepName(stepName);
                kettleStepStatus.setCopyRow(copyRow);
                kettleStepStatus.setReadRow(readRow);
                kettleStepStatus.setWriteNum(writeNum);
                kettleStepStatus.setInputNum(inputNum);
                kettleStepStatus.setOutputNum(outputNum);
                kettleStepStatus.setUpdataNum(updataNum);
                kettleStepStatus.setRejectNum(rejectNum);
                kettleStepStatus.setErrorNum(errorNum);
                kettleStepStatus.setStatues(statues);
                kettleStepStatus.setUseTime(useTime);
                kettleStepStatus.setSpeed(speed);

                kettleStepStatuses.add(kettleStepStatus);
            }
            // List倒序
            Collections.reverse(kettleStepStatuses);
            kettleResult.setStepStatus(kettleStepStatuses);


            int errors = trans.getErrors();
            kettleResult.setErrors(errors);
            if (errors > 0) {
                String errMsg = KettleLogStore.getAppender().getBuffer(trans.getLogChannelId(), false).toString();

                int errMsgLength = errMsg.length();
                if(errMsgLength>3000){
                    errMsg = errMsg.substring(errMsgLength-3000, errMsgLength);
                }

                String[] errMsgArray = errMsg.split("\r\n");

                kettleResult.setErrorMsg(JSONKit.toJsonString(errMsgArray));
            }
            KettleLogStore.getAppender().clear();// 清空日志缓冲区


            return kettleResult;
        } else {
            return null;
        }
    }

    private String tableNameToIn(List<String> tables){
        StringBuffer tablesString = new StringBuffer("");
        int size = tables.size();
        if(size==0) return "''";
        for(int i=0;i<size;i++){
            if(i==0) tablesString.append("'");
            tablesString.append(tables.get(i));
            if(i<tables.size()-1) {
                tablesString.append("','");
            } else {
                tablesString.append("'");
            }

        }
        return tablesString.toString();
    }


    private Map<String,String> queryETLConfig(){
        Map<String,String> confParam = new HashMap<String,String>();
        List<MapObject> mapData = mapObjectCruder.selectList("select * from ETL_CONFIG");
        for (MapObject var:mapData) {
            String varname = var.getValue("varname").strValue();
            String varvalue = var.getValue("varvalue").strValue();

            confParam.put(varname, varvalue);
        }

        return confParam;
    }
}
