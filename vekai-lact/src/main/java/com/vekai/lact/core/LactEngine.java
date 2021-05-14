package com.vekai.lact.core;

import com.vekai.lact.core.transaction.LactTransaction;
import com.vekai.lact.exception.LactTransactionDefinitionException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 核算交易处理引擎<br/>
 * <dd> 1.对核算引擎输入参数</dd>
 * <dd> 2.进行初始化,加载相关业务数据</dd>
 * <dd> 3.调用核算交易</dd>
 * <dd> 4.输出核算处理结果</dd>
 * 
 * 
 * @author 杨松<syang@amarsoft.com>
 * @date 2016年12月6日
 */
public class LactEngine {
    private Map<String, LactTransaction> transactionPool = new HashMap<String,LactTransaction>();
    
    public LactTransaction getTransaction(String transCode){
        LactTransaction at = transactionPool.get(transCode);
        if(at == null) throw new LactTransactionDefinitionException("transation not exists,transCode="+transCode);
        return at;
    }
    
    public void setTransactions(List<LactTransaction> transList){
        if(transList != null){
            for(int i=0;i<transList.size();i++){
                LactTransaction atItem = transList.get(i);
                if(transactionPool.containsKey(atItem.getCode())){
                    throw new LactTransactionDefinitionException("accounting transaction define duplicate,transCode="+atItem.getCode());
                }
                transactionPool.put(atItem.getCode(), atItem);
            }
        }
    }
}
