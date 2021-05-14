package com.vekai.lact.core.transaction;

import cn.fisok.raw.lang.MapObject;
import com.vekai.lact.model.LactLoan;

/**
 * 核算交易
 * <dd> 1.对核算引擎输入参数</dd>
 * <dd> 2.进行初始化,加载相关业务数据</dd>
 * <dd> 3.调用核算交易</dd>
 * <dd> 4.输出核算处理结果</dd>
 * 
 * @author 杨松<syang@amarsoft.com>
 * @date 2016年12月6日
 */
public abstract class LactTransaction<T extends LactLoan> {
    private String code;
    private String name;
    private String reverseCode;
    private String reverseName;
 
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getReverseCode() {
        return reverseCode;
    }
    public void setReverseCode(String reverseCode) {
        this.reverseCode = reverseCode;
    }
    public String getReverseName() {
        return reverseName;
    }
    public void setReverseName(String reverseName) {
        this.reverseName = reverseName;
    }
    
    
    /**
     * 执行反冲交易
     * @param parameter
     */
    public abstract void reverse(T dataObject, MapObject parameter);

    /**
     * 交易第一步：加载数据
     * @param dataObject
     * @param parameter
     */
    public abstract void load(T dataObject, MapObject parameter);

    /**
     * 交易第二步：处理逻辑
     * @param dataObject
     */
    public abstract void handle(T dataObject);

}
