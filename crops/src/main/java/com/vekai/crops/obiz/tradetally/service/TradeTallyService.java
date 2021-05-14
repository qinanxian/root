package com.vekai.crops.obiz.tradetally.service;

import cn.fisok.sqloy.core.BeanCruder;
import com.vekai.crops.obiz.tradetally.entity.ObizTradeTally;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 交易流水处理服务
 */
@Service
public class TradeTallyService {
    @Autowired
    BeanCruder beanCruder;

    public int appendTradeTally(ObizTradeTally tradeTally){
        int ret = beanCruder.insert(tradeTally);
        return ret;
    }
}
