package com.vekai.dataform.validator;

import java.io.Serializable;
import java.util.*;

/**
 * 校验结果，对每一条数据的校验结果
 */
public class ValidateResult implements Serializable{
    private int success = 0;
    private int warn = 0;
    private int fail = 0;

    /**
     * KEY = 业务要素代码
     * VALUE = 校验结果（一个业务要素可能有多条校验）
     */
    private Map<String,List<ValidateRecord>> records = new LinkedHashMap<String,List<ValidateRecord>>();

    public boolean getPassed(){

        Iterator<String> iterator = records.keySet().iterator();
        while(iterator.hasNext()){
            String name = iterator.next();
            List<ValidateRecord> recordList = records.get(name);
            for(ValidateRecord record : recordList){
                if(record.isMandatory() && !record.isPassed()){         //强制项没通过的，失败
                    fail += 1;
                }else if(!record.isMandatory() && !record.isPassed()){  //非强制项，没通过的，警告
                    warn += 1;
                }else{
                    success += 1;
                }
            }
        }

        return fail <= 0 ;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getWarn() {
        return warn;
    }

    public void setWarn(int warn) {
        this.warn = warn;
    }

    public int getFail() {
        return fail;
    }

    public void setFail(int fail) {
        this.fail = fail;
    }

    public Map<String, List<ValidateRecord>> getRecords() {
        return records;
    }

    public void setRecords(Map<String, List<ValidateRecord>> records) {
        this.records = records;
    }

    public ValidateResult addRecord(String key,ValidateRecord record){
        List<ValidateRecord> vrList = records.get(key);
        if(vrList == null ){
            vrList = new ArrayList<ValidateRecord>();
        }
        vrList.add(record);
        if(!record.isPassed()){
            if(record.isMandatory()){
                this.fail +=1;
            }else{
                this.warn += 1;
            }
        }else{
            this.success += 1;
        }

        records.put(key,vrList);

        return this;
    }
}
